package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterSelectActivity.COMP_DOOM_ACTIVITY_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.databinding.ActivityMainMenuBinding;

public class MainMenuActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    public static final String COMP_DOOM_ACTIVITY_CHARACTER_ID = "com.example.labandrioddemo.COMP_DOOM_ACTIVITY_CHARACTER_ID";
    private ActivityMainMenuBinding binding;
    private AccountRepository repository;
    private int loggedInUserId = LOGGED_OUT;
    private int loggedInCharacterId = LOGGED_OUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // instantiate repository to allow for database access
        repository = AccountRepository.getRepository(getApplication());

        // get info from intent extras
        loggedInUserId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT);
        loggedInCharacterId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, LOGGED_OUT);

        // get the ProjectCharacter object corresponding to the logged in user
        LiveData<ProjectCharacter> characterLiveData = repository.getCharacterByCharacterId(loggedInCharacterId);
        characterLiveData.observe(this, new Observer<ProjectCharacter>() {
            @Override
            public void onChanged(ProjectCharacter character) {
                if(character != null) {
                    binding.titleLoginTextView.setText("Welcome\nAdventurer\n" + character.getCharacterName());
                    binding.levelTextView.setText("Level: " + character.getLvl());
                    binding.goldTextView.setText("Gold: " + character.getGold());
                    binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());

                    characterLiveData.removeObserver(this);
                }
            }
        });


        binding.nextMissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BattleScreenActivity.battleScreenIntentFactory(getApplicationContext(), loggedInCharacterId));
            }
        });

        binding.visitTownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TownScreenActivity.townScreenIntentFactory(getApplicationContext(), loggedInUserId, loggedInCharacterId));
            }
        });

        binding.battleHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BattleHistoryActivity.battleHistoryIntentFactory(getApplicationContext(), loggedInUserId, loggedInCharacterId));
            }
        });

        binding.mainMenuBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CharacterSelectActivity.characterSelectIntentFactory(getApplicationContext(), loggedInUserId));
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