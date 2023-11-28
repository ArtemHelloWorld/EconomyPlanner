package com.example.economyplanner;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView taskId;
    CheckBox isDone;
    TextView taskName;
    TextView dateTextView;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        taskId = itemView.findViewById(R.id.taskId);
        isDone = itemView.findViewById(R.id.isDone);
        taskName = itemView.findViewById(R.id.taskName);
        dateTextView = itemView.findViewById(R.id.dateTextView);
    }
}
