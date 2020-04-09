package com.heckteck.birthy.Notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationHelper extends Application {

    public static final String CHANNEL_1_ID = "Birthdays";

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("Birthdays",
                    "Birthday Reminders",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Birthday Reminders Channel");
            notificationChannel.enableVibration(true);
            getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }
}
