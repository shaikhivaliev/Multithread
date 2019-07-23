package com.multithread.task.Retain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.multithread.task3.R;

public class FragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, FragmentRetain.newInstance())
                    .commit();
        }
    }
}
