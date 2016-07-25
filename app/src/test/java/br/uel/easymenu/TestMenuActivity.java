package br.uel.easymenu;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jayway.awaitility.Awaitility;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.gui.MenuActivity;
import br.uel.easymenu.ioc.AppComponent;
import br.uel.easymenu.ioc.DaggerTestApp_MockHttpComponent;
import br.uel.easymenu.ioc.MockHttpModule;
import br.uel.easymenu.ioc.TestApp;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.service.NetworkEvent;
import br.uel.easymenu.tables.DbHelper;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestMenuActivity {

    private MenuActivity menuActivity;

    @Inject
    MealDao mealDao;

    private MockWebServer webServer;

    @Before
    public void setupTests() throws Exception {
        DbHelper.resetConnection();
        TestApp.component().inject(this);

        webServer = new MockWebServer();
        webServer.start();

        AppComponent component = DaggerTestApp_MockHttpComponent
                .builder()
                .appModule(new MockHttpModule(RuntimeEnvironment.application, webServer))
                .build();
        ((App) RuntimeEnvironment.application).setComponent(component);
    }

    public void closeDatabase() {
        DbHelper.getInstance(RuntimeEnvironment.application).getWritableDatabase().close();
    }

    @Test
    public void updatedMealsRefreshTheUI() throws Exception {
        buildActivityWithJsonResponse("3-days.json");

        TabLayout layout = (TabLayout) menuActivity.findViewById(R.id.tabs);
        assertTrue(layout != null);
        assertEquals(layout.getTabCount(), 3);
    }

    @Test
    public void viewPagerDisplaysTheRightDishes() throws Exception {
        buildActivityWithJsonResponse("3-days.json");
        View view = getViewFromViewPager(0);

        assertTrue(viewContainsText(view, "Burger"));
        assertTrue(viewContainsText(view, "Rice"));
        assertTrue(viewContainsText(view, "Pizza"));
        assertTrue(viewContainsText(view, "Beans"));
    }

    @Test
    public void viewPagerDisplaysTheRightPeriods() throws Exception {
        buildActivityWithJsonResponse("3-days.json");
        View view = getViewFromViewPager(1);

        String breakfast = getStringFromResources(R.string.breakfast);
        String lunch = getStringFromResources(R.string.lunch);
        String both = getStringFromResources(R.string.both);

        assertTrue(viewContainsText(view, breakfast));
        assertTrue(viewContainsText(view, lunch));
        assertTrue(viewContainsText(view, both));
    }

    @Test
    public void dontDisplayTitleWhenOnlyMealPeriodIsBoth() throws Exception {
        buildActivityWithJsonResponse("1-day-both.json");

        String both = getStringFromResources(R.string.both);
        View view = getViewFromViewPager(0);
        assertTrue(viewContainsText(view, "Rice"));
        assertTrue(viewContainsText(view, "Beans"));
        assertFalse(viewContainsText(view, both));
    }

    @Test
    public void twoUpdatesDontMessTheUI() throws Exception {
        buildActivityWithJsonResponse("3-days.json");

        List<Meal> meals = MealBuilder.createFakeMeals(CalendarFactory.mondayPlusDays(4));
        mealDao.insert(meals);
        updateUI();

        TabLayout layout = (TabLayout) menuActivity.findViewById(R.id.tabs);
        assertEquals(layout.getTabCount(), 4);
    }

    @Test
    public void errorEventWithoutShowsGenericToast() {
        webServer.enqueue(new MockResponse().setResponseCode(500).setBody("Server error"));
        menuActivity = Robolectric.buildActivity(MenuActivity.class).create().get();
        Awaitility.await().atMost(2, TimeUnit.SECONDS).until(hasErrorOccurred());

        String networkErrorMessage = menuActivity.getResources().getString(R.string.server_error);
        assertEquals(ShadowToast.getTextOfLatestToast(), networkErrorMessage);
    }

    @Test
    public void emptyMealsDoesNotShowAnything() {
        menuActivity = Robolectric.buildActivity(MenuActivity.class).create().get();
        TabLayout tabs = (TabLayout) menuActivity.findViewById(R.id.tabs);
        TabLayout.Tab singleTab = tabs.getTabAt(0);

        String menu = menuActivity.getResources().getString(R.string.nonexistent_meals_title);
        assertEquals(singleTab.getText().toString(), menu);
    }

    @Test
    public void displayMessageWhenMealHasEmptyDishes() throws Exception {
        buildActivityWithJsonResponse("meal-without-dish.json");

        String emptyDishes = getStringFromResources(R.string.empty_dishes);
        String lunch = getStringFromResources(R.string.lunch);

        View view = getViewFromViewPager(0);
        assertTrue(viewContainsText(view, emptyDishes));
        assertTrue(viewContainsText(view, lunch));
    }

    // TODO: This should be designed better when the server reports the error
    @Ignore
    @Test
    public void errorEventWithMessageShouldDisplayMessage() throws Exception {
        String mockText = "MOCK";

        NetworkEvent.NetworkErrorType errorEvent = NetworkEvent.NetworkErrorType.SERVER_ERROR;
        NetworkEvent event = new NetworkEvent(errorEvent, mockText);
        menuActivity.updatedMeals(event);

        assertEquals(ShadowToast.getTextOfLatestToast(), mockText);
    }

    private String getStringFromResources(int resourceId) {
        return this.menuActivity.getResources().getString(resourceId);
    }

    private View getViewFromViewPager(int index) {
        ViewPager viewPager = (ViewPager) menuActivity.findViewById(R.id.viewpager);
        PagerAdapter adapter = viewPager.getAdapter();
        Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, index);
        SupportFragmentTestUtil.startFragment(fragment, MenuActivity.class);
        return fragment.getView();
    }

    private boolean viewContainsText(View view, String testString) {
        if (view instanceof TextView) {
            String txtViewString = ((TextView) view).getText().toString();
            if (testString.equals(txtViewString)) {
                return true;
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                boolean found = viewContainsText(childView, testString);
                if (found) return true;
            }
        }
        return false;
    }

    private void updateUI() {
        NetworkEvent.Type successEvent = NetworkEvent.Type.SUCCESS;
        menuActivity.updatedMeals(new NetworkEvent(successEvent));
    }

    private void buildActivityWithJsonResponse(String jsonFile) throws Exception {
        String jsonResponse = JsonUtils.convertJsonToString(jsonFile);
        webServer.enqueue(new MockResponse().setBody(jsonResponse));
        menuActivity = Robolectric.buildActivity(MenuActivity.class).create().get();
        Awaitility.await().atMost(2, TimeUnit.SECONDS).until(hasMealsPersisted());
    }

    private Callable<Boolean> hasMealsPersisted() {
        return new Callable<Boolean>() {
            @Override public Boolean call() throws Exception {
                return mealDao.count() > 0;
            }
        };
    }

    private Callable<Boolean> hasErrorOccurred() {
        return new Callable<Boolean>() {
            @Override public Boolean call() throws Exception {
                return ShadowToast.getTextOfLatestToast() != null;
            }
        };
    }
}
