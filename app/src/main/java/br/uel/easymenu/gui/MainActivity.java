package br.uel.easymenu.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.inject.Inject;

import br.uel.easymenu.App;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.gcm.RegistrationIntentService;
import br.uel.easymenu.scheduler.DailyListener;
import br.uel.easymenu.service.NetworkEvent;
import br.uel.easymenu.service.NetworkService;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;

public class MainActivity extends RoboActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private final static String MENU_WTIH_MEALS = "withoutMeals";
    private final static String FIRST_RUN_ALARM = "firstRunAlarm";

    @Inject
    private NetworkService networkService;

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
        if (!firstRunAlarm) {
            WakefulIntentService.scheduleAlarms(new DailyListener(), this, false);

            setKeyPreferenceToTrue(FIRST_RUN_ALARM);
        }

        boolean firstRunNetwork = sharedPreferences.getBoolean(MENU_WTIH_MEALS, false);
        if (!firstRunNetwork) {
            setupNewMeals();
        }

        if (checkPlayServices()) {
            Intent intentGcm = new Intent(this, RegistrationIntentService.class);
            startService(intentGcm);
        } else {
            Log.e(App.TAG, "No valid Google Play Services APK found");
        }
    }

    private void setupNewMeals() {
        networkService.persistCurrentMealsFromServer(new NetworkService.NetworkServiceListener() {
            @Override
            public void onSuccess() {
                setKeyPreferenceToTrue(MENU_WTIH_MEALS);
            }

            @Override
            public void onError(NetworkEvent.NetworkErrorType errorMessage) {
            }
        });
    }

    private void setKeyPreferenceToTrue(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, true);
        editor.apply();
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
