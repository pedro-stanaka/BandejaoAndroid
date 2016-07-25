package br.uel.easymenu;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.uel.easymenu.gui.MenuActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestSingleUniversity {

    @Rule
    public ActivityTestRule<MenuActivity> activityRule =
            new ActivityTestRule<>(MenuActivity.class, true, false);

    @Rule
    public TestAppRule appRule = new TestAppRule();

    @Test(expected = NoMatchingViewException.class)
    public void TitleDoesNotShowInDrawer() throws Exception {
        String successString = JsonUtils.convertJsonToString("single-university.json");
        appRule.enqueueRequest(successString);
        activityRule.launchActivity(new Intent());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withText(R.string.campus)).check(matches(isDisplayed()));
    }
}
