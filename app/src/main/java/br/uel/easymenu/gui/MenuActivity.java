package br.uel.easymenu.gui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import br.uel.easymenu.R;
import br.uel.easymenu.adapter.MealsPagerAdapter;
import br.uel.easymenu.adapter.MissingMealAdapter;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.service.NetworkEvent;
import br.uel.easymenu.service.NetworkService;
import roboguice.RoboGuice;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MenuActivity extends RoboAppCompatActivity {

    @Inject
    private MealDao mealDao;

    @Inject
    private NetworkService networkService;

    @Inject
    private SharedPreferences sharedPreferences;

    @Inject
    private EventBus bus;

    @InjectView(R.id.viewpager)
    private ViewPager viewPager;

    @InjectView(R.id.toolbar)
    private Toolbar toolbar;

    @InjectView(R.id.tabs)
    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RoboGuice.getInjector(this).injectMembersWithoutViews(this);
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        setGuiWithMeals();

        bus.register(this);
    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        RoboGuice.getInjector(this).injectViewMembers(this);
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
}

