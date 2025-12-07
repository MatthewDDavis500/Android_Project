package com.example.labandrioddemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;

import com.example.labandrioddemo.database.AccountDatabase;
import com.example.labandrioddemo.database.UserDAO;
import com.example.labandrioddemo.database.entities.User;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class UserDatabaseTest {
    private UserDAO userDAO;
    private AccountDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AccountDatabase.class).build();
        userDAO = db.userDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertUser() {
        User user = new User("Timmy", "1234");
        assertNotNull(user);
        userDAO.insert(user);

        User testUser = new User("Jimmy", "4321");
        assertNotNull(testUser);
        userDAO.insert(testUser);

        assertNotEquals(user, testUser);
    }

    @Test
    public void updateUser() {
        User user = new User("Timmy", "9876");
        assertNotNull(user);
        userDAO.insert(user);

        User dbUser = userDAO.getUserByUsernameWithoutLiveData("Timmy");
        assertNotNull(dbUser);

        dbUser.setUsername("Rambo");
        userDAO.updateUser(dbUser);
        assertNotEquals(user, dbUser);
    }

    @Test
    public void deleteUser() {
        User user = new User("Mike", "1589");
        assertNotNull(user);

        userDAO.deleteAll();

        assertNull(userDAO.getUserByUsernameWithoutLiveData(user.getUsername()));
    }
}
