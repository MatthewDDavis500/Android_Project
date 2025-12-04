package com.example.labandrioddemo.viewHolders;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.labandrioddemo.database.entities.BattleHistory;

public class HistoryAdapter extends ListAdapter<BattleHistory, HistoryViewHolder> {
    public HistoryAdapter(@NonNull DiffUtil.ItemCallback<BattleHistory> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return HistoryViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        BattleHistory current = getItem(position);
        holder.bind(current.toString());
    }

    public static class HistoryDiff extends DiffUtil.ItemCallback<BattleHistory> {
        @Override
        public boolean areItemsTheSame(@NonNull BattleHistory oldItem, @NonNull BattleHistory newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull BattleHistory oldItem, @NonNull BattleHistory newItem) {
            return oldItem.equals(newItem);
        }
    }
}
