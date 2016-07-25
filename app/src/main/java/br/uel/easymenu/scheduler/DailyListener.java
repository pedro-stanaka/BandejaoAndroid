package br.uel.easymenu.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import java.util.Calendar;

import br.uel.easymenu.App;

public class DailyListener implements WakefulIntentService.AlarmListener {

    @Override
    public void scheduleAlarms(AlarmManager alarmManager, PendingIntent pendingIntent, Context context) {

        // register when enabled in preferences
        Log.i(App.TAG, "Schedule update check...");

        // every day at 10 am
        Calendar calendar = Calendar.getInstance();
        // if it's after or equal 10 am schedule for next day
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 10) {
            calendar.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
        }
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void sendWakefulWork(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        // only when connected or while connecting...
        if (netInfo != null && netInfo.isConnectedOrConnecting() &&
                netInfo.getType() == ConnectivityManager.TYPE_WIFI) {

            Log.d(App.TAG, "We have internet, start update check directly now!");

            Intent backgroundIntent = new Intent(context, BackgroundService.class);
            WakefulIntentService.sendWakefulWork(context, backgroundIntent);
        } else {
            ConnectivityReceiver.enableReceiver(context);
        }
    }

    @Override
    public long getMaxAge() {
        return (AlarmManager.INTERVAL_DAY + 60 * 1000);
    }
}
