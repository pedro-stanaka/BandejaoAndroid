package br.uel.easymenu.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import javax.inject.Inject;

import br.uel.easymenu.App;
import br.uel.easymenu.service.UniversityService;

public class AppGcmListenerService extends GcmListenerService {

    @Inject
    UniversityService universityService;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        ((App) getApplicationContext()).component().inject(this);

        String message = data.getString("message");
        Log.d(App.TAG, "From: " + from);
        Log.d(App.TAG, "Message: " + message);

        universityService.syncUniversitiesWithServer();
    }
}
