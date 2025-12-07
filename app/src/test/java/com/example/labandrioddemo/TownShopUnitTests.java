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
    public void purchaseAttackSuccessful() {
        // test initial values
        assertEquals(TEST_ATTACK_MODIFIER, testCharacter.getAtkMod());
        assertTrue(TownShopActivity.ATTACK_COST <= testCharacter.getGold());

        // purchase attack upgrade
        townShop.buyAttack(testCharacter);

        // test values after purchase
        assertEquals(TEST_ATTACK_MODIFIER + 1, testCharacter.getAtkMod());
        assertEquals(TEST_GOLD - TownShopActivity.ATTACK_COST, testCharacter.getGold());
    }

    @Test
    public void purchaseAttackFailed() {
        // set character gold to be not enough for the purchase
        testCharacter.setGold(TownShopActivity.ATTACK_COST - 1);

        // test initial values
        assertEquals(TEST_ATTACK_MODIFIER, testCharacter.getAtkMod());
        assertFalse(TownShopActivity.ATTACK_COST <= testCharacter.getGold());

        // attempt to purchase attack upgrade
        townShop.buyAttack(testCharacter);

        // test values after purchase (should not have changed)
        assertEquals(TEST_ATTACK_MODIFIER, testCharacter.getAtkMod());
        assertEquals(TownShopActivity.ATTACK_COST - 1, testCharacter.getGold());
    }

    @Test
    public void purchaseHealthSuccessful() {
        // test initial values
        assertEquals(TEST_CURRENT_HP, testCharacter.getCurrHp());
        assertEquals(TEST_MAX_HP, testCharacter.getMaxHp());
        assertTrue(TownShopActivity.HEALTH_COST <= testCharacter.getGold());

        // purchase attack upgrade
        townShop.buyHealth(testCharacter);

        // test values after purchase
        assertEquals(TEST_CURRENT_HP + 5, testCharacter.getCurrHp());
        assertEquals(TEST_MAX_HP + 5, testCharacter.getMaxHp());
        assertEquals(TEST_GOLD - TownShopActivity.HEALTH_COST, testCharacter.getGold());
    }

    @Test
    public void purchaseHealthFailed() {
        // set character gold to be not enough for the purchase
        testCharacter.setGold(TownShopActivity.HEALTH_COST - 1);

        // test initial values
        assertEquals(TEST_CURRENT_HP, testCharacter.getCurrHp());
        assertEquals(TEST_MAX_HP, testCharacter.getMaxHp());
        assertFalse(TownShopActivity.HEALTH_COST <= testCharacter.getGold());

        // attempt to purchase attack upgrade
        townShop.buyAttack(testCharacter);

        // test values after purchase (should not have changed)
        assertEquals(TEST_CURRENT_HP, testCharacter.getCurrHp());
        assertEquals(TEST_MAX_HP, testCharacter.getMaxHp());
        assertEquals(TownShopActivity.HEALTH_COST - 1, testCharacter.getGold());
    }

    @Test
    public void purchaseFleeChanceSuccessful() {
        // test initial values
        assertEquals(TEST_FLEE_CHANCE, testCharacter.getFleeChance());
        assertTrue(TownShopActivity.FLEE_COST <= testCharacter.getGold());

        // purchase attack upgrade
        townShop.buyFleeChance(testCharacter);

        // test values after purchase
        assertEquals(TEST_FLEE_CHANCE + 10, testCharacter.getFleeChance());
        assertEquals(TEST_GOLD - TownShopActivity.FLEE_COST, testCharacter.getGold());
    }

    @Test
    public void purchaseFleeChanceFailed() {
        // set character gold to be not enough for the purchase
        testCharacter.setGold(TownShopActivity.FLEE_COST - 1);

        // test initial values
        assertEquals(TEST_FLEE_CHANCE, testCharacter.getFleeChance());
        assertFalse(TownShopActivity.FLEE_COST <= testCharacter.getGold());

        // attempt to purchase attack upgrade
        townShop.buyFleeChance(testCharacter);

        // test values after purchase (should not have changed)
        assertEquals(TEST_FLEE_CHANCE, testCharacter.getFleeChance());
        assertEquals(TownShopActivity.FLEE_COST - 1, testCharacter.getGold());
    }
}
