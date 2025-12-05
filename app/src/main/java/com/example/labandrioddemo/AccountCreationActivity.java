package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityAccountCreationBinding;

public class AccountCreationActivity extends AppCompatActivity {
    private ActivityAccountCreationBinding binding;
    private AccountRepository repository;
    private boolean nullVerificationDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AccountRepository.getRepository(getApplication());

        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * This method attempts to create a new user using the info entered on the Account Creation Activity.
     * If the username is empty/taken or the password doesn't match the confirmation, an error message is displayed.
     * Otherwise, a new User object is created with the entered info and added to the database.
     */
    public void createUser() {
        String username = binding.userNameEnterEditText.getText().toString();

        // Validate that username isn't empty
        if(username.isEmpty()) {
            makeToast("Username may not be empty.");
            return;
        }

        // Validate password
        String password = binding.passwordCreateEditText.getText().toString();
        if(password.isEmpty()) {
            makeToast("Password may not be empty.");
            return;
        }
        if(!password.equals(binding.passwordConfirmEditText.getText().toString())) {
            makeToast("Passwords don't match!");
            return;
        }

        AccountCreationActivity thisHolder = this; // this is so that the Toast can be made in the observer block

        // Validate that username is available
        LiveData<User> userObserver = repository.getUserByUsername(username);
        userObserver.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null) { // username is available
                    // Add new user to database
                    User newUser = new User(username, password);
                    if(binding.adminCodeEditText.getText().toString().equals(getString(R.string.adminCode))) {
                        newUser.setAdmin(true);
                    }
                    repository.insertUser(newUser);

                    nullVerificationDone = false;
                    userObserver.removeObserver(this);

                    // Send user to MainActivity to sign in
                    startActivity(MainActivity.mainIntentFactory(getApplicationContext()));
                } else {
                    // if this is the first null received, the query may not be finished yet so don't do anything
                    // else state that username already exists
                    if(nullVerificationDone) {
                        makeToast("Username already exists.");
                        nullVerificationDone = false;

                        userObserver.removeObserver(this);
                    } else {
                        nullVerificationDone = true;
                    }
                }
            }
        });
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent accountCreationIntentFactory(Context context) {
        return new Intent(context, AccountCreationActivity.class);
    }
}