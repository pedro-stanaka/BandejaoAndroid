package br.uel.easymenu;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import br.uel.easymenu.adapter.MealListAdapter;
import br.uel.easymenu.gui.MenuActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.uel.easymenu.CalendarFactory.monday;
import static br.uel.easymenu.CalendarFactory.mondayPlusDays;
import static br.uel.easymenu.utils.CalendarUtils.dayOfWeekName;
import static org.hamcrest.core.AllOf.allOf;
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

    @Test
    public void testTabTitle() throws Exception {
        appRule.enqueueRequestFile(SUCCESS_RESPONSE_FILE);
        activityRule.launchActivity(new Intent());

        getTab(monday()).check(matches(isDisplayed()));
        getTab(mondayPlusDays(1)).check(matches(isDisplayed()));
        getTab(mondayPlusDays(2)).check(matches(isDisplayed()));
    }

    private ViewInteraction getTab(Calendar calendar) {
        String dateString = dayOfWeekName(calendar);
        return onView(withText(startsWith(dateString)));
    }

    @Test
    public void testPeriodDisplay() throws Exception {
        appRule.enqueueRequestFile(SUCCESS_RESPONSE_FILE);
        activityRule.launchActivity(new Intent());

        getTab(mondayPlusDays(0)).perform(click());

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) activityRule.getActivity().findViewById(R.id.meals_list);
                MealListAdapter mealListAdapter = (MealListAdapter) recyclerView.getAdapter();
                mealListAdapter.expandAllParents();
            }
        });

        assertPeriodWithDishes(R.string.breakfast, "Scrambled Eggs", "Bacon");
        assertPeriodWithDishes(R.string.lunch, "Pig", "Olive Oil");
        assertPeriodWithDishes(R.string.dinner, "Pasta", "Chicken");
    }

    private void assertPeriodWithDishes(int periodId, final String...dishes) {
        onView(allOf(withText(periodId), isDisplayed())).perform(click());

        for(String dish : dishes) {
            onView(withText(dish)).check(matches(isDisplayed()));
        }
    }
}
