package br.uel.easymenu.scheduler;

import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import javax.inject.Inject;

import br.uel.easymenu.App;
import br.uel.easymenu.service.UniversityService;

import static br.uel.easymenu.utils.CalendarUtils.today;

public class BackgroundService extends WakefulIntentService {

    @Inject
    UniversityService universityService;

    public BackgroundService() {
        super("BackgroundService");
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        Log.d(App.TAG, "Alarm triggered in " + today());
        ((App) getApplicationContext()).component().inject(this);
        universityService.syncUniversitiesWithServer();
    }
}
