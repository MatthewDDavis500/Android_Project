package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class CharacterCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_creation);
    }

    static Intent characterCreationIntentFactory(Context context) {
        return new Intent(context, CharacterCreationActivity.class);
    }
}