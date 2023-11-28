package com.example.economyplanner;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        holder.taskId.setText(String.format(Locale.getDefault(), "%d", items.get(position).getId()));
        holder.isDone.setChecked(items.get(position).getStatus());
        holder.taskName.setText(items.get(position).getName());
        holder.taskName.setPaintFlags(items.get(position).getStatus() ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
        holder.dateTextView.setText(String.format("с %s до %s", items.get(position).getDeadlineStart(), items.get(position).getDeadlineEnd()));

        holder.isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String url = String.format("http://artemkg2.beget.tech/api/v1/task/%s/", holder.taskId.getText());
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("status", b);
                } catch (JSONException e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, requestBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        items.get(holder.getAdapterPosition()).setStatus(b);
                        holder.taskName.setPaintFlags(items.get(holder.getAdapterPosition()).getStatus() ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzAxMzQ2NjQ1LCJpYXQiOjE3MDExNzM4NDUsImp0aSI6IjI1NzkwOTk2NjEwZjRmOTY5M2NjYzRhMmMwZjdjNTZlIiwidXNlcl9pZCI6MX0.Fy5FpG1A0judpz98gzJUj-yBkqGVXGSXju5t73aJW-o ");
                        return headers;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(jsonObjectRequest);
                }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
