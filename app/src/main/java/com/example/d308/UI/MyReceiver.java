package com.example.d308.UI;
import static android.content.Context.NOTIFICATION_SERVICE;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.d308.R;

public class MyReceiver extends BroadcastReceiver {

    String channel_id = "test";
    static int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        String vacationTitle = intent.getStringExtra("title");
        String alertType = intent.getStringExtra("key");
        String message = vacationTitle + "trip is " + alertType + " today!";

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        createNotificationChannel(context, channel_id);
        Notification n = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(intent.getStringExtra(message))
                .setContentTitle("Vacation Notification").build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, n);
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = context.getResources().getString(R.string.channel_name);
//            String description = context.getString(R.string.channel_description);
            CharSequence name = "Vacation Alerts";
            String description = "Notifications for vacation start and end dates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}