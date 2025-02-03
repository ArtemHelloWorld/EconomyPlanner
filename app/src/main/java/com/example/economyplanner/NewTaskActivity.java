package com.example.economyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;import android.content.DialogInterface;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.economyplanner.UsersRecyclerView.UserItem;
import com.example.economyplanner.UsersRecyclerView.UsersListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NewTaskActivity extends AppCompatActivity {

    Button saveButton, exitButton;
    EditText taskName;
    Button deadlineStart, deadlineEnd, chooseSubordinates;
    TextView errorTextView;
    DatePickerDialog datePickerDialog;

    boolean[] chosenSubordinatesBooleanArray;
    ArrayList<UserItem> subordinatesList = new ArrayList<>();
    ArrayList<Integer> subordinatesListSelectedIndexes = new ArrayList<>();


    String SHARED_PREFERENCES = "LoginData";

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
        chooseSubordinates = findViewById(R.id.chooseSubordinates);

        getSubordinatesList();

    }

    public void createTask(View view) {
        String url = "http://172.28.187.56:8000/api/v1/tasks/";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", taskName.getText());
            requestBody.put("deadline_start", deadlineStart.getText());
            requestBody.put("deadline_end", deadlineEnd.getText());
            JSONArray users = new JSONArray();
            for (int i=0; i<subordinatesListSelectedIndexes.size(); i++){
                UserItem useritem = subordinatesList.get(subordinatesListSelectedIndexes.get(i));
                users.put(useritem.getId());
            }
            requestBody.put("users_ids", (JSONArray)users);
            Toast.makeText(NewTaskActivity.this, users.toString(), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Toast.makeText(NewTaskActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(NewTaskActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                showError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("AccessToken", "");
                headers.put("Authorization", String.format("Bearer %s", accessToken));
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(NewTaskActivity.this);
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
        Intent intent = new Intent(NewTaskActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void showError(){
        errorTextView.setText("Проверьте что все поля заполнены");
    }

    private void getSubordinatesList(){
        String url = "http://172.28.187.56:8000/api/v1/profile/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = (JSONObject) response.get("Data");
                    JSONArray subordinates = (JSONArray) data.get("subordinates");

                    for (int i=0; i<subordinates.length(); i++){

                        JSONObject user = (JSONObject) subordinates.get(i);
                        subordinatesList.add(new UserItem((Integer) user.get("id"), user.get("username").toString(), user.get("job_title").toString()));
                    }
                    chosenSubordinatesBooleanArray = new boolean[subordinatesList.size()];

                } catch (JSONException e) {
                    Toast.makeText(NewTaskActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewTaskActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = NewTaskActivity.this.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("AccessToken", "");
                headers.put("Authorization", String.format("Bearer %s", accessToken));
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(NewTaskActivity.this);
        requestQueue.add(jsonObjectRequest);
    }
    public void showSubordinatesDropdown(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewTaskActivity.this);
        builder.setTitle("Выберите подчиненных");
        builder.setCancelable(false);


        String[] subordinatesUsernames = new String[subordinatesList.size()];

        for (int i = 0; i < subordinatesList.size(); i++) {
            subordinatesUsernames[i] = subordinatesList.get(i).getUsername();
        }

        builder.setMultiChoiceItems(subordinatesUsernames, chosenSubordinatesBooleanArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                    subordinatesListSelectedIndexes.add(i);
                    Collections.sort(subordinatesListSelectedIndexes);
                } else {
                    subordinatesListSelectedIndexes.remove(Integer.valueOf(i));
                }
            }
        });

        builder.setPositiveButton("сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chooseSubordinates.setText("Выбрано " + subordinatesListSelectedIndexes.size() + " подчиненных");
            }
        });

        builder.show();
    }
}