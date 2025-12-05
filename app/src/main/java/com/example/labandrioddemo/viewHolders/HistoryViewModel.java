package com.example.labandrioddemo.viewHolders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.BattleHistory;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private final AccountRepository repository;

    public HistoryViewModel(Application application) {
        super(application);
        repository = AccountRepository.getRepository(getApplication());
    }

    public LiveData<List<BattleHistory>> getBattleByCharacterId(int characterId) {
        return repository.getBattleByCharacterId(characterId);
    }

    public void insert(BattleHistory battleHistory) {
        repository.insertBattleHistory(battleHistory);
    }
}
