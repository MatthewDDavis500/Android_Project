package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityCharacterSelectBinding;

public class CharacterSelectActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    private static final int SLOT_NUMBER_ONE = 1;
    private static final int SLOT_NUMBER_TWO = 2;
    private static final int SLOT_NUMBER_THREE = 3;
    public static final String COMP_DOOM_ACTIVITY_USER_ID = "com.example.labandrioddemo.COMP_DOOM_ACTIVITY_USER_ID";

    private ActivityCharacterSelectBinding binding;
    private AccountRepository repository;
    int loggedInUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCharacterSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // instantiate repository to allow for database access
        repository = AccountRepository.getRepository(getApplication());

        // update loggedInUserId using the intent extra
        loggedInUserId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT);

        // if the user is somehow still logged out, send them back to MainActivity to login
        if(loggedInUserId == LOGGED_OUT) {
            startActivity(MainActivity.mainIntentFactory(getApplicationContext()));
        }

        // get the User object of the logged in user
        LiveData<User> userLiveData = repository.getUserByUserId(loggedInUserId);
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    // show admin tag and admin powers button if user is an admin
                    if (user.isAdmin()) {
                        binding.adminConfirmation.setText(getString(R.string.admin_true));
                        binding.adminPowersButton.setVisibility(View.VISIBLE);
                        userLiveData.removeObserver(this);
                    } else {
                        binding.adminConfirmation.setText(getString(R.string.admin_false));
                        binding.adminPowersButton.setVisibility(View.INVISIBLE);
                        userLiveData.removeObserver(this);
                    }
                }
            }
        });

        // get the ProjectCharacter object corresponding to the logged in user
        LiveData<ProjectCharacter> characterLiveData1 = repository.getCharacterByUserIdAndSlot(loggedInUserId, SLOT_NUMBER_ONE);
        characterLiveData1.observe(this, new Observer<ProjectCharacter>() {
            @Override
            public void onChanged(ProjectCharacter character) {
                // if character is found for slot one, button will select that character. Otherwise, button will be character create.
                if(character != null) {
                    binding.character1Button.setText(character.getCharacterName());
                    binding.character1Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(MainMenuActivity.mainMenuIntentFactory(getApplicationContext(),
                                    loggedInUserId,
                                    character.getCharacterID()
                            ));
                        }
                    });

                    characterLiveData1.removeObserver(this);
                } else {
                    binding.character1Button.setText(getString(R.string.create_character));
                    binding.character1Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(CharacterCreationActivity.characterCreationIntentFactory(getApplicationContext(), loggedInUserId, SLOT_NUMBER_ONE));
                        }
                    });
                }
            }
        });

        // get the ProjectCharacter object corresponding to the logged in user
        LiveData<ProjectCharacter> characterLiveData2 = repository.getCharacterByUserIdAndSlot(loggedInUserId, SLOT_NUMBER_TWO);
        characterLiveData2.observe(this, new Observer<ProjectCharacter>() {
            @Override
            public void onChanged(ProjectCharacter character) {
                // if character is found for slot two, button will select that character. Otherwise, button will be character create.
                if (character != null) {
                    binding.character2Button.setText(character.getCharacterName());
                    binding.character2Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(MainMenuActivity.mainMenuIntentFactory(getApplicationContext(),
                                    loggedInUserId,
                                    character.getCharacterID()
                            ));
                        }
                    });

                    characterLiveData2.removeObserver(this);
                } else {
                    binding.character2Button.setText(getString(R.string.create_character));
                    binding.character2Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(CharacterCreationActivity.characterCreationIntentFactory(getApplicationContext(), loggedInUserId, SLOT_NUMBER_TWO));
                        }
                    });
                }
            }
        });

        // get the ProjectCharacter object corresponding to the logged in user
        LiveData<ProjectCharacter> characterLiveData3 = repository.getCharacterByUserIdAndSlot(loggedInUserId, SLOT_NUMBER_THREE);
        characterLiveData3.observe(this, new Observer<ProjectCharacter>() {
            @Override
            public void onChanged(ProjectCharacter character) {
                // if character is found for slot three, button will select that character. Otherwise, button will be character create.
                if (character != null) {
                    binding.character3Button.setText(character.getCharacterName());
                    binding.character3Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(MainMenuActivity.mainMenuIntentFactory(getApplicationContext(),
                                    loggedInUserId,
                                    character.getCharacterID()
                            ));
                        }
                    });

                    characterLiveData3.removeObserver(this);
                } else {
                    binding.character3Button.setText(getString(R.string.create_character));
                    binding.character3Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(CharacterCreationActivity.characterCreationIntentFactory(getApplicationContext(), loggedInUserId, SLOT_NUMBER_THREE));
                        }
                    });
                }
            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        binding.adminPowersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AdminPowersActivity.adminPowersIntentFactory(getApplicationContext(), loggedInUserId));
            }
        });
    }

    /**
     * This method logs the user out of the application.
     * This involves updating the loggedInUserId, shared preferences, and the intent extra.
     * Additionally, the user is moved to the Main Activity, which is the login page.
     */
    private void logout() {
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT);
        startActivity(MainActivity.mainIntentFactory(getApplicationContext()));
    }

    /**
     * This method updates the user's shared preferences.
     * Updated shared preferences allows the app to remember user login information.
     */
    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }

    static Intent characterSelectIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, CharacterSelectActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_USER_ID, userId);
        return intent;
    }
}