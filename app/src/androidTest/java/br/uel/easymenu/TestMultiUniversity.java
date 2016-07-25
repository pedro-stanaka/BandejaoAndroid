package br.uel.easymenu;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.uel.easymenu.gui.MenuActivity;
import br.uel.easymenu.utils.JsonUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TestMultiUniversity {

    @Rule
    public ActivityTestRule<MenuActivity> activityRule =
            new ActivityTestRule<>(MenuActivity.class, true, false);

    @Rule
    public TestAppRule appRule = new TestAppRule();

    @Before
    public void setupTest() throws Exception {
        String response = JsonUtils.convertJsonToString("multi-university.json");
        appRule.enqueueRequest(response);
        activityRule.launchActivity(new Intent());
    }

    @Test
    public void showsCampusTitle() throws Exception {

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withText(R.string.campus)).check(matches(isDisplayed()));
        onView(withText("PU")).check(matches(isDisplayed()));
        onView(withText("DU")).check(matches(isDisplayed()));
    }
}
