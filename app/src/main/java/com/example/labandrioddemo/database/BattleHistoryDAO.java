package com.example.labandrioddemo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.labandrioddemo.database.entities.BattleHistory;

import java.util.List;

@Dao
public interface BattleHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BattleHistory... battleHistory);

    @Delete
    void delete(BattleHistory battleHistory);

    @Query("SELECT * from " + AccountDatabase.BATTLE_HISTORY_TABLE + " WHERE battleNumber == :battleNumber")
    LiveData<BattleHistory> getAllBattleNumbers(int battleNumber);

    @Query("DELETE from " + AccountDatabase.BATTLE_HISTORY_TABLE)
    void deleteAll();
}
