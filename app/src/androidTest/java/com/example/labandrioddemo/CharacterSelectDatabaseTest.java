package com.example.labandrioddemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
public class UserDatabaseTest {
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
    public void writeUserAndReadInList() throws Exception {
        ProjectCharacter character = new ProjectCharacter("George", 2, 1, 10, 10, 10, 1, 7, 1, 2);
        character.setCharacterName("George");
        cdao.insert(character);
        ProjectCharacter byName = cdao.getCharacterByNameWithoutLiveData("George");
        assertNotNull(byName);
        assertEquals("George", byName.getCharacterName());

        byName.setCharacterName("Jimmy");
        cdao.updateCharacter(byName);
        assertEquals("Jimmy", byName.getCharacterName());

        assertNotNull(byName);
        cdao.delete(byName);
    }
}