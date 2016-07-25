package br.uel.easymenu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityUtils {

    public static boolean isConnected(Context context, boolean onlyWifi) {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            if (onlyWifi && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                connected = true;
            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE ||
                    netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                connected = true;
            }
        }
        return connected;
    }
}
