package com.example.loginfunction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginfunction.SQLite.DBHelper;

public class ResetPasswordActivity extends AppCompatActivity {

    TextView userName;
    EditText newPassword, newRePassword;
    Button confirm, btnBack;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userName = findViewById(R.id.userName);
        newPassword = findViewById(R.id.newpassword);
        newRePassword = findViewById(R.id.newrepassword);
        confirm = findViewById(R.id.changepassword);
        btnBack = findViewById(R.id.btnBack);


        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        userName.setText(intent.getStringExtra("userName"));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString();
                String password = newPassword.getText().toString();
                String repassword = newRePassword.getText().toString();
                if(password.equals(repassword)) {
                    Boolean checkPasswordd = dbHelper.updatePassword(user, password);
                    if (checkPasswordd == true) {
                        Intent intent = new Intent(getApplicationContext(), LoginAcivity.class);
                        startActivity(intent);
                        Toast.makeText(ResetPasswordActivity.this, "Password updated successfully !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Password can't update successfully !!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Password not matching !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}