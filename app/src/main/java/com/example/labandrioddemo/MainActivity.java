package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterSelectActivity.COMP_DOOM_ACTIVITY_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

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
    private boolean loginNullVerificationDone = false; // this variable is used for the asynchronous calls in login and verifyUser. The update function is immediately called with a null value, this variable is set to true, and if the update is called again with a null value, then the username is invalid
    private boolean verifyNullVerificationDone = false; // same as above, but for verify method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // instantiate repository to allow for database access
        repository = AccountRepository.getRepository(getApplication());

        // attempt to login user based on previously stored info (like shared preferences and saved instance states)
        loginUser(savedInstanceState);

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
                startActivity(AccountCreationActivity.accountCreationIntentFactory(getApplicationContext()));
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
            loggedInUserId = getIntent().getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT) {
            return;
        }

        LiveData<User> userLoginLiveData = repository.getUserByUserId(loggedInUserId);
        userLoginLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null) {
                    userLoginLiveData.removeObserver(this);
                    loginNullVerificationDone = false;
                    Intent intent = CharacterSelectActivity.characterSelectIntentFactory(getApplicationContext(), loggedInUserId);
                    startActivity(intent);
                } else {
                    if(loginNullVerificationDone) {
                        loggedInUserId = LOGGED_OUT;
                        updateSharedPreference();
                        loginNullVerificationDone = false;
                    } else {
                        loginNullVerificationDone = true;
                    }

                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        updateSharedPreference();
    }

    /**
     * This method updates the sharedPreferences, which is used for login persistence
     */
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

        LiveData<User> userVerifyLiveData = repository.getUserByUsername(username);
        userVerifyLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null) {
                    userVerifyLiveData.removeObserver(this);
                    String password = binding.passwordLoginEditText.getText().toString();

                    if(password.equals(user.getPassword())) {
//                        invalidateOptionsMenu();
                        loggedInUserId = user.getId();
                        updateSharedPreference();
                        verifyNullVerificationDone = false;
                        startActivity(CharacterSelectActivity.characterSelectIntentFactory(getApplicationContext(), user.getId())); //THE ERROR IS HERE
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(verifyNullVerificationDone) {
                        Toast.makeText(MainActivity.this, username + " is not a valid username.", Toast.LENGTH_SHORT).show();
                        verifyNullVerificationDone = false;
                    } else {
                        verifyNullVerificationDone = true;
                    }
                }
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