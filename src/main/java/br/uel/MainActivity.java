package br.uel;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import br.uel.dao.MealDao;
import br.uel.model.Meal;
import com.google.inject.Inject;
import com.google.inject.Key;
import roboguice.RoboGuice;
import roboguice.util.RoboContext;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends FragmentActivity implements RoboContext, ActionBar.TabListener {

    @Inject
    private MealDao mealDao;

    private ViewPager viewPager;

    private FragmentPagerAdapter mealsPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RoboGuice.getInjector(this).injectMembers(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Meal> meals = mealDao.mealsOfTheWeek(Calendar.getInstance());

        mealsPagerAdapter = getFragmentFromAdapter(meals);

        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mealsPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mealsPagerAdapter.getCount(); i++) {
            ActionBar.Tab tab = actionBar.newTab().setText(mealsPagerAdapter.getPageTitle(i)).setTabListener(this);
            actionBar.addTab(tab);
        }
    }

    private FragmentPagerAdapter getFragmentFromAdapter(List<Meal> meals) {

        if(meals.size() > 0)
            return new MealsPagerAdapter(getSupportFragmentManager(), meals);
        else
            return new MissingMealAdapter(getSupportFragmentManager());
    }

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return new HashMap<>();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}

