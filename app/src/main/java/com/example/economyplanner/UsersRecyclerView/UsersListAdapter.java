package com.example.economyplanner.UsersRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.economyplanner.R;

import java.util.List;
import java.util.Locale;

public class UsersListAdapter extends RecyclerView.Adapter<UsersViewHolder> {

    Context context;
    List<UserItem> userItems;
    String SHARED_PREFERENCES = "LoginData";


    public UsersListAdapter(Context context, List<UserItem> userItems) {
        this.context = context;
        this.userItems = userItems;
    }

    public List<UserItem> getItems() {
        return userItems;
    }

    public void setItems(List<UserItem> userItems) {
        this.userItems = userItems;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersViewHolder(LayoutInflater.from(context).inflate(R.layout.userlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        holder.userId.setText(String.format(Locale.getDefault(), "%d", userItems.get(position).getId()));
        holder.userName.setText(userItems.get(position).getUsername());

        holder.jobTitle.setText(userItems.get(position).getJobTitle());

    }

    @Override
    public int getItemCount() {
        return userItems.size();

    }
}
