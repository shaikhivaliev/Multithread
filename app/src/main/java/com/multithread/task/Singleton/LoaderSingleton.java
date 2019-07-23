package com.multithread.task.Singleton;

import android.os.Handler;
import android.util.Log;

import com.multithread.task.MainActivity;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

public final class LoaderSingleton extends Observable {
    public static final String TAG =  LoaderSingleton.class.getSimpleName();
    private static LoaderSingleton instance;
    private static Handler handler;
    private boolean isLoading = false;
    private boolean isFinish = false;

    private LoaderSingleton()
    {
        Log.d(TAG, "LoaderSingleton: ");
        handler = new Handler();
    }

    public static LoaderSingleton getInstance()
    {
        Log.d(TAG, "getInstance: ");
        if (instance == null)
            instance = new LoaderSingleton();
        return instance;
    }

    public void loadData()
    {
        Log.d(TAG, "loadData: ");
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
                        setChanged();
                        notifyObservers();
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
