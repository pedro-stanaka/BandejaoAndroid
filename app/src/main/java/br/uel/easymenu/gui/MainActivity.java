package br.uel.easymenu.gui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Inject;

import br.uel.easymenu.App;
import br.uel.easymenu.R;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.gcm.RegistrationIntentService;
import br.uel.easymenu.scheduler.DailyListener;
import br.uel.easymenu.service.NetworkService;

public class MainActivity extends Activity {

    private final static String MENU_WTIH_MEALS = "withoutMeals";
    private final static String FIRST_RUN_ALARM = "firstRunAlarm";

    @Inject
    NetworkService networkService;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    MealDao mealDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.component().inject(this);

        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

        setupPollerAlarm();
        setupGcm();

    }

    private void setupPollerAlarm() {
        boolean firstRunAlarm = sharedPreferences.getBoolean(FIRST_RUN_ALARM, false);
        if (!firstRunAlarm) {
            WakefulIntentService.scheduleAlarms(new DailyListener(), this, false);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(FIRST_RUN_ALARM, true);
            editor.apply();
        }
    }

    private void setupGcm() {
        if (checkPlayServices()) {
            Intent intentGcm = new Intent(this, RegistrationIntentService.class);
            startService(intentGcm);
        } else {
            Log.e(App.TAG, "No valid Google Play Services APK found");
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.i(App.TAG, getResources().getString(R.string.playservices_not_supported));
            finish();
            return false;
        }
        return true;
    }
}
