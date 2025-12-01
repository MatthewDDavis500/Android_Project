package com.example.labandrioddemo;

import static com.example.labandrioddemo.MainMenuActivity.COMP_DOOM_ACTIVITY_CHARACTER_ID;
import static com.example.labandrioddemo.databinding.ActivityBattleScreenBinding.inflate;

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

public class BattleScreenActivity extends AppCompatActivity {

    private ActivityBattleScreenBinding binding;
    private AccountRepository repository;
    private int battleNum;

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
                    binding.battleName.setText("Battle " + battleNum);
                }
            }
        });
        //Don't put stuff here, too slow ^
    }

    static Intent BattleScreenIntentFactory(Context context, int characterId) {
        Intent intent = new Intent(context, BattleScreenActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}