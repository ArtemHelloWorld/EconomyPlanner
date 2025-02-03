package com.example.economyplanner.TaskRecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.economyplanner.R;

public class TasksViewHolder extends RecyclerView.ViewHolder {
    TextView taskId;
    CheckBox isDone;
    TextView taskName;
    TextView dateTextView;
    public TasksViewHolder(@NonNull View itemView) {
        super(itemView);

        taskId = itemView.findViewById(R.id.taskId);
        isDone = itemView.findViewById(R.id.isDone);
        taskName = itemView.findViewById(R.id.taskName);
        dateTextView = itemView.findViewById(R.id.jobTitleTextView);
    }
}
