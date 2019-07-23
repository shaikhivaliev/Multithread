package com.multithread.task.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.multithread.task.NotificationProgress;
import com.multithread.task.MainActivity;

import java.util.concurrent.TimeUnit;

public class ServiceLoader extends IntentService {
    public static final String TAG = ServiceLoader.class.getSimpleName();
    private NotificationProgress notification;
    private Messenger messengerClient, messengerService;

    private class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            messengerClient = msg.replyTo;
        }
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public ServiceLoader() {
        super("ServiceLoader");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        messengerService = new Messenger(new IncomingHandler());
        notification = new NotificationProgress(this);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messengerService.getBinder();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: start");
        notification.showNotificationLoading();
        Message messageLoading = Message.obtain(null, ActivityService.MSG_LOADING);
        try {
            messengerClient.send(messageLoading);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onHandleIntent: wait");
        try {
            TimeUnit.MILLISECONDS.sleep(MainActivity.time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        notification.showNotificationReady();
        Log.d(TAG, "onHandleIntent: finish");
        Message messageReady = Message.obtain(null, ActivityService.MSG_READY);
        try {
            messengerClient.send(messageReady);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        stopSelf();
        notification.cancel();
    }
}
