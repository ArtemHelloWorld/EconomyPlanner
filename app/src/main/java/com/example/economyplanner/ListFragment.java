package com.example.economyplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class ListFragment extends Fragment {

    RecyclerView recyclerView;
    Button addButton;

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


        List<Item> items = new ArrayList<>();
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


        recyclerView = view.findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyAdapter(requireContext(), items));

        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), AddTaskActivity.class);
                startActivity(intent);
            }
        });

        return view ;

    }
}