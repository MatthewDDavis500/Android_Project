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
            @Override
            public void onChanged(ProjectCharacter character) {
                if (character != null) {
                    characterLiveData.removeObserver(this);
                    thisHolder.character = character;
                    binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                    binding.battleName.setText("Battle " + character.getBattleNum());
                    monsterMaxHp = character.getBattleNum()*2 + 10;
                    monsterCurHp = monsterMaxHp;
                    binding.enemyHpTextView.setText("Enemy HP: " + monsterCurHp + "/" + monsterMaxHp);
                    binding.currentSituationTextView.setText("A Monster Appears!");
                }
            }
        });
        //Don't put stuff here, too slow ^

        binding.attackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monsterCurHp > 0) {
                    character.setCurrHp(character.getCurrHp() - random.nextInt(character.getBattleNum(),5 + character.getBattleNum()));
                    binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                }
                monsterCurHp = monsterCurHp - ( 5 + character.getAtkMod());
                binding.enemyHpTextView.setText("Enemy HP: " + monsterCurHp + "/" + monsterMaxHp);
                if (character.getCurrHp() > 0) {
                    if (monsterCurHp > 0) {
                        binding.currentSituationTextView.setText(character.getCharacterName() + " did and took damage.");
                    }
                    binding.currentSituationTextView.setText("You beat the monster!");
                } else {
                    binding.currentSituationTextView.setText("You died!");
                }
            }
        });

        binding.fleeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chance = random.nextInt(1 + character.getFleeChance(),10);
                if (monsterCurHp <= 0) {
                    //go to victory screen
                } else if (character.getCurrHp() <= 0) {
                    //go to loss screen
                } else {
                    if (chance > 5) {
                        // return to main menu
                    } else {
                        binding.currentSituationTextView.setText("You tried to flee and failed!");
                        character.setCurrHp(character.getCurrHp() - random.nextInt(character.getBattleNum(),5 + character.getBattleNum()));
                        binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                    }
                }
            }
        });
    }



    static Intent BattleScreenIntentFactory(Context context, int characterId) {
        Intent intent = new Intent(context, BattleScreenActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}