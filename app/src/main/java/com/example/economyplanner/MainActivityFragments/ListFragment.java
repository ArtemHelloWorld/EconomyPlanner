package com.example.economyplanner.MainActivityFragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.economyplanner.NewTaskActivity;
import com.example.economyplanner.R;
import com.example.economyplanner.TaskRecyclerView.TaskItem;
import com.example.economyplanner.TaskRecyclerView.TasksListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListFragment extends Fragment {

    RecyclerView recyclerView;
    Button addButton;
    String SHARED_PREFERENCES = "LoginData";


    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);


        ArrayList<TaskItem> taskItems = new ArrayList<>();
        String url = "http://172.28.187.56:8000/api/v1/tasks/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = (JSONArray) response.get("Data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = (JSONObject) data.get(i);
                        taskItems.add(new TaskItem((Integer) item.get("id"), item.get("name").toString(), (boolean) item.get("status"), item.get("deadline_start").toString(), item.get("deadline_end").toString(), item.get("time_completed").toString()));
                    }
                    recyclerView.setAdapter(new TasksListAdapter(requireContext(), taskItems));
                } catch (JSONException e) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("AccessToken", "");
                headers.put("Authorization", String.format("Bearer %s", accessToken));
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(jsonObjectRequest);


        recyclerView = view.findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TasksListAdapter(requireContext(), taskItems));

        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), NewTaskActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}