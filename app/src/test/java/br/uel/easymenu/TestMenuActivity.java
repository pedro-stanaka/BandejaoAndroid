package br.uel.easymenu;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.List;

import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.gui.MenuActivity;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.service.NetworkEvent;
import br.uel.easymenu.tables.DbHelper;
import roboguice.RoboGuice;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestMenuActivity {

    private MenuActivity menuActivity;

    @Inject
    private MealDao mealDao;

    @Before
    public void setupTests() {
        menuActivity = Robolectric.buildActivity(MenuActivity.class).create().get();
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
    }

    @After
    public void closeDatabase() {
        DbHelper helper = DbHelper.getInstance(RuntimeEnvironment.application);
        helper.close();
    }

    @Test
    public void emptyMealsDoesNotShowAnything() {
        TabLayout tabs = (TabLayout) menuActivity.findViewById(R.id.tabs);
        TabLayout.Tab singleTab = tabs.getTabAt(0);

        String menu = menuActivity.getResources().getString(R.string.nonexistent_meals_title);
        assertEquals(singleTab.getText().toString(), menu);
    }

    @Test
    public void updatedMealsRefreshTheUI() throws Exception {
        createFakeMealsAndUpdateUI();
        TabLayout layout = (TabLayout) menuActivity.findViewById(R.id.tabs);
        assertTrue(layout != null);
        assertEquals(layout.getTabCount(), 3);
    }

    @Test
    public void errorEventWithoutShowsGenericToast() {
        NetworkEvent.NetworkErrorType errorEvent = NetworkEvent.NetworkErrorType.SERVER_ERROR;
        menuActivity.updatedMeals(new NetworkEvent(errorEvent));

        String networkErrorMessage = menuActivity.getResources().getString(R.string.server_error);
        assertEquals(ShadowToast.getTextOfLatestToast(), networkErrorMessage);
    }

    @Test
    public void errorEventWithMessageShouldDisplayMessage() throws Exception {
        String mockText = "MOCK";

        NetworkEvent.NetworkErrorType errorEvent = NetworkEvent.NetworkErrorType.SERVER_ERROR;
        NetworkEvent event = new NetworkEvent(errorEvent, mockText);
        menuActivity.updatedMeals(event);

        assertEquals(ShadowToast.getTextOfLatestToast(), mockText);
    }

    @Test
    public void viewPagerDisplaysTheRightDishes() {
        createFakeMealsAndUpdateUI();
        View view = getViewFromViewPager(0);

        assertTrue(viewContainsText(view, "Burger"));
        assertTrue(viewContainsText(view, "Rice"));
        assertTrue(viewContainsText(view, "Pizza"));
        assertTrue(viewContainsText(view, "Beans"));
    }

    @Test
    public void viewPagerDisplaysTheRightPeriods() {
        createFakeMealsAndUpdateUI();
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
        MealBuilder builder = new MealBuilder();
        Meal meal = builder.withPeriod(Meal.BOTH).withDishes("Rice", "Beans").build();
        mealDao.insert(meal);

        this.updateUI();

        String both = getStringFromResources(R.string.both);
        View view = getViewFromViewPager(0);
        assertTrue(viewContainsText(view, "Rice"));
        assertTrue(viewContainsText(view, "Beans"));
        assertFalse(viewContainsText(view, both));
    }

    @Test
    public void twoUpdatesDontMessTheUI() {
        createFakeMealsAndUpdateUI();
        TabLayout layout = (TabLayout) menuActivity.findViewById(R.id.tabs);
        updateUI();

        List<Meal> meals = MealBuilder.createFakeMeals(CalendarFactory.mondayPlusDays(4));
        mealDao.insert(meals);
        updateUI();

        assertEquals(layout.getTabCount(), 4);
    }

    @Test
    public void displayMessageWhenMealHasEmptyDishes() {
        Meal meal = new MealBuilder().withPeriod(Meal.LUNCH).build();
        mealDao.insert(meal);
        updateUI();

        String emptyDishes = getStringFromResources(R.string.empty_dishes);
        String lunch = getStringFromResources(R.string.lunch);

        View view = getViewFromViewPager(0);
        assertTrue(viewContainsText(view, emptyDishes));
        assertTrue(viewContainsText(view, lunch));
    }

    private String getStringFromResources(int resourceId) {
        return this.menuActivity.getResources().getString(resourceId);
    }

    private View getViewFromViewPager(int index) {
        ViewPager viewPager = (ViewPager) menuActivity.findViewById(R.id.viewpager);
        PagerAdapter adapter = viewPager.getAdapter();
        Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, 0);
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

    private void createFakeMealsAndUpdateUI() {
        List<Meal> meals = MealBuilder.createFakeMeals(
                CalendarFactory.monday(),
                CalendarFactory.mondayPlusDays(1),
                CalendarFactory.mondayPlusDays(2));
        mealDao.insert(meals);

        updateUI();
    }

    private void updateUI() {
        NetworkEvent.Type successEvent = NetworkEvent.Type.SUCCESS;
        menuActivity.updatedMeals(new NetworkEvent(successEvent));
    }
}
