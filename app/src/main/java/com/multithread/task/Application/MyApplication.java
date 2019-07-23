package com.multithread.task.Application;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.multithread.task.NotificationProgress;
import com.multithread.task.MainActivity;

import java.util.concurrent.TimeUnit;

public final class MyApplication extends Application {
    public static final String TAG =  MyApplication.class.getSimpleName();
    private NotificationProgress notificationProgress;
    private Handler handler;
    private AsyncResponse asyncResponse;

    private boolean isLoading = false;
    private boolean isFinish = false;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        notificationProgress = new NotificationProgress(this);
    }

    public void setAsyncResponse(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    public void loadData()
    {
        Log.d(TAG, "loadData: ");
        notificationProgress.showNotificationLoading();
        isLoading = true;
        isFinish = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(MainActivity.time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                isLoading = false;
                isFinish = true;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notificationProgress.showNotificationReady();
                        asyncResponse.finish();
                    }
                });
            }
        }).start();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isFinish() {
        return isFinish;
    }
}
