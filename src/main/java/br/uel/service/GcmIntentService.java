package br.uel.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.uel.App;
import br.uel.R;
import br.uel.gui.MenuActivity;
import br.uel.model.Meal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.inject.Inject;

import java.util.List;

public class GcmIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    @Inject
    private ObjectMapper mapper;

    @Inject
    private MealService mealService;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if(!extras.isEmpty()) {

            if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e(App.TAG, "Error: " + messageType);
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(App.TAG, "Received message: " + extras.toString());
                persistNewMeals(extras.toString());
                sendNotification();
            }
        }
    }

    private void persistNewMeals(String json) {
        List<Meal> meals = mealService.deserializeMeal(json);
        mealService.persistMeals(meals);
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MenuActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("OLA"))
                        .setContentText("OLA");

        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
