package com.example.labandrioddemo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.labandrioddemo.database.entities.ProjectCharacter;

import java.util.List;
@Dao
public interface CharacterDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProjectCharacter... character);

    @Delete
    void delete(ProjectCharacter character);

    @Query("SELECT * FROM " + AccountDatabase.CHARACTER_TABLE + " ORDER BY characterName")
    LiveData<List<ProjectCharacter>> getAllCharacters();

    @Query("DELETE from " + AccountDatabase.CHARACTER_TABLE) void deleteAll();

    @Query("SELECT * from " + AccountDatabase.CHARACTER_TABLE + " WHERE characterName == :characterName")
    LiveData<ProjectCharacter> getAllCharacterByName(String characterName);

    // TODO: Edit this to search via slot number as well. This is not complete as is.
    @Query("SELECT * from " + AccountDatabase.CHARACTER_TABLE + " WHERE userID == :userId")
    LiveData<ProjectCharacter> getCharacterByUserId(int userId);
}
