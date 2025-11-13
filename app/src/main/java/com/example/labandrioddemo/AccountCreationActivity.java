package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityAccountCreationBinding;

public class AccountCreationActivity extends AppCompatActivity {
    private ActivityAccountCreationBinding binding;
    private AccountRepository repository;

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
    }

    /**
     * This method attempts to create a new user using the info entered on the Account Creation Activity.
     * If the username is empty/taken or the password doesn't match the confirmation, an error message is displayed.
     * Otherwise, a new User object is created with the entered info and added to the database.
     */
    public void createUser() {
        // Validate username
        String username = binding.userNameEnterEditText.getText().toString();
        if(username.isEmpty()) {
            Toast.makeText(this, "Username may not be empty.", Toast.LENGTH_SHORT).show();
            return;
        } else if(repository.getUserByUsername(username) != null) {
            Toast.makeText(this, "Username already exists.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password
        String password = binding.passwordCreateEditText.getText().toString();
        if(!password.equals(binding.passwordConfirmEditText.getText().toString())) {
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add new user to the database
        User newUser = new User(username, password);
        repository.insertUser(newUser);

        // Send user to MainActivity to sign in
        startActivity(MainActivity.mainIntentFactory(getApplicationContext()));
    }

    static Intent accountCreationActivityIntentFactory(Context context) {
        return new Intent(context, AccountCreationActivity.class);
    }
}