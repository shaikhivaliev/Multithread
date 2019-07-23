package com.multithread.task.Retain;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.multithread.task.NotificationProgress;
import com.multithread.task.MainActivity;
import com.multithread.task3.R;

import java.util.concurrent.TimeUnit;

public class FragmentRetain extends Fragment {
    public static final String TAG = FragmentRetain.class.getSimpleName();

    private NotificationProgress notification;
    private MyAsyncTask task;

    private TextView tvProgress;
    private ProgressBar progressBarRound;
    private Button button;

    private boolean isLoading = false;
    private boolean isFinish = false;

    private int currentProgress = 0;
    
    public static FragmentRetain newInstance() {
        Log.d(TAG, "newInstance: ");
        Bundle args = new Bundle();
        FragmentRetain fragment = new FragmentRetain();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        notification = new NotificationProgress(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.loader_content, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        progressBarRound = view.findViewById(R.id.progressBarRound);
        tvProgress = view.findViewById(R.id.tvProgress);
        button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = new MyAsyncTask();
                task.execute();
            }
        });

        if (isLoading)
            setViewsForLoading();

        if (isFinish)
            setViewsForReady();
    }

    private String getProgress()
    {
        return getResources().getString(R.string.loading) + currentProgress + "%";
    }

    private void setViewsForLoading()
    {
        if (!isLoading)
            currentProgress = 0;
        button.setEnabled(false);
        progressBarRound.setVisibility(View.VISIBLE);
        tvProgress.setText(getProgress());
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


    public class MyAsyncTask extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: ");
            super.onPreExecute();
            setViewsForLoading();
            notification.showNotificationLoading();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "onPostExecute: ");
            super.onPostExecute(aVoid);
            setViewsForReady();
            notification.showNotificationReady();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: ");
            currentProgress = values[0];
            tvProgress.setText(getProgress());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: start");
            int count = 5;
            for (int i = 1; i <= count; i++) {
                try {
                    if (isCancelled()) break;
                    TimeUnit.MILLISECONDS.sleep(MainActivity.time / count);
                    int progressInPercent = i * 100 / count;
                    publishProgress(progressInPercent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "doInBackground: finish");
            return null;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        //Прерываем поток. Асинк таск продолжает работать. Он обращается к несуществуемому контексту. Возникает ошибку.
        //Поэтому нужно прервать поток при уничтожении фрагмента или актиновсти.
        task.cancel(false);
        notification.cancel();
    }
}
