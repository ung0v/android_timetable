package com.ulan.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ulan.timetable.R;
import com.ulan.timetable.utils.ServicesHelper;

public class LoginActitity extends AppCompatActivity {
    private static final String DB_NAME = "timetabledb";
    private EditText Eusername;
    private EditText Epassword;
    private TextView txtError;
    private ProgressBar progressBar;
    private Button btnLogin;
    private ServicesHelper servicesHelper;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actitity);
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("connect", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else {
            servicesHelper = new ServicesHelper(this);
            init();
        }


    }

    public void init() {
        Eusername = findViewById(R.id.username);
        Epassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        txtError = findViewById(R.id.txtError);

        btnLogin = findViewById(R.id.btnLogin);
        progressBar.setVisibility(View.GONE);
        btnLogin.setVisibility(View.VISIBLE);
        txtError.setVisibility(View.GONE);
    }


    public void Login(View view) {
        btnLogin.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String username = String.valueOf(Eusername.getText()).trim();
        String password = String.valueOf(Epassword.getText()).trim();


        servicesHelper.getMarkByUrl(username , password, this);

    }

//"69DCTT20024"
//        "29/02/2000"
}