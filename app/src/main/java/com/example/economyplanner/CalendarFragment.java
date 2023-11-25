package com.example.economyplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
        items.add(new Item("Задача 1", "25-11-2023",true));
        items.add(new Item("Задача 2", "25-11-2023",false));
        items.add(new Item("Задача 3", "25-11-2023",true));
        items.add(new Item("Задача 4", "26-11-2023",false));
        items.add(new Item("Задача 5", "26-11-2023",false));
        items.add(new Item("Задача 6", "26-11-2023",false));
        items.add(new Item("Задача 7", "27-11-2023",true));
        items.add(new Item("Задача 8", "27-11-2023",false));
        items.add(new Item("Задача 9", "27-11-2023",true));
        items.add(new Item("Задача 10", "27-11-2023",false));
        items.add(new Item("Задача 11", "28-11-2023",false));
        items.add(new Item("Задача 12", "28-11-2023",true));
        items.add(new Item("Задача 13", "28-11-2023",true));
        items.add(new Item("Задача 14", "28-11-2023",false));
        items.add(new Item("Задача 15", "28-11-2023",false));
        items.add(new Item("Задача 16", "28-11-2023",true));


        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDateFormatted = sdf.format(currentDate);

        recyclerView = view.findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyAdapter(requireContext(), getTasksForDay(currentDateFormatted)));


        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                month = month+1;
                String selectedDate = dayOfMonth + "-" + month + "-" + year;
                List<Item> items_local = getTasksForDay(selectedDate);
                recyclerView.setAdapter(new MyAdapter(requireContext(), items_local));
            }
        });





        return view;
    }
    private List<Item> getTasksForDay(String selectedDate){
        List<Item> items_local = new ArrayList<>();
        for (int i=0; i<items.size(); i++) {
            if (Objects.equals(items.get(i).getDate(), selectedDate)) {
                items_local.add(items.get(i));
            }
        }
        return items_local;
    }
}