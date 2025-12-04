package com.example.labandrioddemo.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.labandrioddemo.database.AccountDatabase;

import java.util.Objects;

@Entity(tableName = AccountDatabase.BATTLE_HISTORY_TABLE)
public class BattleHistory {
    @PrimaryKey(autoGenerate = true)
    private int recordKey;
    private int characterId;
    private int battleNumber;
    private int remainingHp;
    private boolean isWin;

    public BattleHistory(int battleNumber, int remainingHp, boolean isWin) {
        this.battleNumber = battleNumber;
        this.remainingHp = remainingHp;
        this.isWin = isWin;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getBattleNumber() {
        return battleNumber;
    }

    public void setBattleNumber(int battleNumber) {
        this.battleNumber = battleNumber;
    }

    public int getRecordKey() {
        return recordKey;
    }

    public void setRecordKey(int recordKey) {
        this.recordKey = recordKey;
    }

    public int getRemainingHp() {
        return remainingHp;
    }

    public void setRemainingHp(int remainingHp) {
        this.remainingHp = remainingHp;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BattleHistory that = (BattleHistory) o;
        return battleNumber == that.battleNumber && recordKey == that.recordKey && remainingHp == that.remainingHp && isWin == that.isWin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(battleNumber, recordKey, remainingHp, isWin);
    }
}
