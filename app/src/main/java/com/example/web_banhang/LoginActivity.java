package com.example.web_banhang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText edtUsername;
    TextInputEditText edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {

        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {

            Toast.makeText(
                    this,
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Tài khoản ADMIN mẫu
        if (username.equals("admin") && password.equals("123456")) {

            Intent intent = new Intent(
                    LoginActivity.this,
                    AdminActivity.class
            );

            startActivity(intent);
            finish();

        }

        // Tài khoản USER mẫu
        else if (username.equals("user") && password.equals("123456")) {

            Intent intent = new Intent(
                    LoginActivity.this,
                    UserActivity.class
            );

            startActivity(intent);
            finish();

        }

        else {

            Toast.makeText(
                    this,
                    "Sai tên đăng nhập hoặc mật khẩu",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
