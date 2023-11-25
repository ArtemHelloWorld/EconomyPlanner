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

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.tasklist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.isDone.setChecked(items.get(position).getDone());
        holder.taskName.setText(items.get(position).getName());
        holder.taskName.setPaintFlags(items.get(position).getDone() ? Paint.STRIKE_THRU_TEXT_FLAG : 0);

        holder.isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                items.get(holder.getAdapterPosition()).setDone(b);
                holder.taskName.setPaintFlags(items.get(holder.getAdapterPosition()).getDone() ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}