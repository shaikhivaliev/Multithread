package com.multithread.task.Service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.multithread.task3.R;

public class ActivityService extends AppCompatActivity{
    public static final String TAG = ActivityService.class.getSimpleName();
    public static final int MSG_LOADING = 0;
    public static final int MSG_READY = 1;
    public static final String KEY_LOADING = "KEY_LOADING";
    public static final String KEY_FINISH = "KEY_FINISH";
    private Messenger messengerService, messengerClient;
    private TextView tvProgress;
    private ProgressBar progressBarRound;
    private Button button;
    private boolean isLoading = false;
    private boolean isFinish = false;
    private boolean mBound;
    private Intent intentService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: ");
            messengerService = new Messenger(iBinder);
            Message message = Message.obtain();
            message.replyTo = messengerClient;
            try {
                messengerService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected: ");
            messengerService = null;
            mBound = false;
        }
    };

    private class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case (MSG_LOADING):
                    Log.d(TAG, "handleMessage: loading");
                    setViewsForLoading();
                    break;

                case (MSG_READY):
                    Log.d(TAG, "handleMessage: ready");
                    setViewsForReady();
                    break;
            }
            super.handleMessage(msg);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.loader_content);
        progressBarRound = findViewById(R.id.progressBarRound);
        tvProgress = findViewById(R.id.tvProgress);
        button = findViewById(R.id.button);
        
        messengerClient = new Messenger(new IncomingHandler());
        intentService = new Intent(this, ServiceLoader.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(intentService);
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(KEY_LOADING))
                setViewsForLoading();

            if (savedInstanceState.getBoolean(KEY_FINISH))
                setViewsForReady();
        }
    }


    private void setViewsForLoading() {
        button.setEnabled(false);
        progressBarRound.setVisibility(View.VISIBLE);
        tvProgress.setText(getResources().getString(R.string.loading));
        isLoading = true;
        isFinish = false;
    }

    private void setViewsForReady() {
        button.setEnabled(true);
        progressBarRound.setVisibility(View.INVISIBLE);
        tvProgress.setText(getResources().getString(R.string.ready));
        isLoading = false;
        isFinish = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        bindService(intentService, serviceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if (mBound)
        {
            unbindService(serviceConnection);
            mBound = false;
        }
        super.onDestroy();
    }

    //Может не вызываться.
    //Не совсем стабильная штука в подобной реализации с асинхронной загрузкой файла.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putBoolean(KEY_LOADING, isLoading);
        outState.putBoolean(KEY_FINISH, isFinish);
    }
}
