package com.example.labandrioddemo.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.labandrioddemo.MainActivity;
import com.example.labandrioddemo.database.entities.User;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AccountRepository {
    private final UserDAO userDAO;

//    private ArrayList<> allCharacters;

    private static AccountRepository repository;

    private AccountRepository(Application application) {
        AccountDatabase db = AccountDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
//        this.allCharacters = (ArrayList<GymLog>) this.gymLogDAO.getAllRecords();
    }

    public static AccountRepository getRepository(Application application) {
        if(repository != null) {
            return repository;
        }
        Future<AccountRepository> future = AccountDatabase.databaseWriteExecutor.submit(
                new Callable<AccountRepository>() {
                    @Override
                    public AccountRepository call() throws Exception {
                        return new AccountRepository(application);
                    }
                }
        );

        try {
            return future.get();
        } catch(InterruptedException | ExecutionException e) {
            Log.d(MainActivity.TAG, "Problem getting AccountRepository, thread error.");
        }
        return null;
    }

//    public ArrayList<GymLog> getAllCharacters() {
//        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
//                new Callable<ArrayList<GymLog>>() {
//                    @Override
//                    public ArrayList<GymLog> call() throws Exception {
//                        return (ArrayList<GymLog>) gymLogDAO.getAllRecords();
//                    }
//                }
//        );
//
//        try {
//            return future.get();
//        } catch(InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in the repository");
//        }
//
//        return null;
//    }

    public void insertUser(User... user) {
        AccountDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }


}
