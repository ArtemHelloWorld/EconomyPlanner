package com.example.economyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    TextView errorText;
    Button loginButton;
    String SHARED_PREFERENCES = "LoginData";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        errorText = findViewById(R.id.errorText);
    }

    public void Login(View view){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        username = username.trim();
        password = password.trim();

        if(username.length() > 0 && password.length() > 0) {
            Authorize(username, password);
        }
        else{
            HandleError("Заполните логин и пароль");
        }





    }
    private void Authorize(String username, String password){
        String url = "http://172.28.187.56:8000/api/v1/token/";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException error) {
            HandleError(error.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = (JSONObject) response.get("Data");
                    String accessToken = (String) data.get("access");
                    String refreshToken = (String) data.get("refresh");

                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("AccessToken", accessToken);
                    editor.putString("RefreshToken", refreshToken);
                    editor.apply();

                    OpenMainActivity();

                } catch (JSONException error) {
                    HandleError("Введите корректные Логин и Пароль");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HandleError("Введите корректные Логин и Пароль");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

    private void HandleError(String error){
        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
        errorText.setText(error);
    }
    private void OpenMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}