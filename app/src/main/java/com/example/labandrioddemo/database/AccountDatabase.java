package com.example.labandrioddemo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.labandrioddemo.database.entities.User;

@Database(entities = {User.class}, version = 0, exportSchema = false)
public abstract class AccountDatabase extends RoomDatabase {
}
