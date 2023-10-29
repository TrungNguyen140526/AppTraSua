package com.example.loginfunction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginfunction.SQLite.DBHelper;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText usernamereset;
    Button resetPassword, btnBack;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        usernamereset = findViewById(R.id.usernamereset);
        resetPassword = findViewById(R.id.resetPassword);
        btnBack = findViewById(R.id.Back);
        dbHelper = new DBHelper(this);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = usernamereset.getText().toString();
                Boolean checkUserExist = dbHelper.checkUsername(userName);
                if(checkUserExist == true) {
                    Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Username does not existed !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginAcivity.class);
                startActivity(intent);
            }
        });
    }
}