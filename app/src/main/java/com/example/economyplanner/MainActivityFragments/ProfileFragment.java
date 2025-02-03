package com.example.economyplanner.MainActivityFragments;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.economyplanner.LoginActivity;
import com.example.economyplanner.R;
import com.example.economyplanner.TaskRecyclerView.TaskItem;
import com.example.economyplanner.TaskRecyclerView.TasksListAdapter;
import com.example.economyplanner.UsersRecyclerView.UserItem;
import com.example.economyplanner.UsersRecyclerView.UsersListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    TextView usernameTextView, jobTitleTextView;
    Button logoutButton, saveStatistic;
    RecyclerView subordinatesRecyclerView, bossesRecyclerView;
    String SHARED_PREFERENCES = "LoginData";
    ArrayList<UserItem> subordinatesList = new ArrayList<>();
    ArrayList<UserItem> bossesList = new ArrayList<>();
    final static int REQUEST_CODE = 1232;


    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        usernameTextView = view.findViewById(R.id.usernameTextView);
        jobTitleTextView = view.findViewById(R.id.jobTitleTextView);

        subordinatesRecyclerView = view.findViewById(R.id.subordinatesRecyclerView);
        subordinatesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bossesRecyclerView = view.findViewById(R.id.bossesRecyclerView);
        bossesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

        saveStatistic = view.findViewById(R.id.saveStatistic);
        saveStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermissions();
                createPDF();
            }
        });
        SetData();
        return view;
    }

    private void SetData() {
        String url = "http://172.28.187.56:8000/api/v1/profile/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = (JSONObject) response.get("Data");
                    String username = (String) data.get("username");
                    String jobTitle = (String) data.get("job_title");
                    JSONArray subordinates = (JSONArray) data.get("subordinates");
                    JSONArray bosses = (JSONArray) data.get("bosses");

                    usernameTextView.setText(username);
                    jobTitleTextView.setText(jobTitle);

                    for (int i=0; i<subordinates.length(); i++){
                        JSONObject user = (JSONObject) subordinates.get(i);
                        subordinatesList.add(new UserItem((Integer) user.get("id"), user.get("username").toString(), user.get("job_title").toString()));
                    }
                    subordinatesRecyclerView.setAdapter(new UsersListAdapter(getContext(), subordinatesList));

                    for (int i=0; i<bosses.length(); i++){
                        JSONObject user = (JSONObject) bosses.get(i);
                        bossesList.add(new UserItem((Integer) user.get("id"), user.get("username").toString(), user.get("job_title").toString()));
                    }
                    bossesRecyclerView.setAdapter(new UsersListAdapter(getContext(), bossesList));

                } catch (JSONException error) {
                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_LONG).show();
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
    }

    public void LogOut() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("AccessToken", "");
        editor.putString("RefreshToken", "");
        editor.apply();

        OpenLoginActivity();
    }
    private void OpenLoginActivity() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void askPermissions(){
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    private void createPDF(){
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint titlePaint = new Paint();
        titlePaint.setTextSize(42);
        titlePaint.setTextAlign(Paint.Align.CENTER);

        Paint textPaint = new Paint();
        textPaint.setTextSize(25);
        textPaint.setTextAlign(Paint.Align.LEFT);



        String text = "Статистика";

        canvas.drawText(text, canvas.getWidth()/2, canvas.getHeight()/10, titlePaint);


        String url = "http://172.28.187.56:8000/api/v1/tasks/statistic/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = (JSONArray) response.get("Data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = (JSONObject) data.get(i);
                        TaskItem taskItem = new TaskItem((Integer) item.get("id"), item.get("name").toString(), (boolean) item.get("status"), item.get("deadline_start").toString(), item.get("deadline_end").toString(), item.get("time_completed").toString());
                        String line =  "Задача " + "`" + taskItem.getName() + "`" + " выполнена за " + taskItem.getTimeCompleted();
                        canvas.drawText(line, 100, (canvas.getHeight()/10) + 50 * ((i*2)+1), textPaint);

                        ArrayList<String> performers = new ArrayList<>();
                        JSONArray users = (JSONArray) item.get("users");
                        for (int j = 0; j < users.length(); j++){
                            JSONObject user = (JSONObject) users.get(j);
                            performers.add(user.get("username").toString());
                        }
                        canvas.drawText("Выполнили: " + String.join("; ", performers), 100, (canvas.getHeight()/10) + 50 * ((i*2)+2) - 20, textPaint);


                    }
                    pdfDocument.finishPage(page);

                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    String fileName = "statistic.pdf";
                    File file = new File(downloadsDir, fileName);
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        pdfDocument.writeTo(fileOutputStream);
                        pdfDocument.close();
                        Toast.makeText(requireContext(), String.format("Сохранено в файл %s ",fileName), Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
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


//        TextPaint mTextPaint=new TextPaint();
//        StaticLayout mTextLayout = new StaticLayout("my text\nNext line is very long text that does not definitely fit in a single line on an android device. This will show you how!", mTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//
//        canvas.save();
//
//        int textX = 100;
//        int textY = 100;
//
//        canvas.translate(textX, textY);
//        mTextLayout.draw(canvas);
//        canvas.restore();


    }

}