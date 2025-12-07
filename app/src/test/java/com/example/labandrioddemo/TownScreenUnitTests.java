package com.example.labandrioddemo;

import static org.junit.Assert.*;

import com.example.labandrioddemo.database.entities.ProjectCharacter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TownScreenUnitTests {
    private TownScreen town;
    private ProjectCharacter testCharacter;
    private final String TEST_NAME = "Testing Dummy";
    private final int TEST_USERID = 42;
    private final int TEST_LEVEL = 2;
    private final int TEST_GOLD = 30;
    private final int TEST_CURRENT_HP = 80;
    private final int TEST_LOWER_HP = 60;
    private final int TEST_HIGHER_HP = 90;
    private final int TEST_MAX_HP = 100;
    private final int TEST_ATTACK_MODIFIER = 2;
    private final int TEST_FLEE_CHANCE = 20;
    private final int TEST_BATTLE_NUMBER = 3;
    private final int TEST_SLOT = 1;


    @Before
    public void initializeValues() {
        town = new TownScreen();
        testCharacter = new ProjectCharacter(TEST_NAME, TEST_USERID, TEST_LEVEL,
                TEST_GOLD, TEST_CURRENT_HP, TEST_MAX_HP, TEST_ATTACK_MODIFIER, TEST_FLEE_CHANCE,
                TEST_BATTLE_NUMBER, TEST_SLOT);
    }

    @After
    public void cleanupValues() {
        town = null;
        testCharacter = null;
    }

    @Test
    public void purchaseRestSuccessfulNotToFullHealth() {
        // set character health to be less than 75 (so that heal doesn't fill all the way)
        testCharacter.setCurrHp(TEST_LOWER_HP);

        // test initial values
        assertEquals(TEST_LOWER_HP, testCharacter.getCurrHp());
        assertTrue(TownScreen.REST_COST <= testCharacter.getGold());

        // purchase attack upgrade
        assertEquals(TownScreenActivity.SUCCESS, town.attemptRest(testCharacter));

        // test values after purchase
        assertEquals(TEST_LOWER_HP + 25, testCharacter.getCurrHp());
        assertEquals(TEST_GOLD - TownScreen.REST_COST, testCharacter.getGold());
    }

    @Test
    public void purchaseRestSuccessfulToOverFullHealth() {
        // set character health to be more than 75 (so that heal would fill over max hp)
        testCharacter.setCurrHp(TEST_HIGHER_HP);

        // test initial values
        assertEquals(TEST_HIGHER_HP, testCharacter.getCurrHp());
        assertTrue(TownScreen.REST_COST <= testCharacter.getGold());

        // purchase attack upgrade
        assertEquals(TownScreenActivity.SUCCESS, town.attemptRest(testCharacter));

        // test values after purchase (should cap at max hp)
        assertEquals(TEST_MAX_HP, testCharacter.getCurrHp());
        assertEquals(TEST_GOLD - TownScreen.REST_COST, testCharacter.getGold());
    }

    @Test
    public void purchaseRestFailedInsufficientGold() {
        // set character gold to be not enough for the purchase
        testCharacter.setGold(TownScreen.REST_COST - 1);

        // test initial values
        assertEquals(TEST_CURRENT_HP, testCharacter.getCurrHp());
        assertFalse(TownScreen.REST_COST <= testCharacter.getGold());

        // purchase attack upgrade
        assertEquals(TownScreenActivity.NOT_ENOUGH_GOLD, town.attemptRest(testCharacter));

        // test values after purchase (shouldn't have changed)
        assertEquals(TEST_CURRENT_HP, testCharacter.getCurrHp());
        assertEquals(TownScreen.REST_COST - 1, testCharacter.getGold());
    }

    @Test
    public void purchaseRestFailedAlreadyMaxHealth() {
        // set character health to be at maximum
        testCharacter.setCurrHp(TEST_MAX_HP);

        // test initial values
        assertEquals(TEST_MAX_HP, testCharacter.getCurrHp());
        assertTrue(TownScreen.REST_COST <= testCharacter.getGold());

        // purchase attack upgrade
        assertEquals(TownScreenActivity.ALREADY_MAX_HEALTH, town.attemptRest(testCharacter));

        // test values after purchase (shouldn't have changed)
        assertEquals(TEST_MAX_HP, testCharacter.getCurrHp());
        assertEquals(TEST_GOLD, testCharacter.getGold());
    }
}
