package com.example.labandrioddemo;

import com.example.labandrioddemo.database.entities.ProjectCharacter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TownShopUnitTests {
    private TownShopActivity townShop;
    private ProjectCharacter testCharacter;
    private final int TEST_USERID = 42;
    private final int TEST_LEVEL = 2;
    private final int TEST_GOLD = 30;
    private final int TEST_CURRENT_HP = 80;
    private final int TEST_MAX_HP = 100;
    private final int TEST_ATTACK_MODIFIER = 2;
    private final int TEST_FLEE_CHANCE = 20;
    private final int TEST_BATTLE_NUMBER = 3;
    private final int TEST_SLOT = 1;


    @Before
    public void initializeValues() {
        townShop = new TownShopActivity();
        testCharacter = new ProjectCharacter("TestingBoi", TEST_USERID, TEST_LEVEL,
                TEST_GOLD, TEST_CURRENT_HP, TEST_MAX_HP, TEST_ATTACK_MODIFIER, TEST_FLEE_CHANCE,
                TEST_BATTLE_NUMBER, TEST_SLOT);
    }

    @After
    public void cleanupValues() {
        townShop = null;
        testCharacter = null;
    }

    @Test
    public void purchasingAttackSuccessful() {
        // test initial values
        assertEquals(TEST_ATTACK_MODIFIER, testCharacter.getAtkMod());
        assertEquals(TEST_GOLD, testCharacter.getGold());

        // purchase attack upgrade
        townShop.buyAttack(testCharacter);

        // test values after purchase
        assertEquals(TEST_ATTACK_MODIFIER + 1, testCharacter.getAtkMod());
        assertEquals(TEST_GOLD - TownShopActivity.ATTACK_COST, testCharacter.getGold());
    }
}
