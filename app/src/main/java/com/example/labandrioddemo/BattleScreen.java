package com.example.labandrioddemo;

import com.example.labandrioddemo.database.entities.ProjectCharacter;

import kotlin.random.Random;

public class BattleScreen {

    private Random random = Random.Default;
    private int monsterMaxHp;
    private int monsterCurHp;

    private int startingHp;

    /**
     * Attempts to battle a monster
     * @param character Character that is fighting
     * @return what happened:
     * IS_DEAD if the character doesn't have enough hp <br>
     * HAS_WON if the monsters's health is empty <br>
     * SUCCESS otherwise
     */
    public int attemptAttack(ProjectCharacter character) {
        if (monsterCurHp > 0) {
            character.setCurrHp(character.getCurrHp() - random.nextInt(character.getBattleNum() - 1,character.getBattleNum() + 3));
            monsterCurHp = monsterCurHp - (random.nextInt(3,6) + character.getAtkMod());
        }
        if (character.getCurrHp() <= 0) {
            return BattleScreenActivity.IS_DEAD;
        } else if (monsterCurHp <= 0) {
            character.setBattleNum(character.getBattleNum() + 1);
            return BattleScreenActivity.HAS_WON;
        }
        return BattleScreenActivity.SUCCESS;
    }

    /**
     * Sets the max hp and current hp of the monster
     * @param character for the battle number
     * @return the max hp
     */
    public int initializeMonster(ProjectCharacter character) {
        monsterMaxHp = (character.getBattleNum()- 1) * 2 + 10;
        monsterCurHp = monsterMaxHp;
        return monsterMaxHp;
    }
}
