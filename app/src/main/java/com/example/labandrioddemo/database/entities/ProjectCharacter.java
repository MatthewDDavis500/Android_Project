package com.example.labandrioddemo.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.labandrioddemo.database.AccountDatabase;

import java.util.Objects;

@Entity(tableName = AccountDatabase.CHARACTER_TABLE)
public class ProjectCharacter {
    @PrimaryKey(autoGenerate = true)
    private int characterID;
    private String characterName;
    private int userID;
    private int lvl;
    private int gold;
    private int currHp;
    private int maxHp;
    private int atkMod;
    private int fleeChance;
    private int battleNum;


    public ProjectCharacter(String characterName, int userID, int characterID, int lvl, int gold, int currHp, int maxHp, int atkMod, int fleeChance, int battleNum) {
        this.characterName = characterName;
        this.userID = userID;
        this.characterID = characterID;
        this.lvl = lvl;
        this.gold = gold;
        this.currHp = currHp;
        this.maxHp = maxHp;
        this.atkMod = atkMod;
        this.fleeChance = fleeChance;
        this.battleNum = battleNum;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCharacterID() {
        return characterID;
    }

    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getCurrHp() {
        return currHp;
    }

    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getAtkMod() {
        return atkMod;
    }

    public void setAtkMod(int atkMod) {
        this.atkMod = atkMod;
    }

    public int getFleeChance() {
        return fleeChance;
    }

    public void setFleeChance(int fleeChance) {
        this.fleeChance = fleeChance;
    }

    public int getBattleNum() {
        return battleNum;
    }

    public void setBattleNum(int battleNum) {
        this.battleNum = battleNum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProjectCharacter that = (ProjectCharacter) o;
        return userID == that.userID && lvl == that.lvl && gold == that.gold && currHp == that.currHp && maxHp == that.maxHp && atkMod == that.atkMod && fleeChance == that.fleeChance && battleNum == that.battleNum && Objects.equals(characterName, that.characterName) && Objects.equals(characterID, that.characterID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterName, userID, characterID, lvl, gold, currHp, maxHp, atkMod, fleeChance, battleNum);
    }
}
