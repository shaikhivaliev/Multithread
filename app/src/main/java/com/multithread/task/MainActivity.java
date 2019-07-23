package com.multithread.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.multithread.task.Application.AppActivity;
import com.multithread.task.Loader.LoaderActivity;
import com.multithread.task.Retain.FragmentActivity;
import com.multithread.task.Service.ActivityService;
import com.multithread.task.Singleton.SingletonActivity;
import com.multithread.task3.R;

public class MainActivity extends AppCompatActivity {
    public static long time = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToFragmentActivity(View view) {
        Intent intent = new Intent(this, FragmentActivity.class);
        startActivity(intent);
    }

    public void goToAsyncTaskLoader(View view) {
        Intent intent = new Intent(this, LoaderActivity.class);
        startActivity(intent);
    }

    public void goToSingleton(View view) {
        Intent intent = new Intent(this, SingletonActivity.class);
        startActivity(intent);
    }

    public void goToApplication(View view) {
        Intent intent = new Intent(this, AppActivity.class);
        startActivity(intent);
    }

    public void goToService(View view) {
        Intent intent = new Intent(this, ActivityService.class);
        startActivity(intent);
    }
}
