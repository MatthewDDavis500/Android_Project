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
import com.example.labandrioddemo.databinding.ActivityGameOverScreenBinding;


public class GameOverScreenActivity extends AppCompatActivity {

    private ActivityGameOverScreenBinding binding;
    private ProjectCharacter character;
    private AccountRepository repository;
    private GameOverScreenActivity thisHolder = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameOverScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // instantiate repository to allow for database access
        repository = AccountRepository.getRepository(getApplication());

        // search for the current character to display the character's final level
        LiveData<ProjectCharacter> characterLiveData = repository.getCharacterByCharacterId(getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, -1));
        characterLiveData.observe(this, new Observer<ProjectCharacter>() {
            @Override
            public void onChanged(ProjectCharacter character) {
                if (character != null) {
                    characterLiveData.removeObserver(this);
                    thisHolder.character = character;
                    binding.finalLevelTextView.setText("Final Level: " + character.getLvl());
                }
            }
        });

        binding.RestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.deleteBattleHistoryByCharacterId(character.getCharacterID());  // delete the character's battle history
                repository.deleteCharacter(character);  // delete the character
                startActivity(CharacterSelectActivity.characterSelectIntentFactory(getApplicationContext(), character.getUserID())); // move to character select activity
            }
        });
    }

    static Intent GameOverScreenIntentFactory(Context context, int characterId) {
        Intent intent = new Intent(context, GameOverScreenActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}