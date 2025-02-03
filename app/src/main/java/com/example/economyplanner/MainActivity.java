package com.example.economyplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.economyplanner.MainActivityFragments.CalendarFragment;
import com.example.economyplanner.MainActivityFragments.ListFragment;
import com.example.economyplanner.MainActivityFragments.ProfileFragment;
import com.example.economyplanner.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String SHARED_PREFERENCES = "LoginData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RefreshTokenAndLoadActivity();


    }

    private void RefreshTokenAndLoadActivity(){

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String refreshToken = sharedPreferences.getString("RefreshToken", "");

        String url = "http://172.28.187.56:8000/api/v1/token/refresh/";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("refresh", refreshToken);
        } catch (JSONException error) {
            HandleError(error.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = (JSONObject) response.get("Data");
                    String accessToken = (String) data.get("access");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("AccessToken", accessToken);
                    editor.apply();

                    LoadActivity();

                } catch (JSONException error) {
                    HandleError(error.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HandleError(error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);
    }
    private void LoadActivity(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ReplaceFragment(new ListFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.list) {
                ReplaceFragment(new ListFragment());
            }
            else if(item.getItemId() == R.id.calendar){
                ReplaceFragment(new CalendarFragment());
            }
            else if(item.getItemId() == R.id.profile){
                ReplaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    private void ReplaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void HandleError(String error){
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}