package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterSelectActivity.COMP_DOOM_ACTIVITY_USER_ID;
import static com.example.labandrioddemo.MainMenuActivity.COMP_DOOM_ACTIVITY_CHARACTER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityBattleHistoryBinding;
import com.example.labandrioddemo.viewHolders.HistoryAdapter;
import com.example.labandrioddemo.viewHolders.HistoryViewModel;

public class BattleHistoryActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    private ActivityBattleHistoryBinding binding;
    private AccountRepository repository;
    private HistoryViewModel historyViewModel;
    private User user;
    private int loggedInUserId = LOGGED_OUT;
    private int loggedInCharacterId = LOGGED_OUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBattleHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AccountRepository.getRepository(getApplication());

        loggedInUserId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT);
        loggedInCharacterId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, LOGGED_OUT);

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        RecyclerView recyclerView = binding.historyRecyclerView;
        final HistoryAdapter adapter = new HistoryAdapter(new HistoryAdapter.HistoryDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyViewModel.getBattleByCharacterId(loggedInCharacterId).observe(this, battleHistories -> {
            adapter.submitList(battleHistories);
        });

        binding.historyBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainMenuActivity.mainMenuIntentFactory(getApplicationContext(), loggedInUserId, loggedInCharacterId));
            }
        });


    }

    static Intent battleHistoryIntentFactory(Context context, int userId, int characterId) {
        Intent intent = new Intent(context, BattleHistoryActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}