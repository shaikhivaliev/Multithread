package com.multithread.task;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.multithread.task3.R;

public class NotificationProgress {
    public static final int ID_NOTIFICATION = 101;
    public static final String ID_CHANNEL = "notification_channel_progress";

    private NotificationManager notificationManager;
    private Notification.Builder notificationBuilder;
    private Context context;

    public NotificationProgress(Context context)
    {
        this.context = context;
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = getNotificationBuilder();
    }

    private Notification.Builder getNotificationBuilder()
    {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if (notificationManager.getNotificationChannel(ID_CHANNEL) == null) {
                NotificationChannel channel = new NotificationChannel(ID_CHANNEL, "notification for task 2", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new Notification.Builder(context, ID_CHANNEL);
        }
        else
            builder = new Notification.Builder(context);
        builder
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setAutoCancel(true);
        return builder;
    }

    public void showNotificationLoading()
    {
        notificationBuilder
                .setContentTitle(context.getResources().getString(R.string.loading))
                .setContentText(null)
                .setProgress(0, 0, true);

        Notification notification = notificationBuilder.build();
        notificationManager.notify(ID_NOTIFICATION, notification);
    }

    public void showNotificationReady()
    {
        notificationBuilder
                .setContentTitle(context.getResources().getString(R.string.ready))
                .setContentText(null)
                .setProgress(0, 0, false);

        Notification notification = notificationBuilder.build();
        notificationManager.notify(ID_NOTIFICATION, notification);
    }

    public void cancel()
    {
        notificationManager.cancel(ID_NOTIFICATION);
    }
}
