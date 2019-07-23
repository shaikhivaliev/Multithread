package com.multithread.task.Loader;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.multithread.task3.R;

public class LoaderActivity extends AppCompatActivity {
    public static final String TAG = LoaderActivity.class.getSimpleName();
    public static final int ID_LOADER = 202;

    private TextView tvProgress;
    private ProgressBar progressBarRound;
    private Button button;

    private boolean isLoading = false;
    private boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader_content);
        progressBarRound = findViewById(R.id.progressBarRound);
        tvProgress = findViewById(R.id.tvProgress);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFinish)
                    getSupportLoaderManager().initLoader(ID_LOADER, Bundle.EMPTY, new LoaderCallback());
                else
                    getSupportLoaderManager().restartLoader(ID_LOADER, Bundle.EMPTY, new LoaderCallback());
            }
        });

        if (isLoading)
            setViewsForLoading();

        if (isFinish)
            setViewsForReady();
    }


    private void setViewsForLoading()
    {
        button.setEnabled(false);
        progressBarRound.setVisibility(View.VISIBLE);
        tvProgress.setText(getResources().getString(R.string.loading));
        isLoading = true;
        isFinish = false;
    }

    private void setViewsForReady()
    {
        button.setEnabled(true);
        progressBarRound.setVisibility(View.INVISIBLE);
        tvProgress.setText(getResources().getString(R.string.ready));
        isLoading = false;
        isFinish = true;
    }


    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Integer>
    {
        @NonNull
        @Override
        public Loader<Integer> onCreateLoader(int id, @Nullable Bundle args) {
            if (id == ID_LOADER)
            {
                Log.d(TAG, "onCreateLoader: ");
                setViewsForLoading();
                return new LoaderTask(LoaderActivity.this);
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Integer> loader, Integer data) {
            if (loader.getId() == ID_LOADER)
            {
                Log.d(TAG, "onLoadFinished: ");
                setViewsForReady();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Integer> loader) {
            Log.d(TAG, "onLoaderReset: ");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Добавлен флаг в манифесте.
        //Удобен в том плане, что активность не пересоздается при смене ориентации.
        //Альтернативные ресурсы не используются, поэтому изменяться разметка не будет.
        Toast.makeText(this, "onConfigurationChanged", Toast.LENGTH_SHORT).show();
    }
}
