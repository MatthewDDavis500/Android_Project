package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.databinding.ActivityCharacterCreationBinding;

public class CharacterCreationActivity extends AppCompatActivity {
    private ActivityCharacterCreationBinding creationBinding;
    private ProjectCharacter character;
    private AccountRepository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creationBinding = ActivityCharacterCreationBinding.inflate(getLayoutInflater());
        setContentView(creationBinding.getRoot());
        repository = AccountRepository.getRepository(getApplication());
        String name = creationBinding.nameEnterEditText.getText().toString();

        creationBinding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character = new ProjectCharacter(name, 3,
                character.getCharacterID(), 3, 500000, character.getCurrHp(), character.getMaxHp(),
                character.getAtkMod(), character.getFleeChance(), character.getBattleNum(), character.getSlot());
                repository.insertCharacter(character);
                startActivity(CharacterSelectActivity.characterSelectIntentFactory(getApplicationContext(), character.getCharacterID()));
            }
        });
    }
    static Intent characterCreationIntentFactory(Context context) {
        return new Intent(context, CharacterCreationActivity.class);
    }
}