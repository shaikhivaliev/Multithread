package com.multithread.task.Singleton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.multithread.task.NotificationProgress;
import com.multithread.task3.R;

import java.util.Observable;
import java.util.Observer;

public class SingletonActivity extends AppCompatActivity implements Observer {
    public static final String TAG = SingletonActivity.class.getSimpleName();

    private NotificationProgress notification;
    private LoaderSingleton loader;

    private TextView tvProgress;
    private ProgressBar progressBarRound;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.loader_content);
        progressBarRound = findViewById(R.id.progressBarRound);
        tvProgress = findViewById(R.id.tvProgress);
        button = findViewById(R.id.button);

        notification = new NotificationProgress(this);
        loader = LoaderSingleton.getInstance();
        loader.addObserver(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewsForLoading();
                notification.showNotificationLoading();
                loader.loadData();
            }
        });

        if (loader.isLoading())
            setViewsForLoading();

        if (loader.isFinish())
            setViewsForReady();
    }


    private void setViewsForLoading() {
        button.setEnabled(false);
        progressBarRound.setVisibility(View.VISIBLE);
        tvProgress.setText(getResources().getString(R.string.loading));
    }

    private void setViewsForReady() {
        button.setEnabled(true);
        progressBarRound.setVisibility(View.INVISIBLE);
        tvProgress.setText(getResources().getString(R.string.ready));
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof LoaderSingleton) {
            setViewsForReady();
            notification.showNotificationReady();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loader.deleteObserver(this);
    }
}
