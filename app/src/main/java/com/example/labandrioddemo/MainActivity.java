package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "COMP_DOOM";

    private ActivityMainBinding binding;
    private AccountRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AccountRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }
        });

        binding.sighUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AccountCreationActivity.accountCreationActivityIntentFactory(getApplicationContext()));
            }
        });
    }

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
                    invalidateOptionsMenu();
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

    static Intent mainIntentFactory(Context context) {
        return new Intent(context, MainActivity.class);
    }
}