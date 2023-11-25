package com.example.economyplanner;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Item> items;

    public MyAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;


    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.tasklist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.isDone.setChecked(items.get(position).getStatus());
        holder.taskName.setText(items.get(position).getName());
        holder.taskName.setPaintFlags(items.get(position).getStatus() ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
        holder.dateTextView.setText(items.get(position).getDeadline());

        holder.isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                items.get(holder.getAdapterPosition()).setStatus(b);
                holder.taskName.setPaintFlags(items.get(holder.getAdapterPosition()).getStatus() ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
