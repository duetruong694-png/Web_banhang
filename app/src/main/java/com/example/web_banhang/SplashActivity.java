package com.example.web_banhang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2200;

    private TextView tvLogo;
    private TextView tvName;
    private TextView tvSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hiển thị giao diện Splash
        setContentView(R.layout.activity_splash);

        // Ánh xạ giao diện
        tvLogo = findViewById(R.id.tvLogo);
        tvName = findViewById(R.id.tvName);
        tvSubtitle = findViewById(R.id.tvSubtitle);

        // Chờ 2.2 giây rồi kiểm tra trạng thái đăng nhập
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            SharedPreferences preferences =
                    getSharedPreferences("LOGIN", MODE_PRIVATE);

            // Kiểm tra đã đăng nhập chưa
            boolean isLoggedIn =
                    preferences.getBoolean("isLoggedIn", false);

            // Lấy username
            String username =
                    preferences.getString("username", "");

            // Lấy role
            String role =
                    preferences.getString("role", "");

            Intent intent;

            // =====================================================
            // ĐÃ ĐĂNG NHẬP
            // =====================================================
            if (isLoggedIn) {

                // -------------------------------------------------
                // ADMIN
                // -------------------------------------------------
                if ("admin".equalsIgnoreCase(role)) {

                    intent = new Intent(
                            SplashActivity.this,
                            AdminActivity.class
                    );

                }

                // -------------------------------------------------
                // USER
                // -------------------------------------------------
                else {

                    intent = new Intent(
                            SplashActivity.this,
                            UserActivity.class
                    );
                }

            }

            // =====================================================
            // CHƯA ĐĂNG NHẬP
            // =====================================================
            else {

                // Trang chủ vẫn cho phép xem sản phẩm
                intent = new Intent(
                        SplashActivity.this,
                        UserActivity.class
                );
            }

            // Không cho quay lại Splash bằng nút Back
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();

        }, SPLASH_TIME);
    }
}
