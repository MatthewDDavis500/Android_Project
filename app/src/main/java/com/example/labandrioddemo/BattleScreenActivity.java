package com.example.labandrioddemo;

import static com.example.labandrioddemo.MainMenuActivity.COMP_DOOM_ACTIVITY_CHARACTER_ID;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.databinding.ActivityBattleScreenBinding;
import java.util.Random;

public class BattleScreenActivity extends AppCompatActivity {

    private ActivityBattleScreenBinding binding;
    private AccountRepository repository;
    private int battleNum;
    private int monsterMaxHp;
    private int monsterCurHp;

    Random random = new Random();

    private ProjectCharacter character;
    private BattleScreenActivity thisHolder = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBattleScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = AccountRepository.getRepository(getApplication());
        LiveData<ProjectCharacter> characterLiveData = repository.getCharacterByCharacterId(getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, -1));
        characterLiveData.observe(this, new Observer<ProjectCharacter>() {
            /**
             * Creates the initial layout of the screen by pulling data related to character
             */
            @Override
            public void onChanged(ProjectCharacter character) {
                if (character != null) {
                    characterLiveData.removeObserver(this);
                    thisHolder.character = character;
                    binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                    binding.battleName.setText("Battle " + character.getBattleNum());
                    monsterMaxHp = (character.getBattleNum()- 1) * 2 + 10;
                    monsterCurHp = monsterMaxHp;
                    binding.enemyHpTextView.setText("Enemy HP: " + monsterCurHp + "/" + monsterMaxHp);
                    binding.currentSituationTextView.setText("A Monster Appears!");
                }
            }
        });
        //Don't put stuff here, too slow ^

        /**
         * On click listener for attack, does damage to both player and monster, checks if either are dead.
         */
        binding.attackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int monsterDmg = random.nextInt(character.getBattleNum() - 1,3 + character.getBattleNum());
                if (monsterCurHp > 0) {
                    character.setCurrHp(character.getCurrHp() - monsterDmg);
                    binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                }
                int playerDmg = random.nextInt(3,6) + character.getAtkMod();
                monsterCurHp = monsterCurHp - (playerDmg);
                binding.enemyHpTextView.setText("Enemy HP: " + monsterCurHp + "/" + monsterMaxHp);
                if (character.getCurrHp() > 0) {
                    if (monsterCurHp > 0) {
                        binding.currentSituationTextView.setText(character.getCharacterName() + " did "  + playerDmg + " and took " + monsterDmg + " damage.");
                    } else {
                        binding.currentSituationTextView.setText("You beat the monster!");
                        binding.fleeButton.setText("Return Home");
                    }
                } else {
                    binding.currentSituationTextView.setText("You died!");
                    binding.fleeButton.setText("Game Over");
                }
            }
        });

        /**
         * Flee button rolls to see if you escape and tells you if you did, also move you to victory or loss
         * based on game state.
         */
        binding.fleeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chance = random.nextInt(1 + character.getFleeChance(),10);
                if (monsterCurHp <= 0) {
                    startActivity(VictoryScreen.VictoryScreenIntentFactory(getApplicationContext(), character.getUserID(), character.getCharacterID()));
                } else if (character.getCurrHp() <= 0) {
                    startActivity(GameOverScreenActivity.GameOverScreenIntentFactory(getApplicationContext(), character.getCharacterID()));
                } else {
                    if (chance > 5) {
                        startActivity(MainMenuActivity.mainMenuIntentFactory(getApplicationContext(), character.getUserID(), character.getCharacterID()));
                    } else {
                        binding.currentSituationTextView.setText("You tried to flee and failed!");
                        character.setCurrHp(character.getCurrHp() - random.nextInt(character.getBattleNum(),5 + character.getBattleNum()));
                        binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                    }
                }
            }
        });
    }


    /**
     * BattleScreenIntentFactory takes in context and characterId for accessing all the related data
     * @param context the screen
     * @param characterId the id for the selected character
     * @return the intent
     */
    static Intent BattleScreenIntentFactory(Context context, int characterId) {
        Intent intent = new Intent(context, BattleScreenActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}