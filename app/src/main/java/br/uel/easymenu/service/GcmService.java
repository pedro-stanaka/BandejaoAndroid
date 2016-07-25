package br.uel.easymenu.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import br.uel.easymenu.App;
import br.uel.easymenu.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.inject.Inject;
import roboguice.inject.InjectResource;

import java.io.IOException;

/**
 * Implementation based in the implementation of <a href="http://developer.android.com/google/gcm/client.html">
 *
 */
public class GcmService {

    @Inject
    private GoogleCloudMessaging gcm;

    @Inject
    private SharedPreferences sharedPreferences;

    @Inject
    private Context context;

    @InjectResource(R.string.sender_id)
    private String senderId;

    private final static String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "app_version";

    public String getRegistrationId() {

        String registrationId = sharedPreferences.getString(PROPERTY_REG_ID, null);

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(PROPERTY_REG_ID);
        edit.commit();

        if (registrationId == null || isAppUpdated()) {
            return null;
        }

        return registrationId;
    }

    private boolean isAppUpdated() {
        int registeredVersion = sharedPreferences.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        return (registeredVersion != currentVersion);
    }

    private int getAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void registerInBackground() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String regid = gcm.register(senderId);
                    sendRegistrationIdToBackend(regid);
                    storeRegistrationId(regid);
                } catch (IOException ex) {
//                    SERVICE_NOT_AVAILABLE
                    Log.e(App.TAG, ex.getMessage());
                }
                return null;
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend(String regid) {
//        networkService.sendRegistrationId();
    }

    /**
     * Store the registration id and the app version in the sharedPreferences
     *
     */
    private void storeRegistrationId(String regid) {
        int appVersion = getAppVersion();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROPERTY_REG_ID, regid);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
}
