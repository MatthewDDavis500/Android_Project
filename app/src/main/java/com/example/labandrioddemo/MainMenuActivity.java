package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterSelectActivity.COMP_DOOM_ACTIVITY_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandrioddemo.databinding.ActivityMainMenuBinding;

public class MainMenuActivity extends AppCompatActivity {
    public static final String COMP_DOOM_ACTIVITY_CHARACTER_ID = "com.example.labandrioddemo.COMP_DOOM_ACTIVITY_CHARACTER_ID";
    private ActivityMainMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.nextMissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open mission activity
            }
        });

        binding.visitTownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open town activity
            }
        });

        binding.adminPowersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AdminPowersActivity.adminPowersIntentFactory(getApplicationContext(), 1, 1));
            }
        });
    }

    static Intent mainMenuIntentFactory(Context context, int userId, int characterId) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}