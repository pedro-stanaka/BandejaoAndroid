package br.uel.easymenu.gui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.Key;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import br.uel.easymenu.R;
import br.uel.easymenu.adapter.MealsPagerAdapter;
import br.uel.easymenu.adapter.MissingMealAdapter;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.service.NetworkEvent;
import br.uel.easymenu.service.NetworkService;
import roboguice.RoboGuice;
import roboguice.util.RoboContext;

public class MenuActivity extends AppCompatActivity implements RoboContext {

    @Inject
    private MealDao mealDao;

    @Inject
    private NetworkService networkService;

    @Inject
    private SharedPreferences sharedPreferences;

    private EventBus bus = EventBus.getDefault();

    // TODO: Use InjectView
    private ViewPager viewPager;

    private Toolbar toolbar;

    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RoboGuice.getInjector(this).injectMembers(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setSupportActionBar(toolbar);
        setGuiWithMeals();

        bus.register(this);
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void updatedMeals(NetworkEvent event) {
        String message;
        if(!(event.hasMessage()) && event.getEventType() == NetworkEvent.Type.ERROR) {
            message = getResources().getString(event.getError().resourceId);
        }
        else if (event.getEventType() == NetworkEvent.Type.SUCCESS){
            message = getResources().getString(R.string.network_sucess);
        }
        else {
            message = event.getMessage();
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setGuiWithMeals() {
        GroupedMeals groupedMeals = mealDao.mealsOfTheWeekGroupedByDay(Calendar.getInstance());

        FragmentStatePagerAdapter mealsPagerAdapter = (groupedMeals.size() > 0) ?
                new MealsPagerAdapter(getSupportFragmentManager(), groupedMeals) :
                new MissingMealAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mealsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Setting today's tab
        if (groupedMeals.hasDay(Calendar.getInstance())) {
            int index = groupedMeals.getPositionByDay(Calendar.getInstance());
            TabLayout.Tab tab = tabLayout.getTabAt(index);
            if (tab != null) {
                tab.select();
            }
        }
    }


    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return new HashMap<>();
    }
}

