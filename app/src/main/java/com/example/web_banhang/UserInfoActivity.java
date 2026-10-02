package com.example.web_banhang;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.web_banhang.api.ApiService;
import com.example.web_banhang.api.RetrofitClient;
import com.example.web_banhang.api.UserInfoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    private TextView tvUsername;
    private TextView tvFullName;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvRole;

    private Button btnEdit;
    private Button btnBack;

    private int userId;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_info);

        // =========================
        // ÁNH XẠ VIEW
        // =========================

        tvUsername = findViewById(R.id.tvUsername);
        tvFullName = findViewById(R.id.tvFullName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvRole = findViewById(R.id.tvRole);

        btnEdit = findViewById(R.id.btnEdit);
        btnBack = findViewById(R.id.btnBack);

        // =========================
        // LẤY USER ID
        // =========================

        userId = getSharedPreferences(
                "USER",
                MODE_PRIVATE
        ).getInt("user_id", 0);

        if (userId == 0) {

            userId = getSharedPreferences(
                    "LoginPrefs",
                    MODE_PRIVATE
            ).getInt("user_id", 0);
        }

        // =========================
        // RETROFIT
        // =========================

        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        // =========================
        // NÚT SỬA
        // =========================

        btnEdit.setOnClickListener(v -> {

            android.content.Intent intent =
                    new android.content.Intent(
                            UserInfoActivity.this,
                            EditUserActivity.class
                    );

            startActivity(intent);
        });

        // =========================
        // NÚT QUAY LẠI
        // =========================

        btnBack.setOnClickListener(v -> finish());

        // =========================
        // LOAD THÔNG TIN
        // =========================

        loadUserInfo();
    }

    // =====================================================
    // LOAD USER INFO
    // =====================================================

    private void loadUserInfo() {

        if (userId <= 0) {

            Toast.makeText(
                    this,
                    "Bạn chưa đăng nhập",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        apiService.getUserInfo(userId)
                .enqueue(new Callback<UserInfoResponse>() {

                    @Override
                    public void onResponse(
                            Call<UserInfoResponse> call,
                            Response<UserInfoResponse> response
                    ) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    UserInfoActivity.this,
                                    "Không lấy được thông tin tài khoản",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        UserInfoResponse result =
                                response.body();

                        if (!result.isSuccess()) {

                            Toast.makeText(
                                    UserInfoActivity.this,
                                    result.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        if (result.getUser() == null) {

                            Toast.makeText(
                                    UserInfoActivity.this,
                                    "Không có dữ liệu tài khoản",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        // =========================
                        // HIỂN THỊ THÔNG TIN
                        // =========================

                        tvUsername.setText(
                                "Tên tài khoản: " +
                                        safe(
                                                result.getUser()
                                                        .getUsername()
                                        )
                        );

                        tvFullName.setText(
                                "Họ và tên: " +
                                        safe(
                                                result.getUser()
                                                        .getFullName()
                                        )
                        );

                        tvPhone.setText(
                                "Số điện thoại: " +
                                        safe(
                                                result.getUser()
                                                        .getPhone()
                                        )
                        );

                        tvAddress.setText(
                                "Địa chỉ: " +
                                        safe(
                                                result.getUser()
                                                        .getAddress()
                                        )
                        );

                        tvRole.setText(
                                "Vai trò: " +
                                        getRoleText(
                                                result.getUser()
                                                        .getRole()
                                        )
                        );
                    }

                    @Override
                    public void onFailure(
                            Call<UserInfoResponse> call,
                            Throwable t
                    ) {

                        Toast.makeText(
                                UserInfoActivity.this,
                                "Lỗi kết nối: " +
                                        t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    // =====================================================
    // LOAD LẠI KHI QUAY VỀ
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (apiService != null
                && userId > 0) {

            loadUserInfo();
        }
    }

    // =====================================================
    // XỬ LÝ NULL
    // =====================================================

    private String safe(String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return "Chưa cập nhật";
        }

        return value;
    }

    // =====================================================
    // HIỂN THỊ ROLE
    // =====================================================

    private String getRoleText(String role) {

        if (role == null) {
            return "Không xác định";
        }

        if (role.equalsIgnoreCase("admin")) {
            return "Quản trị viên";
        }

        if (role.equalsIgnoreCase("user")) {
            return "Người dùng";
        }

        return role;
    }
}