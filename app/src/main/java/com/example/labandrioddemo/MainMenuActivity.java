package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterSelectActivity.COMP_DOOM_ACTIVITY_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandrioddemo.databinding.ActivityMainMenuBinding;

public class MainMenuActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    public static final String COMP_DOOM_ACTIVITY_CHARACTER_ID = "com.example.labandrioddemo.COMP_DOOM_ACTIVITY_CHARACTER_ID";
    public static final String COMP_DOOM_ACTIVITY_IS_ADMIN_ID = "com.example.labandrioddemo.COMP_DOOM_ACTIVITY_IS_ADMIN_ID";
    private ActivityMainMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getIntent().getBooleanExtra(COMP_DOOM_ACTIVITY_IS_ADMIN_ID, false)) {
            binding.adminPowersButton.setVisibility(View.VISIBLE);
        } else {
            binding.adminPowersButton.setVisibility(View.INVISIBLE);
        }

        binding.nextMissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BattleScreenActivity.BattleScreenIntentFactory(getApplicationContext(),
                        getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, LOGGED_OUT)
                ));
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
                startActivity(AdminPowersActivity.adminPowersIntentFactory(getApplicationContext(),
                        getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT),
                        getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, LOGGED_OUT),
                        getIntent().getBooleanExtra(COMP_DOOM_ACTIVITY_IS_ADMIN_ID, false)
                ));
            }
        });

        binding.mainMenuBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CharacterSelectActivity.characterSelectIntentFactory(getApplicationContext(),
                        getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT)
                ));
            }
        });
    }

    static Intent mainMenuIntentFactory(Context context, int userId, int characterId, boolean isAdmin) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        intent.putExtra(COMP_DOOM_ACTIVITY_IS_ADMIN_ID, isAdmin);
        return intent;
    }
}