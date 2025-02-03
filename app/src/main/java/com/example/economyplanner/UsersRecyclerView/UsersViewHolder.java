package com.example.economyplanner.UsersRecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.economyplanner.R;

public class UsersViewHolder extends RecyclerView.ViewHolder {
    TextView userId;
    TextView userName;
    TextView jobTitle;
    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);

        userId = itemView.findViewById(R.id.userId);
        userName = itemView.findViewById(R.id.userName);
        jobTitle = itemView.findViewById(R.id.jobTitleTextView);
        userName.setText("AAAAA");
    }

}
