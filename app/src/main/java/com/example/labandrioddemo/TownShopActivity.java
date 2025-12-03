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
import com.example.labandrioddemo.databinding.ActivityTownShopBinding;

public class TownShopActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    private ActivityTownShopBinding binding;
    private AccountRepository repository;
    private TownShopActivity thisHolder = this;
    private ProjectCharacter character;
    private int loggedInCharacterId = LOGGED_OUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTownShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AccountRepository.getRepository(getApplication());

        loggedInCharacterId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, LOGGED_OUT);

        binding.attackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyAttack();
            }
        });

        binding.healthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyHealth();
            }
        });

        binding.fleeChanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyFleeChance();
            }
        });

        LiveData<ProjectCharacter> characterLiveData = repository.getCharacterByCharacterId(loggedInCharacterId);
        characterLiveData.observe(this, new Observer<ProjectCharacter>() {
            @Override
            public void onChanged(ProjectCharacter character) {
                if(character != null) {
                    thisHolder.character = character;

                    binding.goldTextView.setText("Gold: " + character.getGold());
                    binding.atkModTextView.setText("Attack Modifier: +" + character.getAtkMod());
                    binding.healthTextView.setText("Health: " + character.getCurrHp() + "/" + character.getMaxHp());
                    binding.fleeChanceTextView.setText("Flee Chance: " + character.getFleeChance() + "%");

                    characterLiveData.removeObserver(this);
                }
            }
        });
    }

    private void buyAttack() {
        if(character != null) {
            if(character.getGold() < 20) {
                makeToast("Insufficient gold.");
            } else {
                character.setGold(character.getGold() - 20);

                character.setAtkMod(character.getAtkMod() + 1);

                repository.updateCharacter(character);
                binding.goldTextView.setText("Gold: " + character.getGold());
                binding.atkModTextView.setText("Attack Modifier: +" + character.getAtkMod());
                makeToast("Purchased +1 to Attack Modifier.");
            }
        }
    }

    private void buyHealth() {
        if(character != null) {
            if(character.getGold() < 30) {
                makeToast("Insufficient gold.");
            } else {
                character.setGold(character.getGold() - 30);

                character.setMaxHp(character.getMaxHp() + 5);
                character.setCurrHp(character.getCurrHp() + 5);

                repository.updateCharacter(character);
                binding.goldTextView.setText("Gold: " + character.getGold());
                binding.healthTextView.setText("Health: " + character.getCurrHp() + "/" + character.getMaxHp());
                makeToast("Purchased +5 to Health.");
            }
        }
    }

    private void buyFleeChance() {
        if(character != null) {
            if(character.getGold() < 10) {
                makeToast("Insufficient gold.");
            } else if(character.getFleeChance() >= 100) {
                makeToast("Flee Chance already 100%.");
            } else {
                character.setGold(character.getGold() - 10);

                character.setFleeChance(character.getFleeChance() + 5);

                repository.updateCharacter(character);
                binding.goldTextView.setText("Gold: " + character.getGold());
                binding.fleeChanceTextView.setText("Flee Chance: " + character.getFleeChance() + "%");
                makeToast("Purchased +5% to Flee Chance.");
            }
        }
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent townShopIntentFactory(Context context, int userId, int characterId) {
        Intent intent = new Intent(context, TownShopActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}