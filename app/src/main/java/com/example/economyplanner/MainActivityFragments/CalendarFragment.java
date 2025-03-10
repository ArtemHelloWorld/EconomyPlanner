package com.example.economyplanner.MainActivityFragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.economyplanner.R;
import com.example.economyplanner.TaskRecyclerView.TaskItem;
import com.example.economyplanner.TaskRecyclerView.TasksListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarFragment extends Fragment {

    RecyclerView recyclerView;
    CalendarView calendarView;
    String SHARED_PREFERENCES = "LoginData";


    List<TaskItem> taskItems;

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


        taskItems = new ArrayList<>();
        String url = "http://172.28.187.56:8000/api/v1/tasks/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = (JSONArray) response.get("Data");
                    for (int i=0; i<data.length(); i++){
                        JSONObject item = (JSONObject) data.get(i);
                        taskItems.add(new TaskItem((Integer) item.get("id"), item.get("name").toString(), (boolean)item.get("status"), item.get("deadline_start").toString(), item.get("deadline_end").toString(), item.get("time_completed").toString()));
                    }
                    Date currentDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String currentDateFormatted = sdf.format(currentDate);
                    recyclerView.setAdapter(new TasksListAdapter(requireContext(), getTasksForDay(currentDateFormatted)));
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
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("AccessToken", "");
                headers.put("Authorization", String.format("Bearer %s", accessToken));
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
        recyclerView.setAdapter(new TasksListAdapter(requireContext(), getTasksForDay(currentDateFormatted)));


        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                month = month+1;
                String selectedDate = year + "-" + (month < 10 ? "0" : "") + month + "-" + (dayOfMonth < 10 ? "0" : "") +  dayOfMonth;
                List<TaskItem> items_local = getTasksForDay(selectedDate);
                recyclerView.setAdapter(new TasksListAdapter(requireContext(), items_local));
            }
        });





        return view;
    }
    private List<TaskItem> getTasksForDay(String selectedDateString){
        LocalDate selectedDate = LocalDate.parse(selectedDateString);
        List<TaskItem> items_local = new ArrayList<>();
        for (int i = 0; i< taskItems.size(); i++) {
            LocalDate deadLineStart = LocalDate.parse(taskItems.get(i).getDeadlineStart());
            LocalDate deadLineEnd = LocalDate.parse(taskItems.get(i).getDeadlineEnd());
            if ((selectedDate.isAfter(deadLineStart) || selectedDate.isEqual(deadLineStart))
                    && (selectedDate.isBefore(deadLineEnd) || selectedDate.isEqual(deadLineEnd))) {
                items_local.add(taskItems.get(i));
            }
        }
        return items_local;
    }
}