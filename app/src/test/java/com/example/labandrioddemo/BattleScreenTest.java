package com.example.labandrioddemo;

import static org.junit.Assert.*;

import com.example.labandrioddemo.database.entities.ProjectCharacter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BattleScreenTest {
    private BattleScreen battle;
    private ProjectCharacter testCharacter;
    private final String TEST_NAME = "Testing Dummy";
    private final int TEST_USERID = 42;
    private final int TEST_LEVEL = 2;
    private final int TEST_GOLD = 30;
    private final int TEST_CURRENT_HP = 100;
    private final int TEST_LOW_HP = 1;
    private final int TEST_MAX_HP = 100;
    private final int TEST_ATTACK_MODIFIER = 2;
    private final int TEST_FLEE_CHANCE = 20;
    private final int TEST_BATTLE_NUMBER = 3;
    private final int TEST_SLOT = 1;


    @Before
    public void initializeValues() {
        battle = new BattleScreen();
        testCharacter = new ProjectCharacter(TEST_NAME, TEST_USERID, TEST_LEVEL,
                TEST_GOLD, TEST_CURRENT_HP, TEST_MAX_HP, TEST_ATTACK_MODIFIER, TEST_FLEE_CHANCE,
                TEST_BATTLE_NUMBER, TEST_SLOT);
        battle.initializeMonster(testCharacter);
    }

    @After
    public void cleanupValues() {
        battle = null;
        testCharacter = null;
    }

    @Test
    public void attackAtFull() {
        // set character health to be 100
        testCharacter.setCurrHp(TEST_MAX_HP);

        // test initial values
        assertEquals(TEST_MAX_HP, testCharacter.getCurrHp());

        // purchase rest
        assertEquals(BattleScreenActivity.SUCCESS, battle.attemptAttack(testCharacter));

        // test values after purchase
        assertNotEquals(100, testCharacter.getCurrHp());
    }

    @Test
    public void attackAtLow() {
        // set character health to 1
        testCharacter.setCurrHp(TEST_LOW_HP);

        assertEquals(1, testCharacter.getCurrHp());

        // test initial values
        assertEquals(BattleScreenActivity.IS_DEAD, battle.attemptAttack(testCharacter));
    }

    @Test
    public void beatMonster() {
        for (int i = 0; i < 20; i++) {
            battle.attemptAttack(testCharacter);
        }

        assertEquals(BattleScreenActivity.HAS_WON, battle.attemptAttack(testCharacter));
    }
}
