package br.uel.easymenu.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import br.uel.easymenu.App;

public class AppGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(App.TAG, "From: " + from);
        Log.d(App.TAG, "Message: " + message);

        // TODO: Update the universities
    }
}
