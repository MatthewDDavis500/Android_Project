package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityAccountCreationBinding;

public class CharacterSelectActivity extends AppCompatActivity {
    static final String SHARED_PREFERENCE_USERID_KEY = "com.example.labandrioddemo.SHARED_PREFERENCE_USERID_KEY";
    private static final int LOGGED_OUT = -1;
    private static final String CHARACTER_SELECT_ACTIVITY_USER_ID = "com.example.labandrioddemo.MAIN_ACTIVITY_USER_ID";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.labandrioddemo.SAVED_INSTANCE_STATE_USERID_KEY";

    private ActivityAccountCreationBinding binding;
    private AccountRepository repository;

    int loggedInUserId = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);
    }

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

    static Intent characterSelectActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, CharacterSelectActivity.class);
        intent.putExtra(CHARACTER_SELECT_ACTIVITY_USER_ID, userId);
        return intent;
    }
}