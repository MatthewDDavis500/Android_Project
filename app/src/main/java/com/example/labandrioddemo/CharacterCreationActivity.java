package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.databinding.ActivityCharacterCreationBinding;

public class CharacterCreationActivity extends AppCompatActivity {
    private ActivityCharacterCreationBinding creationBinding;
    private AccountRepository repository;
    private static int userId;
    private static int slot;
    public static final String COMP_DOOM_ACTIVITY_USER_ID = "com.example.labandrioddemo.COMP_DOOM_ACTIVITY_USER_ID";
    public static final String COMP_DOOM_ACTIVITY_SLOT = "com.example.labandrioddemo.COMP_DOOM_ACTIVITY_SLOT";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creationBinding = ActivityCharacterCreationBinding.inflate(getLayoutInflater());
        setContentView(creationBinding.getRoot());
        repository = AccountRepository.getRepository(getApplication());
        userId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, -1);
        slot = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_SLOT, -1);

        creationBinding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = creationBinding.nameEnterEditText.getText().toString();
                ProjectCharacter character = new ProjectCharacter(name, userId, 1, 0, 10, 10, 1, 5,
                1, slot);

                repository.insertCharacter(character);

                // Steven's genius
                finish();
            }
        });
    }
    static Intent characterCreationIntentFactory(Context context, int userId, int slot) {
        Intent intent = new Intent(context, CharacterCreationActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        intent.putExtra(COMP_DOOM_ACTIVITY_SLOT, slot);
        return intent;
    }
}