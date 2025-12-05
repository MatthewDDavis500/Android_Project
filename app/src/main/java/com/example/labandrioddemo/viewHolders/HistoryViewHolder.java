package com.example.labandrioddemo.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.labandrioddemo.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView historyViewItem;

    private HistoryViewHolder(View gymLogView) {
        super(gymLogView);
        historyViewItem = gymLogView.findViewById(R.id.recyclerItemTextView);
    }

    public void bind(String text) {
        historyViewItem.setText(text);
    }

    static HistoryViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.battle_history_recycler_item, parent, false);
        return new HistoryViewHolder(view);
    }
}
