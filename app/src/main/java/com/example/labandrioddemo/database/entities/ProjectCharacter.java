package com.example.labandrioddemo.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.labandrioddemo.database.AccountDatabase;

import java.util.Objects;

@Entity(tableName = AccountDatabase.USER_TABLE)
public class ProjectCharacter {
    @PrimaryKey(autoGenerate = true)
    private String username;

    public ProjectCharacter(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String setUsername() {
        this.username = username;
    }
}
