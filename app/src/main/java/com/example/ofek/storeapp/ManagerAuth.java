package com.example.ofek.storeapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Constants.Constants;

public class ManagerAuth extends Activity {
    EditText usernameET,passwordET;
    Button loginBtn;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_auth);
        preferences=getSharedPreferences("settings",MODE_PRIVATE);
        usernameET=findViewById(R.id.usernameET);
        passwordET=findViewById(R.id.passET);
        loginBtn=findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username= usernameET.getText().toString();
                String pass= passwordET.getText().toString();
                if (username.equals(Constants.MANAGER_ID)&&pass.equals(Constants.MANAGER_PASS)){
                    MainActivity.isManager=true;
                    setResult(RESULT_OK);
                    finish();
                }
                else {
                    Toast.makeText(ManagerAuth.this, "username or password are invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
