package com.example.labandrioddemo;

import com.example.labandrioddemo.database.entities.ProjectCharacter;

public class TownScreen {
    public static final int REST_COST = 10;

    /**
     * Attempts to purchase a rest to replenish the character's health
     * @param character Character that is purchasing the rest
     * @return
     * NOT_ENOUGH_GOLD if the character doesn't have enough gold <br>
     * ALREADY_MAX_HEALTH if the character's health is already full <br>
     * SUCCESS otherwise
     */
    public int attemptRest(ProjectCharacter character) {
        if(character.getGold() < REST_COST) {
            return TownScreenActivity.NOT_ENOUGH_GOLD;
        } else if(character.getCurrHp() == character.getMaxHp()) {
            return TownScreenActivity.ALREADY_MAX_HEALTH;
        } else {
            character.setGold(character.getGold() - REST_COST);

            int currentHp = character.getCurrHp();
            currentHp += character.getMaxHp() / 4;
            if(currentHp > character.getMaxHp()) {
                currentHp = character.getMaxHp();
            }
            character.setCurrHp(currentHp);

            return TownScreenActivity.SUCCESS;
        }
    }
}
