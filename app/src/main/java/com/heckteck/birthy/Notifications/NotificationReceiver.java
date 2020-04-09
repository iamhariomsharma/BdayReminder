package com.heckteck.birthy.Notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.heckteck.birthy.Activities.MainActivity;
import com.heckteck.birthy.Activities.NotifyActivity;
import com.heckteck.birthy.R;

import java.util.Date;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    private NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.notificationManager = NotificationManagerCompat.from(context);
        String title = intent.getStringExtra("TITLE");
        String msg = intent.getStringExtra("MSG");
        String phone = intent.getStringExtra("PHONE");
        int i = (int) ((new Date()).getTime() / 1000L % 2147483647L);
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.putExtra("SEND", "sendWishesNotification");
        mainIntent.putExtra("PHONE", phone.toString());
        mainIntent.putExtra("ID", String.valueOf(i));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) (System.currentTimeMillis() & 0xFFFFFFFL), mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = (new NotificationCompat.Builder(context, "Birthdays"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory("msg")
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, "Send Wishes", pendingIntent)
                .build();
        notificationManager.notify(i, notification);
        Intent intent1 = new Intent(context, NotifyActivity.class);
        intent1.putExtra("NAME", title.toString());
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
