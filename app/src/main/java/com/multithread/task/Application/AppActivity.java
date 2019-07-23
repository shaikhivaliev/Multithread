package com.multithread.task.Application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.multithread.task3.R;

public class AppActivity extends AppCompatActivity {
    public static final String TAG = AppActivity.class.getSimpleName();
    private MyApplication application;

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

        application = (MyApplication) getApplication();
        application.setAsyncResponse(new AsyncResponse() {
            @Override
            public void finish() {
                setViewsForReady();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewsForLoading();
                application.loadData();
            }
        });


        if (application.isLoading())
            setViewsForLoading();

        if (application.isFinish())
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
}
