package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterSelectActivity.COMP_DOOM_ACTIVITY_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityBattleHistoryBinding;

public class BattleHistoryActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    private ActivityBattleHistoryBinding binding;
    private AccountRepository repository;
    private User user;
    private int loggedInUserId = LOGGED_OUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBattleHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AccountRepository.getRepository(getApplication());



    }

    static Intent battleHistoryIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, BattleHistoryActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        return intent;
    }
}