package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterSelectActivity.COMP_DOOM_ACTIVITY_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.databinding.ActivityAdminPowersBinding;

public class AdminPowersActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    private ActivityAdminPowersBinding binding;
    private AccountRepository repository;
    private String searchName = "";
    private int searchId = -1;
    private ProjectCharacter currentCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPowersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AccountRepository.getRepository(getApplication());

        binding.characterSearchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if checked, search is by name. if not checked, search is by ID
                if(binding.characterSearchToggle.isChecked()) {
                    binding.characterSearchBar.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.characterSearchBar.setText("");
                } else {
                    binding.characterSearchBar.setInputType(InputType.TYPE_CLASS_NUMBER);
                    binding.characterSearchBar.setText("");
                }
            }
        });

        binding.adminSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForCharacter();
            }
        });

        binding.adminSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCharacterProperties();
            }
        });

        binding.adminBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CharacterSelectActivity.characterSelectIntentFactory(getApplicationContext(),
                    getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT)
                ));
            }
        });
    }

    /**
     * This method will use the text from the search bar to find a character in the character table
     * <br>
     * If the toggle button is checked, this search will be by characterName
     * If the toggle button is not checked, this search will be by characterId
     * <br>
     * If a character is found, character data is populated into the fields
     * Also updates the currentCharacter variable
     */
    private void searchForCharacter() {
        LiveData<ProjectCharacter> characterLiveData;

        if(!binding.characterSearchToggle.isChecked()) {
            searchId = Integer.parseInt(binding.characterSearchBar.getText().toString());

            // data validation
            if(searchId == -1) {
                makeToast("Please enter a valid ID.");
                return;
            } else if(searchId < 0) {
                makeToast("ID cannot be negative.");
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
                    makeToast("Character found.");

                    // update currentCharacter variable
                    currentCharacter = character;

                    // populate TextViews with uneditable character data
                    binding.currentCharacterNameTextView.setText("Name: " + character.getCharacterName());
                    binding.currentCharacterIdTextView.setText("Character ID: " + character.getCharacterID());
                    binding.currentCharacterUserTextView.setText("User ID: " + character.getUserID());

                    // populate EditTexts with editable data
                    binding.characterGoldEdit.setText(character.getGold() + "");
                    binding.characterLevelEdit.setText(character.getLvl() + "");
                    binding.characterCurrentHealthEdit.setText(character.getCurrHp() + "");
                    binding.characterMaxHealthEdit.setText(character.getMaxHp() + "");

                    characterLiveData.removeObserver(this);
                } else {
                    makeToast("Character does not exist.");
                }
            }
        });
    }

    /**
     * This method will use the fields to update character settings
     * <br>
     * The following situations will result in a toast and doing nothing else:
     *     No character selected (currentCharacter is null)
     *     Gold EditText is empty
     *     Level EditText is empty
     *     Current Health EditText is empty
     *     Max Health EditText is empty
     * <br>
     * Otherwise, update the matching properties of the character and update the character in the database
     */
    private void updateCharacterProperties() {
        // check to make sure there is a character selected to edit
        if(currentCharacter == null) {
            makeToast("Please search for a character to edit.");
            return;
        }

        // validate data
        if(binding.characterGoldEdit.getText().toString().isEmpty()) {
            makeToast("Gold may not be empty.");
            return;
        } else if(binding.characterLevelEdit.getText().toString().isEmpty()) {
            makeToast("Level may not be empty.");
            return;
        } else if(binding.characterCurrentHealthEdit.getText().toString().isEmpty()) {
            makeToast("Current HP may not be empty.");
            return;
        } else if(binding.characterMaxHealthEdit.getText().toString().isEmpty()) {
            makeToast("Max HP may not be empty.");
            return;
        }

        // update character properties
        currentCharacter.setGold(Integer.parseInt(binding.characterGoldEdit.getText().toString()));
        currentCharacter.setLvl(Integer.parseInt(binding.characterLevelEdit.getText().toString()));
        currentCharacter.setCurrHp(Integer.parseInt(binding.characterCurrentHealthEdit.getText().toString()));
        currentCharacter.setMaxHp(Integer.parseInt(binding.characterMaxHealthEdit.getText().toString()));
        repository.updateCharacter(currentCharacter);

        makeToast("Character updated successfully!");
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent adminPowersIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, AdminPowersActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        return intent;
    }
}