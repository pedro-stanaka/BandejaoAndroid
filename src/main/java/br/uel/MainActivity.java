package br.uel;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import br.uel.dao.MealDao;
import br.uel.model.Dish;
import br.uel.model.Meal;
import com.google.inject.Inject;
import com.google.inject.Key;
import roboguice.RoboGuice;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
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

    private MealsPagerAdapter mealsPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RoboGuice.getInjector(this).injectMembers(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Meal> meals = mealDao.mealsOfTheWeek(Calendar.getInstance());

        Meal meal1 = new Meal();
        meal1.setId(1);
        meal1.setDate(Calendar.getInstance());
        meal1.addDish(new Dish("Arroz"));
        meal1.addDish(new Dish("Feijão"));

        Meal meal2 = new Meal();
        meal2.setId(2);
        meal2.setDate(Calendar.getInstance());
        meal2.addDish(new Dish("Macarrão"));
        meal2.addDish(new Dish("Carne de Frango"));

        meals.add(meal1);
        meals.add(meal2);

        mealsPagerAdapter = new MealsPagerAdapter(getSupportFragmentManager(), meals);

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
            actionBar.addTab(actionBar.newTab().setText(mealsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(br.uel.R.menu.main, menu);
        return true;
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

