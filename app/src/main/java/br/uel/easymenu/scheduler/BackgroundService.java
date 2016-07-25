package br.uel.easymenu.scheduler;

import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import br.uel.easymenu.service.NetworkService;

public class BackgroundService extends WakefulIntentService {

    public BackgroundService() {
        super("BackgroundService");
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        NetworkService service = new NetworkService();
        service.persistCurrentMealsFromServer();

    }
}
