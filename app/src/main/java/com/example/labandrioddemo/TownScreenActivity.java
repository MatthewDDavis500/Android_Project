package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterSelectActivity.COMP_DOOM_ACTIVITY_USER_ID;
import static com.example.labandrioddemo.MainMenuActivity.COMP_DOOM_ACTIVITY_CHARACTER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.databinding.ActivityTownScreenBinding;

public class TownScreenActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    private ActivityTownScreenBinding binding;
    private AccountRepository repository;
    private TownScreenActivity thisHolder = this;
    private ProjectCharacter character;
    private int loggedInCharacterId = LOGGED_OUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTownScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AccountRepository.getRepository(getApplication());

        loggedInCharacterId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, LOGGED_OUT);

        binding.restButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restCharacter();
            }
        });

        binding.leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainMenuActivity.mainMenuIntentFactory(getApplicationContext(),
                        getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT),
                        loggedInCharacterId
                ));
            }
        });

        LiveData<ProjectCharacter> characterLiveData = repository.getCharacterByCharacterId(loggedInCharacterId);
        characterLiveData.observe(this, new Observer<ProjectCharacter>() {
            @Override
            public void onChanged(ProjectCharacter character) {
                if(character != null) {
                    thisHolder.character = character;

                    binding.levelTextView.setText("Level: " + character.getLvl());
                    binding.goldTextView.setText("Gold: " + character.getGold());
                    binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());

                    characterLiveData.removeObserver(this);
                }
            }
        });

    }

    private void restCharacter() {
        if(character != null) {
            if(character.getGold() < 10) {
                makeToast("Insufficient gold.");
            } else if(character.getCurrHp() == character.getMaxHp()) {
                makeToast("Already at full health.");
            } else {
                character.setGold(character.getGold() - 10);

                int currentHp = character.getCurrHp();
                currentHp += character.getMaxHp() / 4;
                if(currentHp > character.getMaxHp()) {
                    currentHp = character.getMaxHp();
                }
                character.setCurrHp(currentHp);

                repository.updateCharacter(character);
                binding.goldTextView.setText("Gold: " + character.getGold());
                binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                makeToast("Rest Successful.");
            }
        }
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent townScreenIntentFactory(Context context, int userId, int characterId) {
        Intent intent = new Intent(context, TownScreenActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}