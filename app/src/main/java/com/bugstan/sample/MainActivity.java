package com.bugstan.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bugstan.sample.databinding.ActivityMainBinding;
import com.bugstan.sample.dialog.PagerPanelDialog;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        mViewBinding.showPageViewDialog.setOnClickListener(v -> new PagerPanelDialog().show(getSupportFragmentManager(), ""));
    }
}