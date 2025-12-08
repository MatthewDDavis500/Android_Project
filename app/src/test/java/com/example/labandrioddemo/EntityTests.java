package com.example.labandrioddemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.example.labandrioddemo.database.entities.BattleHistory;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.database.entities.User;

import org.junit.Test;

public class EntityTests {
    @Test
    public void userValid() {
        User user = new User("Jimmy", "Neutron");
        assertEquals("Jimmy", user.getUsername());
        assertEquals("Neutron", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    public void characterValid() {
        ProjectCharacter character = new ProjectCharacter("Anakin", 2, 1, 10, 10, 10, 1, 1, 1, 1);
        character.setCharacterName("Skywalker");
        assertNotEquals("Skywalker", "Anakin");
        assertEquals(10, character.getMaxHp());
        assertEquals(2, character.getUserID());
        assertEquals(1, character.getLvl());
        assertNotEquals(15, character.getGold());
        assertNotEquals(11, character.getCurrHp());
        assertEquals(10, character.getMaxHp());
        assertEquals(1, character.getAtkMod());
        assertNotEquals(2, character.getFleeChance());
        assertNotEquals(2, character.getBattleNum());
        assertEquals(1, character.getSlot());
    }

    @Test
    public void battleHistoryValid() {
        BattleHistory battleHistory = new BattleHistory(3, 1, 5, false);
        assertEquals(3, battleHistory.getCharacterId());
        assertNotEquals(1, battleHistory.getRemainingHp());
        assertNotEquals(42, battleHistory.getCharacterId());
        assertEquals(battleHistory.isWin(), false);
    }
}
