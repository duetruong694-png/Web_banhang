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

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.web_banhang.api.ApiService;
import com.example.web_banhang.api.LoginResponse;
import com.example.web_banhang.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnRegister;
    private TextView tvBackHome;
    private TextView tvShowPassword;

    private boolean passwordVisible = false;

    private ApiService apiService;

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

        // ==============================
        // KHỞI TẠO API
        // ==============================

        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

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

            btnLogin.postDelayed(
                    this::login,
                    120
            );
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
        // FOCUS USERNAME
        // ==============================

        edtUsername.setOnFocusChangeListener(
                (v, hasFocus) -> {

                    if (hasFocus) {
                        editTextFocusAnimation(
                                edtUsername
                        );
                    }
                }
        );

        // ==============================
        // FOCUS PASSWORD
        // ==============================

        edtPassword.setOnFocusChangeListener(
                (v, hasFocus) -> {

                    if (hasFocus) {
                        editTextFocusAnimation(
                                edtPassword
                        );
                    }
                }
        );

        // ==============================
        // NÚT BACK ĐIỆN THOẠI
        // ==============================

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {

                    @Override
                    public void handleOnBackPressed() {

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
        );
    }

    // =========================================================
    // ĐĂNG NHẬP QUA PHP / MYSQL
    // =========================================================

    private void login() {

        String username =
                edtUsername
                        .getText()
                        .toString()
                        .trim();

        String password =
                edtPassword
                        .getText()
                        .toString();

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
        // KHÓA NÚT TRONG KHI LOGIN
        // ==============================

        btnLogin.setEnabled(false);

        btnLogin.setText(
                "Đang đăng nhập..."
        );

        // ==============================
        // GỌI API
        // ==============================

        apiService.login(
                username,
                password
        ).enqueue(
                new Callback<LoginResponse>() {

                    @Override
                    public void onResponse(
                            Call<LoginResponse> call,
                            Response<LoginResponse> response
                    ) {

                        btnLogin.setEnabled(true);

                        btnLogin.setText(
                                "ĐĂNG NHẬP"
                        );

                        // ==============================
                        // SERVER KHÔNG TRẢ VỀ ĐÚNG
                        // ==============================

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            showLoginError(
                                    "Không thể kết nối máy chủ"
                            );

                            return;
                        }

                        LoginResponse loginResponse =
                                response.body();

                        // ==============================
                        // LOGIN THẤT BẠI
                        // ==============================

                        if (!loginResponse.isSuccess()) {

                            String message =
                                    loginResponse.getMessage();

                            if (message == null
                                    || message.isEmpty()) {

                                message =
                                        "Tên đăng nhập hoặc mật khẩu không đúng";
                            }

                            showLoginError(message);

                            return;
                        }

                        // ==============================
                        // LẤY THÔNG TIN USER
                        // ==============================

                        com.example.web_banhang.api.User user =
                                loginResponse.getUser();

                        if (user == null) {

                            showLoginError(
                                    "Dữ liệu tài khoản không hợp lệ"
                            );

                            return;
                        }

                        // ==============================
                        // LƯU LOGIN
                        // ==============================

                        SharedPreferences preferences =
                                getSharedPreferences(
                                        "LOGIN",
                                        MODE_PRIVATE
                                );

                        preferences.edit()
                                .putBoolean(
                                        "isLoggedIn",
                                        true
                                )
                                .putInt(
                                        "user_id",
                                        user.getId()
                                )
                                .putString(
                                        "username",
                                        user.getUsername()
                                )
                                .putString(
                                        "fullName",
                                        user.getFullName()
                                )
                                .putString(
                                        "phone",
                                        user.getPhone()
                                )
                                .putString(
                                        "address",
                                        user.getAddress()
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
                                LoginActivity.this,
                                "Đăng nhập thành công 🎉",
                                Toast.LENGTH_SHORT
                        ).show();

                        // ==============================
                        // ADMIN
                        // ==============================

                        if ("admin".equalsIgnoreCase(
                                user.getRole()
                        )) {

                            Intent intent =
                                    new Intent(
                                            LoginActivity.this,
                                            AdminActivity.class
                                    );

                            intent.putExtra(
                                    "user_id",
                                    user.getId()
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
                                    "user_id",
                                    user.getId()
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

                    // =================================================
                    // KHÔNG KẾT NỐI ĐƯỢC SERVER
                    // =================================================

                    @Override
                    public void onFailure(
                            Call<LoginResponse> call,
                            Throwable t
                    ) {

                        btnLogin.setEnabled(true);

                        btnLogin.setText(
                                "ĐĂNG NHẬP"
                        );

                        showLoginError(
                                "Không kết nối được máy chủ"
                        );
                    }
                }
        );
    }

    // =========================================================
    // HIỂN THỊ LỖI LOGIN
    // =========================================================

    private void showLoginError(
            String message
    ) {

        edtPassword.setError(message);

        edtPassword.requestFocus();

        shakeView(edtPassword);

        Toast.makeText(
                LoginActivity.this,
                message,
                Toast.LENGTH_SHORT
        ).show();
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

        tvShowPassword.startAnimation(
                animation
        );
    }

    // =========================================================
    // HIỆU ỨNG BUTTON
    // =========================================================

    private void buttonPressAnimation(
            View view
    ) {

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

        view.startAnimation(
                animation
        );
    }

    // =========================================================
    // HIỆU ỨNG FOCUS INPUT
    // =========================================================

    private void editTextFocusAnimation(
            View view
    ) {

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

        view.startAnimation(
                animation
        );
    }

    // =========================================================
    // HIỆU ỨNG RUNG KHI SAI
    // =========================================================

    private void shakeView(
            View view
    ) {

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

        view.startAnimation(
                animation
        );
    }

    // =========================================================
    // HIỆU ỨNG MỞ TRANG
    // =========================================================

    private void animatePage() {

        View root =
                findViewById(
                        android.R.id.content
                );

        AlphaAnimation fade =
                new AlphaAnimation(
                        0.0f,
                        1.0f
                );

        fade.setDuration(500);

        root.startAnimation(fade);
    }
}