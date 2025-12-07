package com.example.labandrioddemo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.labandrioddemo.database.entities.BattleHistory;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.database.entities.User;

import java.util.List;

@Dao
public interface BattleHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BattleHistory... battleHistory);

    @Delete
    void delete(BattleHistory battleHistory);

    @Query("SELECT * from " + AccountDatabase.BATTLE_HISTORY_TABLE + " WHERE characterId == :characterId ORDER BY battleNumber AND recordKey")
    LiveData<List<BattleHistory>> getBattleByCharacterId(int characterId);

    @Query("SELECT * from " + AccountDatabase.BATTLE_HISTORY_TABLE + " WHERE battleNumber == :battleNumber")
    BattleHistory getUserByBattleNumberWithoutLiveData(int battleNumber);

    @Query("DELETE from " + AccountDatabase.BATTLE_HISTORY_TABLE)
    void deleteAll();

    @Query("DELETE from " + AccountDatabase.BATTLE_HISTORY_TABLE + " WHERE characterId == :characterId")
    void deleteByCharacterId(int characterId);

    @Update
    void updateBattle (BattleHistory battleHistory);
}
