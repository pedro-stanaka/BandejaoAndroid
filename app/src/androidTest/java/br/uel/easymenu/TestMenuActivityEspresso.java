package br.uel.easymenu;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import br.uel.easymenu.adapter.MealListAdapter;
import br.uel.easymenu.gui.MenuActivity;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.utils.JsonUtils;
import br.uel.easymenu.utils.ResourceUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.uel.easymenu.CalendarFactory.monday;
import static br.uel.easymenu.CalendarFactory.mondayPlusDays;
import static br.uel.easymenu.utils.CalendarUtils.dayOfWeekName;
import static br.uel.easymenu.utils.MatcherUtils.atPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestMenuActivityEspresso {

    private static final String SUCCESS_RESPONSE_FILE = "3-days.json";

    // We don't launch the activity in here because
    // we need to inject the mock http request before the activity onCreate
    @Rule
    public ActivityTestRule<MenuActivity> activityRule =
            new ActivityTestRule<>(MenuActivity.class, true, false);

    @Rule
    public TestAppRule appRule = new TestAppRule();

    @Before
    public void launchActivity() throws Exception {
        String response = JsonUtils.convertJsonToString(SUCCESS_RESPONSE_FILE);
        appRule.enqueueRequest(response);
        activityRule.launchActivity(new Intent());
    }

    @Test
    public void testTabTitle() throws Exception {
        onView(selectTab(monday())).check(matches(isDisplayed()));
        onView(selectTab(mondayPlusDays(1))).check(matches(isDisplayed()));
        onView(selectTab(mondayPlusDays(2))).check(matches(isDisplayed()));
    }

    @Test
    public void allDishesArePresent() throws Exception {
        onView(selectTab(monday())).perform(click());
        expandAllPeriods();

        assertAllDishes(0, 1, "Bacon", "Scrambled Eggs");
        assertAllDishes(0, 4, "Fish", "Carrot");
        assertAllDishes(0, 7, "Pasta", "Chicken");
    }

    @Test
    public void allPeriodsAreDisplayed() throws Exception {
        onView(selectTab(monday())).perform(click());

        assertPeriod(Meal.BREAKFAST);
        assertPeriod(Meal.LUNCH);
        assertPeriod(Meal.DINNER);
    }

    @Test
    public void mealWithoutDishesShowsMessage() throws Exception {
        onView(selectTab(mondayPlusDays(2))).perform(click());
        expandAllPeriods();

        String message = activityRule.getActivity().getString(R.string.empty_dishes);
        assertAllDishes(2, 4, message);
    }

    @Test(expected = NoMatchingViewException.class)
    public void mealWithPeriod() throws Exception {
        onView(selectTab(mondayPlusDays(1))).perform(click());

        // This assertion passes
        onView(withText("Banana")).check(matches(isDisplayed()));

        // This doesn't
        onView(withText(R.string.both)).check(matches(isDisplayed()));
    }

    private void assertPeriod(String period) {
        int resId = ResourceUtils.getPeriodResourceId(period);
        onView(allOf(withText(resId), isDescendantOfA(recyclerViewMatcher(0)))).check(matches(isDisplayed()));
    }

    // There is no easy way to expand parents via espresso custom matchers
    // We expand them via adapter directly
    private void expandAllPeriods() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                ViewPager viewPager = (ViewPager) activityRule.getActivity().findViewById(R.id.viewpager);
                for (int i = 0; i < viewPager.getChildCount(); i++) {
                    if (viewPager.getChildAt(i) instanceof RecyclerView) {
                        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(i);
                        MealListAdapter mealListAdapter = (MealListAdapter) recyclerView.getAdapter();
                        mealListAdapter.expandAllParents();
                    }
                }
            }
        });
    }

    private Matcher<View> selectTab(Calendar calendar) {
        String dateString = dayOfWeekName(calendar);
        return withText(startsWith(dateString));
    }

    private void assertAllDishes(int tabPosition, int position, String...dishes) {
        for(String dishName : dishes) {
            // Scrolling to view
            onView(recyclerViewMatcher(tabPosition)).perform(RecyclerViewActions.scrollToPosition(position));
            onView(recyclerViewMatcher(tabPosition)).check(matches(atPosition(position++, withText(dishName))));
        }
    }

    private Matcher<View> recyclerViewMatcher(int position) {
        return allOf(withId(R.id.meals_list), withTagValue(Matchers.<Object>equalTo(position)));
    }
}
