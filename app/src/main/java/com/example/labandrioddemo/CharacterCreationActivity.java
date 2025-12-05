package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.databinding.ActivityCharacterCreationBinding;

public class CharacterCreationActivity extends AppCompatActivity {
    private ActivityCharacterCreationBinding binding;
    private AccountRepository repository;
    private static int userId;
    private static int slot;
    public static final String COMP_DOOM_ACTIVITY_USER_ID = "com.example.labandrioddemo.COMP_DOOM_ACTIVITY_USER_ID";
    public static final String COMP_DOOM_ACTIVITY_SLOT = "com.example.labandrioddemo.COMP_DOOM_ACTIVITY_SLOT";
    private String characterClass = "None";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCharacterCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AccountRepository.getRepository(getApplication());
        userId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, -1);
        slot = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_SLOT, -1);

        binding.knightClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.knightClass.isChecked()) {
                    characterClass = "knight";
                    binding.rangerClass.setChecked(false);
                    binding.mageClass.setChecked(false);
                } else {
                    characterClass = "None";
                }
            }
        });

        binding.rangerClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.rangerClass.isChecked()) {
                    characterClass = "ranger";
                    binding.knightClass.setChecked(false);
                    binding.mageClass.setChecked(false);
                } else {
                    characterClass = "None";
                }
            }
        });

        binding.mageClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.mageClass.isChecked()) {
                    characterClass = "mage";
                    binding.knightClass.setChecked(false);
                    binding.rangerClass.setChecked(false);
                } else {
                    characterClass = "None";
                }
            }
        });

        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(createCharacter()) {
                    // Steven's genius
                    finish();
                }
            }
        });
    }

    public boolean createCharacter() {
        String name = binding.nameEnterEditText.getText().toString();
        if(name.isEmpty()) {
            makeToast("Name cannot be empty.");
            return false;
        }

        if(characterClass.equals("None")) {
            makeToast("Character must have a class.");
            return false;
        }

        ProjectCharacter character;

        if(characterClass.equals("knight")) {
            character = new ProjectCharacter(name, userId, 1, 20,
                    120, 120, 2,
                    5, 1, slot);
        } else if(characterClass.equals("ranger")) {
            character = new ProjectCharacter(name, userId, 1, 40,
                    80, 80, 1,
                    5, 1, slot);
        } else if(characterClass.equals("mage")) {
            character = new ProjectCharacter(name, userId, 1, 0,
                    100, 100, 4,
                    5, 1, slot);
        } else {
            makeToast("Character must have a class.");
            return false;
        }

        repository.insertCharacter(character);
        makeToast("Character creation successful.");
        return true;
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    static Intent characterCreationIntentFactory(Context context, int userId, int slot) {
        Intent intent = new Intent(context, CharacterCreationActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        intent.putExtra(COMP_DOOM_ACTIVITY_SLOT, slot);
        return intent;
    }
}