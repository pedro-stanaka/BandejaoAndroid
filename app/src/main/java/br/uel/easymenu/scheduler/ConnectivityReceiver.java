package br.uel.easymenu.scheduler;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static void enableReceiver(Context context) {
        ComponentName component = new ComponentName(context, ConnectivityReceiver.class);

        context.getPackageManager().setComponentEnabledSetting(component,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public static void disableReceiver(Context context) {
        ComponentName component = new ComponentName(context, ConnectivityReceiver.class);

        context.getPackageManager().setComponentEnabledSetting(component,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.d("ConnectivityReceiver", "ConnectivityReceiver invoked...");

            Log.d("ConnectivityReceiver", "Update check daily is enabled!");

            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if (!noConnectivity) {

                ConnectivityManager cm = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                // only when connected or while connecting...
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        Log.d("ConnectivityReceiver", "We have internet, start update check and disable receiver!");

                        Intent backgroundIntent = new Intent(context, BackgroundService.class);
                        WakefulIntentService.sendWakefulWork(context, backgroundIntent);

                        // disable receiver after we started the service
                        disableReceiver(context);
                    }
                }
            }
        }
    }
}
