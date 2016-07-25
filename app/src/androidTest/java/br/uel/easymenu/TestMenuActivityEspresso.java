package br.uel.easymenu;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.gui.MenuActivity;
import br.uel.easymenu.ioc.EspressoApp;
import br.uel.easymenu.tables.DbHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestMenuActivityEspresso {

    @Inject
    MealDao mealDao;

    // We don't launch our activity in here because we need to inject the mock http request before
    @Rule
    public ActivityTestRule<MenuActivity> activityRule =
            new ActivityTestRule<>(MenuActivity.class, true, false);

    @Before
    public void setupTests() throws Exception {
        MockWebServer webServer = new MockWebServer();

        App app = (App) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        EspressoApp.EspressoComponent component = EspressoApp.component(app, webServer);
        component.inject(this);
        app.setComponent(component);

        webServer.start();
        String jsonResponse = JsonUtils.convertJsonToString("3-days.json");
        webServer.enqueue(new MockResponse().setBody(jsonResponse));
    }

    @Test
    public void testTabCount() throws Exception {
        activityRule.launchActivity(new Intent());
        onView(withText(R.string.app_name)).check(matches(isDisplayed()));
    }
}
