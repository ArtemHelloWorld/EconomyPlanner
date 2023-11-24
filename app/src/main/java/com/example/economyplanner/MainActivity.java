package com.example.economyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = findViewById(R.id.editText1);
        button = findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = editText.getText().toString() + "!";
                editText.setText(inputText);
            }
        });
    }
}