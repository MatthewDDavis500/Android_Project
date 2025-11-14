package com.example.labandrioddemo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.labandrioddemo.database.entities.ProjectCharacter;

import java.util.List;

public interface CharacterDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProjectCharacter... character);

    @Delete
    void delete(ProjectCharacter character);

    @Query("SELECT * FROM " + AccountDatabase.CHARACTER_TABLE + " ORDER BY username")
    LiveData<List<ProjectCharacter>> getAllUsers();

    @Query("DELETE from " + AccountDatabase.CHARACTER_TABLE) void deleteAll();

    @Query("SELECT * from " + AccountDatabase.CHARACTER_TABLE + " WHERE username == :username")
    LiveData<ProjectCharacter> getCharacterByUserName(String username);
}
