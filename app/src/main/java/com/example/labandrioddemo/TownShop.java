package com.example.labandrioddemo;

import com.example.labandrioddemo.database.entities.ProjectCharacter;

public class TownShop {
    public static final int ATTACK_COST = 20;
    public static final int HEALTH_COST = 20;
    public static final int FLEE_COST = 10;

    /**
     * Attempt to purchase and apply an attack upgrade
     * @param character ProjectCharacter that's purchasing the upgrade
     * @return
     * NOT_ENOUGH_GOLD if the character doesn't have enough gold <br>
     * SUCCESS otherwise
     */
    public int purchaseAttackUpgrade(ProjectCharacter character) {
        if(character.getGold() < ATTACK_COST) {
            return TownShopActivity.NOT_ENOUGH_GOLD;
        } else {
            character.setGold(character.getGold() - ATTACK_COST);
            character.setAtkMod(character.getAtkMod() + 1);

            return TownShopActivity.SUCCESS;
        }
    }

    /**
     * Attempt to purchase and apply a health upgrade
     * @param character ProjectCharacter that's purchasing the upgrade
     * @return
     * NOT_ENOUGH_GOLD if the character doesn't have enough gold <br>
     * SUCCESS otherwise
     */
    public int purchaseHealthUpgrade(ProjectCharacter character) {
        if(character.getGold() < HEALTH_COST) {
            return TownShopActivity.NOT_ENOUGH_GOLD;
        } else {
            character.setGold(character.getGold() - HEALTH_COST);

            character.setMaxHp(character.getMaxHp() + 5);
            character.setCurrHp(character.getCurrHp() + 5);

            return TownShopActivity.SUCCESS;
        }
    }

    /**
     * Attempt to purchase and apply a flee chance upgrade
     * @param character ProjectCharacter that's purchasing the upgrade
     * @return
     * NOT_ENOUGH_GOLD if the character doesn't have enough gold <br>
     * ALREADY_MAX_FLEE if the character's flee chance is already 100% <br>
     * SUCCESS otherwise
     */
    public int purchaseFleeChanceUpgrade(ProjectCharacter character) {
        if(character.getFleeChance() >= 100) {
            return TownShopActivity.ALREADY_MAX_FLEE;
        } else if(character.getGold() < FLEE_COST) {
            return TownShopActivity.NOT_ENOUGH_GOLD;
        } else {
            character.setGold(character.getGold() - FLEE_COST);
            character.setFleeChance(character.getFleeChance() + 10);

            return TownShopActivity.SUCCESS;
        }
    }
}
