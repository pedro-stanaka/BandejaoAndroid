package br.uel.easymenu;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.uel.easymenu.gui.MenuActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestMenuRefresh {

    private static final String ONE_MEAL_FILE= "1-dish.json";
    private static final String TWO_MEAL_FILE= "2-dishes.json";

    @Rule
    public ActivityTestRule<MenuActivity> activityRule =
            new ActivityTestRule<>(MenuActivity.class, true, false);

    @Rule
    public TestAppRule appRule = new TestAppRule();

    @Before
    public void testRefreshNewMeals() throws Exception {
        String response = JsonUtils.convertJsonToString(ONE_MEAL_FILE);
        appRule.enqueueRequest(response);
        activityRule.launchActivity(new Intent());
    }

    @Test
    public void menuIsUpdatedAfterRefreshMeals() throws Exception {
        String response = JsonUtils.convertJsonToString(TWO_MEAL_FILE);
        appRule.enqueueRequest(response);
        onView(withId(R.id.refresh_meals)).perform(click());
        onView(withText("Beans")).check(matches(isDisplayed()));
    }
}
