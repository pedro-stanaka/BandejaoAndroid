package br.uel.easymenu;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import br.uel.easymenu.gui.MenuActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

public class TestErrorMenuActivity {

    @Rule
    public ActivityTestRule<MenuActivity> activityRule =
            new ActivityTestRule<>(MenuActivity.class, true, false);

    @Rule
    public TestAppRule appRule = new TestAppRule();

    @Test
    public void showsToastOnError() throws Exception {
        appRule.enqueueRequest(500, "Server Error");
        activityRule.launchActivity(new Intent());

        onView(withText(R.string.server_error))
                .inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    // TODO: Show custom error from the server
    @Test
    public void showsCustomErrorFromServer() throws Exception {
    }
}
