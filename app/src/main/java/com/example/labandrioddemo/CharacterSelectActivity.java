package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityCharacterSelectBinding;

public class CharacterSelectActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    public static final String CHARACTER_SELECT_ACTIVITY_USER_ID = "com.example.labandrioddemo.CHARACTER_SELECT_ACTIVITY_USER_ID";

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
        loggedInUserId = getIntent().getIntExtra(CHARACTER_SELECT_ACTIVITY_USER_ID, LOGGED_OUT);

        // update the admin tag if the user is an admin
        if (user != null && user.isAdmin()) {
            // Use a string resource instead of a literal
            binding.adminConfirmation.setText(getString(R.string.admin_true));
        } else {
            binding.adminConfirmation.setText(getString(R.string.admin_false));
        }

        // if the user is somehow still logged out, send them back to MainActivity to login
        if(loggedInUserId == LOGGED_OUT) {
            startActivity(MainActivity.mainIntentFactory(getApplicationContext()));
        }

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void logout() {
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(CHARACTER_SELECT_ACTIVITY_USER_ID, LOGGED_OUT);
        startActivity(MainActivity.mainIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }

    static Intent characterSelectActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, CharacterSelectActivity.class);
        intent.putExtra(CHARACTER_SELECT_ACTIVITY_USER_ID, userId);
        return intent;
    }
}