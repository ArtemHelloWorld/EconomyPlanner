package com.example.economyplanner.TaskRecyclerView;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.economyplanner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TasksListAdapter extends RecyclerView.Adapter<TasksViewHolder> {

    Context context;
    List<TaskItem> taskItems;
    String SHARED_PREFERENCES = "LoginData";


    public TasksListAdapter(Context context, List<TaskItem> taskItems) {
        this.context = context;
        this.taskItems = taskItems;
    }

    public List<TaskItem> getItems() {
        return taskItems;
    }

    public void setItems(List<TaskItem> taskItems) {
        this.taskItems = taskItems;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TasksViewHolder(LayoutInflater.from(context).inflate(R.layout.tasklist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        holder.taskId.setText(String.format(Locale.getDefault(), "%d", taskItems.get(position).getId()));
        holder.isDone.setChecked(taskItems.get(position).getStatus());
        holder.taskName.setText(taskItems.get(position).getName());
        holder.taskName.setPaintFlags(taskItems.get(position).getStatus() ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
        holder.dateTextView.setText(String.format("с %s до %s", taskItems.get(position).getDeadlineStart(), taskItems.get(position).getDeadlineEnd()));

        holder.isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String url = String.format("http://172.28.187.56:8000/api/v1/task/%s/", holder.taskId.getText());
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("status", b);
                } catch (JSONException e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, requestBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        taskItems.get(holder.getAdapterPosition()).setStatus(b);
                        holder.taskName.setPaintFlags(taskItems.get(holder.getAdapterPosition()).getStatus() ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
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
                        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                        String accessToken = sharedPreferences.getString("AccessToken", "");
                        headers.put("Authorization", String.format("Bearer %s", accessToken));
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
        return taskItems.size();
    }
}
