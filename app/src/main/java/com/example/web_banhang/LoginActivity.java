package com.example.web_banhang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnRegister;
    private TextView tvBackHome;
    private TextView tvShowPassword;

    private DatabaseHelper databaseHelper;

    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ==============================
        // ÁNH XẠ VIEW
        // ==============================

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackHome = findViewById(R.id.tvBackHome);
        tvShowPassword = findViewById(R.id.tvShowPassword);

        databaseHelper = new DatabaseHelper(this);

        // ==============================
        // HIỆU ỨNG MỞ TRANG
        // ==============================

        animatePage();

        // ==============================
        // HIỆN / ẨN MẬT KHẨU
        // ==============================

        tvShowPassword.setOnClickListener(v -> togglePassword());

        // ==============================
        // ĐĂNG NHẬP
        // ==============================

        btnLogin.setOnClickListener(v -> {

            buttonPressAnimation(btnLogin);

            // Chờ animation rất ngắn rồi mới đăng nhập
            btnLogin.postDelayed(this::login, 120);
        });

        // ==============================
        // ĐĂNG KÝ
        // ==============================

        btnRegister.setOnClickListener(v -> {

            buttonPressAnimation(btnRegister);

            btnRegister.postDelayed(() -> {

                Intent intent = new Intent(
                        LoginActivity.this,
                        RegisterActivity.class
                );

                startActivity(intent);

                // Animation chuyển Activity
                overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );

            }, 100);
        });

        // ==============================
        // QUAY LẠI TRANG CHỦ
        // ==============================

        tvBackHome.setOnClickListener(v -> {

            buttonPressAnimation(tvBackHome);

            tvBackHome.postDelayed(() -> {

                Intent intent = new Intent(
                        LoginActivity.this,
                        UserActivity.class
                );

                intent.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP
                );

                startActivity(intent);

                overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );

                finish();

            }, 100);
        });

        // ==============================
        // HIỆU ỨNG FOCUS Ô USERNAME
        // ==============================

        edtUsername.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus) {
                editTextFocusAnimation(edtUsername);
            }
        });

        // ==============================
        // HIỆU ỨNG FOCUS Ô PASSWORD
        // ==============================

        edtPassword.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus) {
                editTextFocusAnimation(edtPassword);
            }
        });
    }

    // =========================================================
    // ĐĂNG NHẬP
    // =========================================================

    private void login() {

        String username =
                edtUsername.getText().toString().trim();

        String password =
                edtPassword.getText().toString().trim();

        // ==============================
        // KIỂM TRA USERNAME
        // ==============================

        if (username.isEmpty()) {

            edtUsername.setError(
                    "Vui lòng nhập tên đăng nhập"
            );

            edtUsername.requestFocus();

            shakeView(edtUsername);

            return;
        }

        // ==============================
        // KIỂM TRA PASSWORD
        // ==============================

        if (password.isEmpty()) {

            edtPassword.setError(
                    "Vui lòng nhập mật khẩu"
            );

            edtPassword.requestFocus();

            shakeView(edtPassword);

            return;
        }

        // ==============================
        // TÌM USER TRONG DATABASE
        // ==============================

        User user =
                databaseHelper.loginUser(username, password);

        // ==============================
        // SAI TÀI KHOẢN
        // ==============================

        if (user == null) {

            edtPassword.setError(
                    "Tên đăng nhập hoặc mật khẩu không đúng"
            );

            edtPassword.requestFocus();

            shakeView(edtPassword);

            Toast.makeText(
                    this,
                    "Sai tên đăng nhập hoặc mật khẩu",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // ==============================
        // LƯU TRẠNG THÁI ĐĂNG NHẬP
        // ==============================

        SharedPreferences preferences =
                getSharedPreferences(
                        "LOGIN",
                        MODE_PRIVATE
                );

        preferences.edit()
                .putBoolean("isLoggedIn", true)
                .putString(
                        "username",
                        user.getUsername()
                )
                .putString(
                        "role",
                        user.getRole()
                )
                .apply();

        // ==============================
        // THÔNG BÁO
        // ==============================

        Toast.makeText(
                this,
                "Đăng nhập thành công 🎉",
                Toast.LENGTH_SHORT
        ).show();

        // ==============================
        // ADMIN
        // ==============================

        if ("admin".equalsIgnoreCase(user.getRole())) {

            Intent intent =
                    new Intent(
                            LoginActivity.this,
                            AdminActivity.class
                    );

            startActivity(intent);

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );

            finish();

        } else {

            // ==============================
            // USER
            // ==============================

            Intent intent =
                    new Intent(
                            LoginActivity.this,
                            UserActivity.class
                    );

            intent.putExtra(
                    "username",
                    user.getUsername()
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );

            finish();
        }
    }

    // =========================================================
    // HIỆN / ẨN MẬT KHẨU
    // =========================================================

    private void togglePassword() {

        if (passwordVisible) {

            edtPassword.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
            );

            tvShowPassword.setText("👁");

            passwordVisible = false;

        } else {

            edtPassword.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            );

            tvShowPassword.setText("🙈");

            passwordVisible = true;
        }

        // Đưa con trỏ về cuối
        edtPassword.setSelection(
                edtPassword.length()
        );

        // Hiệu ứng icon
        ScaleAnimation animation =
                new ScaleAnimation(
                        0.8f,
                        1.0f,
                        0.8f,
                        1.0f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                );

        animation.setDuration(150);

        tvShowPassword.startAnimation(animation);
    }

    // =========================================================
    // HIỆU ỨNG KHI NHẤN BUTTON
    // =========================================================

    private void buttonPressAnimation(View view) {

        ScaleAnimation animation =
                new ScaleAnimation(
                        1.0f,
                        0.94f,
                        1.0f,
                        0.94f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                );

        animation.setDuration(100);

        animation.setRepeatMode(
                Animation.REVERSE
        );

        animation.setRepeatCount(1);

        view.startAnimation(animation);
    }

    // =========================================================
    // HIỆU ỨNG FOCUS INPUT
    // =========================================================

    private void editTextFocusAnimation(View view) {

        ScaleAnimation animation =
                new ScaleAnimation(
                        1.0f,
                        1.015f,
                        1.0f,
                        1.015f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                );

        animation.setDuration(150);

        view.startAnimation(animation);
    }

    // =========================================================
    // HIỆU ỨNG RUNG KHI NHẬP SAI
    // =========================================================

    private void shakeView(View view) {

        android.view.animation.TranslateAnimation animation =
                new android.view.animation.TranslateAnimation(
                        -8,
                        8,
                        0,
                        0
                );

        animation.setDuration(60);

        animation.setRepeatMode(
                Animation.REVERSE
        );

        animation.setRepeatCount(4);

        view.startAnimation(animation);
    }

    // =========================================================
    // HIỆU ỨNG MỞ TRANG
    // =========================================================

    private void animatePage() {

        View root =
                findViewById(android.R.id.content);

        AlphaAnimation fade =
                new AlphaAnimation(
                        0.0f,
                        1.0f
                );

        fade.setDuration(500);

        root.startAnimation(fade);
    }

    // =========================================================
    // NÚT BACK ĐIỆN THOẠI
    // =========================================================

    @Override
    public void onBackPressed() {

        Intent intent =
                new Intent(
                        LoginActivity.this,
                        UserActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);

        overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );

        finish();
    }
}