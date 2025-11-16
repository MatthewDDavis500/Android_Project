package com.example.labandrioddemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.User;
import com.example.labandrioddemo.databinding.ActivityAccountCreationBinding;

public class CharacterSelectActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    public static final String CHARACTER_SELECT_ACTIVITY_USER_ID = "com.example.labandrioddemo.CHARACTER_SELECT_ACTIVITY_USER_ID";

    private ActivityAccountCreationBinding binding;
    private AccountRepository repository;

    int loggedInUserId = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);
    }



    static Intent characterSelectActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, CharacterSelectActivity.class);
        intent.putExtra(CHARACTER_SELECT_ACTIVITY_USER_ID, userId);
        return intent;
    }
}