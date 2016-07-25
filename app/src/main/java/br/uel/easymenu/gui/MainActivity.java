package br.uel.easymenu.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.uel.easymenu.App;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.gcm.RegistrationIntentService;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.scheduler.DailyListener;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;

public class MainActivity extends RoboActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private final static String FIRST_RUN_ALARM = "firstRunAlarm";

    @Inject
    private SharedPreferences sharedPreferences;

    @Inject
    private MealDao mealDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RoboGuice.getInjector(this).injectMembers(this);

        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

        boolean firstRunAlarm = sharedPreferences.getBoolean(FIRST_RUN_ALARM, false);

        if(!firstRunAlarm) {
            WakefulIntentService.scheduleAlarms(new DailyListener(), this, false);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(FIRST_RUN_ALARM, true);
            editor.apply();
        }

        if (checkPlayServices()) {
            Intent intentGcm = new Intent(this, RegistrationIntentService.class);
            startService(intentGcm);
        } else {
            Log.e(App.TAG, "No valid Google Play Services APK found");
        }

        // Please, remove this
        List<Dish> dishes = new ArrayList<Dish>() {{
           add(new Dish("Rice"));
           add(new Dish("Rice"));
        }};

        List<Dish> dishes2 = new ArrayList<Dish>() {{
           add(new Dish("Beans"));
           add(new Dish("Beans"));
        }};

        Meal meal = new Meal(Calendar.getInstance(), Meal.LUNCH, dishes);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH, 1);
        Meal meal2 = new Meal(calendar2, Meal.LUNCH, dishes2);

        mealDao.insert(meal);
        mealDao.insert(meal2);
        finish();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(App.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
