package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityCharacterSelectBinding;

public class CharacterSelectActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    public static final String COMP_DOOM_ACTIVITY_USER_ID = "com.example.labandrioddemo.COMP_DOOM_ACTIVITY_USER_ID";

    private ActivityCharacterSelectBinding binding;
    private AccountRepository repository;

    int loggedInUserId = -1;
    private User user;

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
        userLiveData.observe(this, user -> {
            this.user = user;

            // update the admin tag if the user is an admin
            if(user != null && user.isAdmin()) {
                // Use a string resource instead of a literal
                binding.adminConfirmation.setText(getString(R.string.admin_true));
            } else {
                binding.adminConfirmation.setText(getString(R.string.admin_false));
            }
        });

        // get the ProjectCharacter object corresponding to the logged in user
        LiveData<ProjectCharacter> characterLiveData = repository.getCharacterByUserId(loggedInUserId);
        characterLiveData.observe(this, character -> {
            if(character != null) {
                binding.character1Button.setText(character.getCharacterName());
            } else {
                binding.character1Button.setText(getString(R.string.create_character));
            }
        });

        binding.character1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainMenuActivity.mainMenuIntentFactory(getApplicationContext(), 1, 1));
            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void logout() {
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT);
        startActivity(MainActivity.mainIntentFactory(getApplicationContext()));
    }

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