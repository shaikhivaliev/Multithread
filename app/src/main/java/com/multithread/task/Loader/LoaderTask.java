package com.multithread.task.Loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.multithread.task.MainActivity;
import com.multithread.task.NotificationProgress;

import java.util.concurrent.TimeUnit;

public class LoaderTask extends AsyncTaskLoader<Integer> {
    public static final String TAG = LoaderTask.class.getSimpleName();
    private boolean isLoading = false;
    private NotificationProgress notification;

    public LoaderTask(@NonNull Context context) {
        super(context);
        Log.d(TAG, "LoaderTask: ");
        notification = new NotificationProgress(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "onStartLoading: ");
        if (!isLoading) {
            Log.d(TAG, "onStartLoading: forceLoad");
            isLoading = true;
            forceLoad();
        }
    }

    @Nullable
    @Override
    public Integer loadInBackground() {
        notification.showNotificationLoading();
        Log.d(TAG, "loadInBackground: start");
        try {
            TimeUnit.MILLISECONDS.sleep(MainActivity.time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "loadInBackground: finish");
        notification.showNotificationReady();
        return null;
    }
}
