package com.example.labandrioddemo.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.labandrioddemo.MainActivity;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, ProjectCharacter.class}, version = 6, exportSchema = false)
public abstract class AccountDatabase extends RoomDatabase {
    public static final String USER_TABLE = "usertable";
    public static final String CHARACTER_TABLE = "charactertable";
    private static final String DATABASE_NAME = "Accountdatabase";
    private static volatile AccountDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AccountDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (AccountDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AccountDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "Database Created!");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                CharacterDAO cdao = INSTANCE.characterDAO();
                dao.deleteAll();
                cdao.deleteAll();

                User admin = new User("admin2", "admin2");
                admin.setAdmin(true);
                dao.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                dao.insert(testUser1);

                ProjectCharacter name = new ProjectCharacter("testdummy1", 2, 1, 500000,
                        5, 100, 5, 7, 52, 1);
                cdao.insert(name);
            });
        }
    };

    public abstract UserDAO userDAO();

    public abstract CharacterDAO characterDAO();
}
