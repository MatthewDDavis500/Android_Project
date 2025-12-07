package com.example.labandrioddemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;

import com.example.labandrioddemo.database.AccountDatabase;
import com.example.labandrioddemo.database.BattleHistoryDAO;
import com.example.labandrioddemo.database.UserDAO;
import com.example.labandrioddemo.database.entities.BattleHistory;
import com.example.labandrioddemo.database.entities.User;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class BattleHistoryDatabaseTest {
    private BattleHistoryDAO bdao;
    private AccountDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AccountDatabase.class).build();
        bdao = db.battleHistoryDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertBattle() {
        BattleHistory battleHistory = new BattleHistory(2, 1, 5, false);
        assertNotNull(battleHistory);
        bdao.insert(battleHistory);

        BattleHistory dbBattle = new BattleHistory(3, 2, 10, true);
        assertNotNull(dbBattle);
        assertNotEquals(battleHistory, dbBattle);
    }

    @Test
    public void updateBattle() {
        BattleHistory otherBattle = new BattleHistory(4, 3, 9, false);
        assertNotNull(otherBattle);
        bdao.insert(otherBattle);

        BattleHistory seriousBattle = new BattleHistory(4, 3, 9, true);
        assertNotNull(seriousBattle);

        seriousBattle.setWin(false);
        bdao.updateBattle(seriousBattle);
        assertEquals(otherBattle, seriousBattle);
    }

    @Test
    public void deleteBattle() {
        BattleHistory terribleBattle = new BattleHistory(5, 4, 3, true);
        assertNotNull(terribleBattle);
        bdao.insert(terribleBattle);

        bdao.deleteAll();

        assertNull(bdao.getUserByBattleNumberWithoutLiveData(terribleBattle.getBattleNumber()));
    }
}
