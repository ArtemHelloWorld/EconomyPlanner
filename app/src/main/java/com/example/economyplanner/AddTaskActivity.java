package com.example.economyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    Button saveButton, exitButton;
    EditText taskName;
    TextView deadlineStart, deadlineEnd, errorTextView;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskName = findViewById(R.id.taskName);
        saveButton = findViewById(R.id.saveButton);
        exitButton = findViewById(R.id.exitButton);
        errorTextView = findViewById(R.id.errorTextView);

        deadlineStart = findViewById(R.id.deadlineStart);
        deadlineStart.setText(getTodayDate());
        deadlineStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerForTextView(deadlineStart);
            }
        });

        deadlineEnd = findViewById(R.id.deadlineEnd);
        deadlineEnd.setText(getTodayDate());
        deadlineEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerForTextView(deadlineEnd);
            }
        });

    }

    public void createTask(View view) {
        String url = "http://artemkg2.beget.tech/api/v1/tasks/";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", taskName.getText());
            requestBody.put("deadline_start", deadlineStart.getText());
            requestBody.put("deadline_end", deadlineEnd.getText());
        } catch (JSONException e) {
            Toast.makeText(AddTaskActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            showError();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                goBack(view);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddTaskActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                showError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzAxMzQ2NjQ1LCJpYXQiOjE3MDExNzM4NDUsImp0aSI6IjI1NzkwOTk2NjEwZjRmOTY5M2NjYzRhMmMwZjdjNTZlIiwidXNlcl9pZCI6MX0.Fy5FpG1A0judpz98gzJUj-yBkqGVXGSXju5t73aJW-o ");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddTaskActivity.this);
        requestQueue.add(jsonObjectRequest);
    }


    private String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        return year + "-" + (month < 10 ? "0" : "") + month + "-" + (dayOfMonth < 10 ? "0" : "") +  dayOfMonth;
    }
    private void openDatePickerForTextView(TextView textView){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month+1;
                String selectedDate = year + "-" + (month < 10 ? "0" : "") + month + "-" + (dayOfMonth < 10 ? "0" : "") +  dayOfMonth;
                textView.setText(selectedDate);
            }
        };
        String[] textViewText = textView.getText().toString().split("-");
        int year = Integer.parseInt(textViewText[0]);
        int month = Integer.parseInt(textViewText[1])-1;
        int dayOfMonth = Integer.parseInt(textViewText[2]);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, dayOfMonth);
        datePickerDialog.show();

    }
    public void goBack(View view){
        Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void showError(){
        errorTextView.setText("Проверьте что все поля заполнены");
    }
}