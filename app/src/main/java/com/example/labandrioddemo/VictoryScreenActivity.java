package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterSelectActivity.COMP_DOOM_ACTIVITY_USER_ID;
import static com.example.labandrioddemo.MainMenuActivity.COMP_DOOM_ACTIVITY_CHARACTER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.BattleHistory;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.databinding.ActivityVictoryScreenBinding;

import kotlin.random.Random;


public class VictoryScreenActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    private ActivityVictoryScreenBinding binding;
    private AccountRepository repository;
    private ProjectCharacter character;
    private Random random = Random.Default;
    private int loggedInCharacterId = LOGGED_OUT;
    private VictoryScreenActivity thisHolder = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVictoryScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = AccountRepository.getRepository(getApplication());

        loggedInCharacterId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, LOGGED_OUT);

        LiveData<ProjectCharacter> characterLiveData = repository.getCharacterByCharacterId(loggedInCharacterId);
        characterLiveData.observe(this, new Observer<ProjectCharacter>() {
            @Override
            public void onChanged(ProjectCharacter character) {
                if (character != null) {
                    characterLiveData.removeObserver(this);
                    thisHolder.character = character;

                    BattleHistory battleRecord = new BattleHistory(loggedInCharacterId, character.getBattleNum(), character.getCurrHp(), true);
                    repository.insertBattleHistory(battleRecord);

                    int goldGained = random.nextInt(3,5) * character.getBattleNum();
                    binding.goldGainedTextView.setText(goldGained + " gold gained");
                    character.setGold(character.getGold() + goldGained);

                    character.setBattleNum(character.getBattleNum() + 1);
                    if (character.getLvl() * 2 == character.getBattleNum()) {
                        character.setLvl(character.getLvl() + 1);
                        binding.levelUpTextView.setText("Level Up!");
                    } else {
                        binding.levelUpTextView.setText(" ");
                    }

                    repository.updateCharacter(character);
                }
            }
        });
        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainMenuActivity.mainMenuIntentFactory(getApplicationContext(),
                        getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT),
                        loggedInCharacterId
                ));
            }
        });
    }

    static Intent VictoryScreenIntentFactory(Context context, int userId, int characterId) {
        Intent intent = new Intent(context, VictoryScreenActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}