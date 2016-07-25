package br.uel.easymenu.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import org.joda.time.DateTime;

import br.uel.easymenu.App;

public class DailyListener implements WakefulIntentService.AlarmListener {

    private static final int HOUR_ALARM = 10;

    @Override
    public void scheduleAlarms(AlarmManager alarmManager, PendingIntent pendingIntent, Context context) {

        // register when enabled in preferences
        Log.i(App.TAG, "Schedule update check...");

        DateTime dateTime = DateTime.now();

        if(dateTime.getHourOfDay() >= HOUR_ALARM) {
            dateTime = dateTime.plusDays(1);
        }
        dateTime = dateTime
                .withHourOfDay(HOUR_ALARM)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0);

        alarmManager.setInexactRepeating(AlarmManager.RTC, dateTime.getMillis(),
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
    public long getMaxAge(Context context) {
        return (AlarmManager.INTERVAL_DAY + 60 * 1000);
    }
}
