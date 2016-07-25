package br.uel.easymenu.gui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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
import br.uel.easymenu.dao.UniversityDao;
import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.model.University;
import br.uel.easymenu.service.NetworkEvent;
import br.uel.easymenu.service.UniversityService;
import butterknife.Bind;
import butterknife.ButterKnife;

import static br.uel.easymenu.utils.CalendarUtils.today;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String UNIVERSITY_NAME = "university_name";

    @Inject
    MealDao mealDao;

    @Inject
    UniversityDao universityDao;

    @Inject
    UniversityService universityService;

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

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    private University currentUniversity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App) getApplicationContext()).component().inject(this);
        ButterKnife.bind(this);

        String universityName = sharedPreferences.getString(UNIVERSITY_NAME, null);
        if(universityName != null) {
            currentUniversity = universityDao.findByName(universityName);
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        bus.register(this);

        // TODO: Check if it has Internet
        universityService.syncUniversitiesWithServer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGuiWithMeals();
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
        currentUniversity = universityService.selectUniversity(currentUniversity);
        setUniversityMenu();
        setTitleActionBar();

        // currentUniversity may be null, but we don't care
        GroupedMeals groupedMeals = mealDao.mealsOfTheWeekGroupedByDay(today(), currentUniversity);

        FragmentStatePagerAdapter mealsPagerAdapter = (groupedMeals.size() > 0) ?
                new MealsPagerAdapter(getSupportFragmentManager(), groupedMeals) :
                new MissingMealAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(mealsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Setting today's tab
        if (groupedMeals.hasDate(today())) {
            int index = groupedMeals.getPositionByDay(today());
            TabLayout.Tab tab = tabLayout.getTabAt(index);
            if (tab != null) {
                tab.select();
            }
        }
    }

    private void setTitleActionBar() {
        String title;
        if (currentUniversity == null) {
            title = getString(R.string.title_without_university);
        }
        else {
            String rawTitle =  getString(R.string.title);
            title = String.format(rawTitle, currentUniversity.getName());
        }
        getSupportActionBar().setTitle(title);
    }

    private void setUniversityMenu() {
        Menu menu = navigationView.getMenu();

        // Set university menu
        boolean showCampusMenu = universityDao.count() > 1;
        menu.findItem(R.id.campus).setVisible(showCampusMenu);

        if(universityDao.count() > 1) {
            SubMenu subMenu = menu.findItem(R.id.campus).getSubMenu();
            if (subMenu == null)
                subMenu = menu.addSubMenu(R.id.campus, Menu.NONE, Menu.NONE, R.string.campus);

            subMenu.clear();

            int count = 0;
            for (University university : universityDao.orderByName()) {
                 MenuItem item = subMenu.add(R.id.campus_names, Menu.NONE, count++, university.getName());

                if(currentUniversity != null && currentUniversity.getName().equals(university.getName())) {
                    item.setChecked(true);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(currentUniversity != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(UNIVERSITY_NAME, currentUniversity.getName());
            editor.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.rate_app) {
            Log.d(App.TAG, "Rate this app <3");
        }
        else if (item.getItemId() == R.id.visit_website) {
            Log.d(App.TAG, "Visit website");
        }
        else if(item.getGroupId() == R.id.campus_names) {
            currentUniversity = universityDao.findByName(item.getTitle()+"");
            setGuiWithMeals();
        }

        drawerLayout.closeDrawers();
        return false;
    }
}

