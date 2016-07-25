package br.uel.easymenu.gui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import br.uel.easymenu.adapter.MealsPagerAdapter;
import br.uel.easymenu.adapter.MissingMealAdapter;
import br.uel.easymenu.R;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.service.NetworkService;
import com.google.inject.Inject;
import com.google.inject.Key;
import roboguice.RoboGuice;
import roboguice.util.RoboContext;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends ActionBarActivity implements RoboContext, ActionBar.TabListener {

    @Inject
    private MealDao mealDao;

    @Inject
    private NetworkService networkService;

    @Inject
    private SharedPreferences sharedPreferences;

    private final static String MENU_WITHOUT_MEALS = "without_meals";

    private ActionBar actionBar;

    private FragmentStatePagerAdapter mealsPagerAdapter;

    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RoboGuice.getInjector(this).injectMembers(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        if(menuWithMeals())
            processNewMeals();

        setGuiWithMeals();
    }

    private boolean menuWithMeals() {
        return !(sharedPreferences.getBoolean(MENU_WITHOUT_MEALS, false));
    }

    private void setGuiWithMeals() {
        List<Meal> meals = mealDao.mealsOfTheWeek(Calendar.getInstance());
        mealsPagerAdapter = getFragmentFromAdapter(meals);
        processViewPager();
        processActionBar();
    }

    private FragmentStatePagerAdapter getFragmentFromAdapter(List<Meal> meals) {
        if (meals.size() > 0)
            return new MealsPagerAdapter(getSupportFragmentManager(), meals);
        else
            return new MissingMealAdapter(getSupportFragmentManager());
    }

    private void processViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mealsPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
    }

    private void processActionBar() {
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (int i = 0; i < mealsPagerAdapter.getCount(); i++) {
            ActionBar.Tab tab = actionBar.newTab().setText(mealsPagerAdapter.getPageTitle(i)).setTabListener(this);
            actionBar.addTab(tab);
        }
    }

    private void processNewMeals() {
        networkService.persistCurrentMealsFromServer(new NetworkService.NetworkServiceListener() {
            @Override
            public void onSuccess() {
//                There are the first meals in the database
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(MENU_WITHOUT_MEALS, true);
                editor.commit();

                actionBar.removeAllTabs();
                setGuiWithMeals();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MenuActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return new HashMap<>();
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}

