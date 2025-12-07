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
    private int loggedInUserId = LOGGED_OUT;
    private int loggedInCharacterId = LOGGED_OUT;
    public static final int SUCCESS = 0;
    public static final int NOT_ENOUGH_GOLD = 1;
    public static final int ALREADY_MAX_HEALTH = 2;
    private TownScreen townScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTownScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // instantiate repository to allow for database access
        repository = AccountRepository.getRepository(getApplication());

        // get info from intent extras
        loggedInUserId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT);
        loggedInCharacterId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, LOGGED_OUT);

        // instantiate TownScreen to access logic functions
        townScreen = new TownScreen();

        binding.restButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restCharacter();
            }
        });

        binding.shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TownShopActivity.townShopIntentFactory(getApplicationContext(), loggedInUserId, loggedInCharacterId));
            }
        });

        binding.leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainMenuActivity.mainMenuIntentFactory(getApplicationContext(), loggedInUserId, loggedInCharacterId));
            }
        });

        // search for current character to populate relevant character data
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

    /**
     * This method attempts to purchase a rest with the character's gold.
     * <br>
     * If the character doesn't have enough gold, return.
     * If the character is already at full health, return.
     * <br>
     * Otherwise, add 25% of the character's maxHp to their currHp (without overflowing)
     * Also removes the necessary gold from the character's gold
     */
    private void restCharacter() {
        if(character != null) {
            int result = townScreen.attemptRest(character);

            if(result == SUCCESS) {
                repository.updateCharacter(character);
                binding.goldTextView.setText("Gold: " + character.getGold());
                binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                makeToast("Rest Successful.");
            } else if(result == NOT_ENOUGH_GOLD) {
                makeToast("Insufficient gold.");
            } else if(result == ALREADY_MAX_HEALTH) {
                makeToast("Already at full health.");
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