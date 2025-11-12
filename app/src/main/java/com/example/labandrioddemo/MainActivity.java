package com.example.labandrioddemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandrioddemo.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}