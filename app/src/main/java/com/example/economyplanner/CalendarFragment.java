package com.example.economyplanner;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CalendarFragment extends Fragment {

    RecyclerView recyclerView;
    CalendarView calendarView;

    List<Item> items;

    public CalendarFragment() {
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);


        items = new ArrayList<>();
        String url = "http://artemkg2.beget.tech/api/v1/tasks/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = (JSONArray) response.get("Data");
                    for (int i=0; i<data.length(); i++){
                        JSONObject item = (JSONObject) data.get(i);
                        items.add(new Item((Integer) item.get("id"), item.get("name").toString(), (boolean)item.get("status"), item.get("deadline_start").toString(), item.get("deadline_end").toString()));
                    }
                    Date currentDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String currentDateFormatted = sdf.format(currentDate);
                    recyclerView.setAdapter(new MyAdapter(requireContext(), getTasksForDay(currentDateFormatted)));
                } catch (JSONException e) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzAxMzQ2NjQ1LCJpYXQiOjE3MDExNzM4NDUsImp0aSI6IjI1NzkwOTk2NjEwZjRmOTY5M2NjYzRhMmMwZjdjNTZlIiwidXNlcl9pZCI6MX0.Fy5FpG1A0judpz98gzJUj-yBkqGVXGSXju5t73aJW-o");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(jsonObjectRequest);


        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDateFormatted = sdf.format(currentDate);

        recyclerView = view.findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyAdapter(requireContext(), getTasksForDay(currentDateFormatted)));


        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                month = month+1;
                String selectedDate = year + "-" + (month < 10 ? "0" : "") + month + "-" + (dayOfMonth < 10 ? "0" : "") +  dayOfMonth;
                List<Item> items_local = getTasksForDay(selectedDate);
                recyclerView.setAdapter(new MyAdapter(requireContext(), items_local));
            }
        });





        return view;
    }
    private List<Item> getTasksForDay(String selectedDateString){
        LocalDate selectedDate = LocalDate.parse(selectedDateString);
        List<Item> items_local = new ArrayList<>();
        for (int i=0; i<items.size(); i++) {
            LocalDate deadLineStart = LocalDate.parse(items.get(i).getDeadlineStart());
            LocalDate deadLineEnd = LocalDate.parse(items.get(i).getDeadlineEnd());
            if ((selectedDate.isAfter(deadLineStart) || selectedDate.isEqual(deadLineStart))
                    && (selectedDate.isBefore(deadLineEnd) || selectedDate.isEqual(deadLineEnd))) {
                items_local.add(items.get(i));
            }
        }
        return items_local;
    }
}