package com.example.labandrioddemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;

import com.example.labandrioddemo.database.AccountDatabase;
import com.example.labandrioddemo.database.CharacterDAO;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class ProjectCharacterDatabaseTest {
    private CharacterDAO cdao;
    private AccountDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AccountDatabase.class).build();
        cdao = db.characterDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertCharacter() {
        ProjectCharacter character = new ProjectCharacter("Timmy", 3, 1, 10, 10, 10, 1, 7, 1, 2);
        cdao.insert(character);
        ProjectCharacter dbCharacter = cdao.getCharacterByNameWithoutLiveData("Timmy");
        assertNotNull(dbCharacter);
        assertEquals(character, dbCharacter.getCharacterName());
    }

    @Test
    public void updateCharacter() {
        ProjectCharacter otherCharacter = new ProjectCharacter("Jimmy", 4, 1, 10, 10, 10, 1, 7 3);
        cdao.insert(otherCharacter);

        ProjectCharacter dbCharacter = cdao.getCharacterByNameWithoutLiveData("Jimmy");
        assertNotNull(dbCharacter);

        dbCharacter.setCharacterName("Jeffrey");
        cdao.updateCharacter(dbCharacter);
        assertNotNull(otherCharacter, dbCharacter);
    }

    @Test
    public void deleteCharacter() {
        ProjectCharacter forSureAnotherCharacter = new ProjectCharacter("Vinny", 4, 1, 10, 10, 10, 1, 7 4);
        cdao.insert(forSureAnotherCharacter);

        cdao.delete(forSureAnotherCharacter);
        assertNull(forSureAnotherCharacter);
    }
}