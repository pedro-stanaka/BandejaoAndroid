package br.uel.easymenu.gui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import javax.inject.Inject;

import br.uel.easymenu.App;
import br.uel.easymenu.R;
import br.uel.easymenu.adapter.MealsPagerAdapter;
import br.uel.easymenu.adapter.MissingMealAdapter;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.service.NetworkEvent;
import br.uel.easymenu.service.NetworkService;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity {

    @Inject
    MealDao mealDao;

    @Inject
    NetworkService networkService;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    EventBus bus;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.component().inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setGuiWithMeals();

        bus.register(this);
        // TODO: Check if it has Internet
        networkService.persistCurrentMealsFromServer();
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void updatedMeals(NetworkEvent event) {
        String message;
        if (!(event.hasMessage()) && event.getEventType() == NetworkEvent.Type.ERROR) {
            message = getResources().getString(event.getError().resourceId);
        } else if (event.getEventType() == NetworkEvent.Type.SUCCESS) {
            message = getResources().getString(R.string.new_meals);
            setGuiWithMeals();
        } else {
            message = event.getMessage();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setGuiWithMeals() {
        GroupedMeals groupedMeals = mealDao.mealsOfTheWeekGroupedByDay(Calendar.getInstance());

        FragmentStatePagerAdapter mealsPagerAdapter = (groupedMeals.size() > 0) ?
                new MealsPagerAdapter(getSupportFragmentManager(), groupedMeals) :
                new MissingMealAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(mealsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Setting today's tab
        if (groupedMeals.hasDate(Calendar.getInstance())) {
            int index = groupedMeals.getPositionByDay(Calendar.getInstance());
            TabLayout.Tab tab = tabLayout.getTabAt(index);
            if (tab != null) {
                tab.select();
            }
        }
    }
}

