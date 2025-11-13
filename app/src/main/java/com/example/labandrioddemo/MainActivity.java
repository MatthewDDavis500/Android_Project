package com.example.labandrioddemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandrioddemo.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "COMP_DOOM";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}