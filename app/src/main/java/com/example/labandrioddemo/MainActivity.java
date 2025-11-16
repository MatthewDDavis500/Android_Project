package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterSelectActivity.CHARACTER_SELECT_ACTIVITY_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "COMP_DOOM"; // tag for LogCat messages sent by the application
    static final String SHARED_PREFERENCE_USERID_KEY = "com.example.labandrioddemo.SHARED_PREFERENCE_USERID_KEY";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.labandrioddemo.SAVED_INSTANCE_STATE_USERID_KEY";
    private static final int LOGGED_OUT = -1;

    private ActivityMainBinding binding;
    private AccountRepository repository;
    int loggedInUserId = LOGGED_OUT;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // instantiate repository to allow for database access
        repository = AccountRepository.getRepository(getApplication());

        // attempt to login user based on previously stored info (like shared preferences and saved instance states)
        loginUser(savedInstanceState);

        // check to see if user is logged in
        if(loggedInUserId != LOGGED_OUT) {
            Intent intent = CharacterSelectActivity.characterSelectActivityIntentFactory(getApplicationContext(), loggedInUserId);
            startActivity(intent);
        }

        // links the login button to the verifyUser method
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }
        });

        // links the sign up button to the AccountCreationActivity
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AccountCreationActivity.accountCreationActivityIntentFactory(getApplicationContext()));
            }
        });
    }

    /**
     * This method attempts to login the user using information persistence.
     * SharedPreferences and SavedInstanceState are checked for previous login information.
     * If possible, use previous login info to login and start CharacterSelection Activity.
     * Otherwise, do nothing. The user must login or create an account.
     * @param savedInstanceState savedInstanceState to check for login info
     */
    private void loginUser(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

        if(loggedInUserId == LOGGED_OUT && savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)) {
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT) {
            loggedInUserId = getIntent().getIntExtra(CHARACTER_SELECT_ACTIVITY_USER_ID, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT) {
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this, user -> {
            if(user != null) {
//                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        updateSharedPreference();
    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }

    /**
     * This method is the logic for logging in a user.
     * If the username is empty, show a Toast and return.
     * If the username doesn't match any Users in the database, show a Toast and return.
     * If the password doesn't match the password from the database, show a Toast and return.
     * Otherwise, start the CharacterSelectionActivity with the User's userId
     */
    private void verifyUser() {
        String username = binding.userNameLoginEditText.getText().toString();

        if(username.isEmpty()) {
            Toast.makeText(this, "Username may not be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        LiveData<User> userObserver = repository.getUserByUsername(username);
        userObserver.observe(this, user -> {
            if(user != null) {
                String password = binding.passwordLoginEditText.getText().toString();
                if(password.equals(user.getPassword())) {
//                    invalidateOptionsMenu();
                    startActivity(CharacterSelectActivity.characterSelectActivityIntentFactory(getApplicationContext(), user.getId()));
                } else {
                    Toast.makeText(this, "Invalid password.", Toast.LENGTH_SHORT).show();
                    binding.passwordLoginEditText.setSelection(0);
                }
            } else {
                Toast.makeText(this, username + " is not a valid username.", Toast.LENGTH_SHORT).show();
                binding.passwordLoginEditText.setSelection(0);
            }
        });
    }

    /**
     * Intent factory for MainActivity
     * @param context Application context
     * @return an Intent for the MainActivity
     */
    static Intent mainIntentFactory(Context context) {
        return new Intent(context, MainActivity.class);
    }
}