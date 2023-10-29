package com.example.loginfunction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginfunction.SQLite.DBHelper;

public class LoginAcivity extends AppCompatActivity {

    EditText username1, password1;
    Button signin1, createaccount, btnForgotPassword;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username1 = findViewById(R.id.username1);
        password1 = findViewById(R.id.password1);
        signin1 = findViewById(R.id.signin1);
        createaccount = findViewById(R.id.createaccount);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        dbHelper = new DBHelper(this);

        signin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username1.getText().toString();
                String pass = password1.getText().toString();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(LoginAcivity.this, "All field required !!!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkuserpass = dbHelper.checkUsernamePassword(user, pass);
                    if(checkuserpass == true) {
                        Toast.makeText(LoginAcivity.this, "Login Successfully !!!", Toast.LENGTH_SHORT).show();
                        if(user.equals("admin")){
                            Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                            intent.putExtra("username", user);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            intent.putExtra("username", user);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(LoginAcivity.this, "Login Failed !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}