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
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityAdminPowersBinding;

public class AdminPowersActivity extends AppCompatActivity {
    private ActivityAdminPowersBinding binding;
    private AccountRepository repository;
    private String searchName = "";
    private int searchId = -1;
    private boolean nullVerificationDone = false;
    private ProjectCharacter currentCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPowersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AccountRepository.getRepository(getApplication());

        binding.adminSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveData<ProjectCharacter> characterLiveData;

                if(!binding.characterSearchToggle.isChecked()) {
                    searchId = Integer.parseInt(binding.characterSearchBar.getText().toString());

                    // data validation
                    if(searchId == -1) {
                        Toast.makeText(AdminPowersActivity.this, "Please enter a valid ID.", Toast.LENGTH_SHORT).show();
                        return;
                    } else if(searchId < 0) {
                        Toast.makeText(AdminPowersActivity.this, "ID cannot be negative.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    characterLiveData = repository.getCharacterByCharacterId(searchId);
                } else if(binding.characterSearchToggle.isChecked()) {
                    searchName = binding.characterSearchBar.getText().toString();

                    characterLiveData = repository.getCharacterByName(searchName);
                } else {
                    return;
                }

                characterLiveData.observe(AdminPowersActivity.this, new Observer<ProjectCharacter>() {
                    @Override
                    public void onChanged(ProjectCharacter character) {
                        if(character != null) {
                            binding.currentCharacterNameTextView.setText("Name: " + character.getCharacterName());
                            binding.currentCharacterIdTextView.setText("Character ID: " + character.getCharacterID());
                            binding.currentCharacterUserTextView.setText("User ID: " + character.getUserID());

                            binding.characterGoldEdit.setText(character.getGold() + "");
                            binding.characterLevelEdit.setText(character.getLvl() + "");
                            binding.characterCurrentHealthEdit.setText(character.getCurrHp() + "");
                            binding.characterMaxHealthEdit.setText(character.getMaxHp() + "");
                        } else {
                            if(nullVerificationDone) {
                                Toast.makeText(AdminPowersActivity.this, "Character does not exist.", Toast.LENGTH_SHORT).show();
                                nullVerificationDone = false;
                            } else {
                                nullVerificationDone = true;
                            }
                        }
                    }
                });
            }
        });

        binding.adminSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check to make sure there is a character selected to edit
                if(currentCharacter == null) {
                    Toast.makeText(AdminPowersActivity.this, "Please search for a character to edit", Toast.LENGTH_SHORT).show();
                    return;
                }

                // validate data
                if(Integer.parseInt(binding.characterGoldEdit.getText().toString()) < 0) {
                    Toast.makeText(AdminPowersActivity.this, "Gold may not be negative.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(Integer.parseInt(binding.characterLevelEdit.getText().toString()) < 0) {
                    Toast.makeText(AdminPowersActivity.this, "Level may not be negative.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(Integer.parseInt(binding.characterCurrentHealthEdit.getText().toString()) < 0) {
                    Toast.makeText(AdminPowersActivity.this, "Current HP may not be negative.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(Integer.parseInt(binding.characterMaxHealthEdit.getText().toString()) < 0) {
                    Toast.makeText(AdminPowersActivity.this, "Max HP may not be negative.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    static Intent adminPowersIntentFactory(Context context, int userId, int characterId) {
        Intent intent = new Intent(context, AdminPowersActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}