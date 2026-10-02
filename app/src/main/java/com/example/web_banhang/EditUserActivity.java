package com.example.web_banhang;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.web_banhang.api.ApiService;
import com.example.web_banhang.api.RetrofitClient;
import com.example.web_banhang.api.UpdateUserResponse;
import com.example.web_banhang.api.UserInfoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {

    private EditText edtFullName;
    private EditText edtPhone;
    private EditText edtAddress;

    private Button btnSave;
    private Button btnCancel;

    private int userId;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_user);

        // =========================
        // ÁNH XẠ VIEW
        // =========================
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // =========================
        // LẤY USER ID
        // =========================
        userId = getSharedPreferences(
                "USER",
                MODE_PRIVATE
        ).getInt("user_id", 0);

        // Hỗ trợ SharedPreferences cũ
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
        // TẢI THÔNG TIN HIỆN TẠI
        // =========================
        loadCurrentInfo();

        // =========================
        // NÚT LƯU
        // =========================
        btnSave.setOnClickListener(v -> updateUser());

        // =========================
        // NÚT HỦY
        // =========================
        btnCancel.setOnClickListener(v -> finish());
    }

    // =========================================================
    // LẤY THÔNG TIN USER
    // =========================================================

    private void loadCurrentInfo() {

        if (userId <= 0) {

            Toast.makeText(
                    this,
                    "Chưa đăng nhập",
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
                            Response<UserInfoResponse> response) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    EditUserActivity.this,
                                    "Không lấy được thông tin tài khoản",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        UserInfoResponse result =
                                response.body();

                        if (!result.isSuccess()) {

                            Toast.makeText(
                                    EditUserActivity.this,
                                    result.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        if (result.getUser() == null) {

                            Toast.makeText(
                                    EditUserActivity.this,
                                    "Không có dữ liệu tài khoản",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        // =========================
                        // ĐƯA DỮ LIỆU VÀO FORM
                        // =========================

                        String fullName =
                                result.getUser().getFullName();

                        String phone =
                                result.getUser().getPhone();

                        String address =
                                result.getUser().getAddress();

                        if (fullName != null) {
                            edtFullName.setText(fullName);
                        }

                        if (phone != null) {
                            edtPhone.setText(phone);
                        }

                        if (address != null) {
                            edtAddress.setText(address);
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<UserInfoResponse> call,
                            Throwable t) {

                        Toast.makeText(
                                EditUserActivity.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    // =========================================================
    // CẬP NHẬT USER
    // =========================================================

    private void updateUser() {

        String fullName =
                edtFullName.getText()
                        .toString()
                        .trim();

        String phone =
                edtPhone.getText()
                        .toString()
                        .trim();

        String address =
                edtAddress.getText()
                        .toString()
                        .trim();

        // =========================
        // KIỂM TRA DỮ LIỆU
        // =========================

        if (fullName.isEmpty()) {

            edtFullName.setError(
                    "Vui lòng nhập họ và tên"
            );

            edtFullName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {

            edtPhone.setError(
                    "Vui lòng nhập số điện thoại"
            );

            edtPhone.requestFocus();
            return;
        }

        if (address.isEmpty()) {

            edtAddress.setError(
                    "Vui lòng nhập địa chỉ"
            );

            edtAddress.requestFocus();
            return;
        }

        // =========================
        // GỌI API UPDATE
        // =========================

        apiService.updateUser(
                userId,
                fullName,
                phone,
                address
        ).enqueue(new Callback<UpdateUserResponse>() {

            @Override
            public void onResponse(
                    Call<UpdateUserResponse> call,
                    Response<UpdateUserResponse> response) {

                if (!response.isSuccessful()
                        || response.body() == null) {

                    Toast.makeText(
                            EditUserActivity.this,
                            "Cập nhật thất bại",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                UpdateUserResponse result =
                        response.body();

                if (result.isSuccess()) {

                    // =========================
                    // LƯU THÔNG TIN MỚI
                    // =========================

                    getSharedPreferences(
                            "USER",
                            MODE_PRIVATE
                    ).edit()
                            .putString(
                                    "fullName",
                                    fullName
                            )
                            .putString(
                                    "phone",
                                    phone
                            )
                            .putString(
                                    "address",
                                    address
                            )
                            .apply();

                    // Hỗ trợ SharedPreferences cũ
                    getSharedPreferences(
                            "LoginPrefs",
                            MODE_PRIVATE
                    ).edit()
                            .putString(
                                    "fullName",
                                    fullName
                            )
                            .putString(
                                    "phone",
                                    phone
                            )
                            .putString(
                                    "address",
                                    address
                            )
                            .apply();

                    Toast.makeText(
                            EditUserActivity.this,
                            "Cập nhật thông tin thành công",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();

                } else {

                    Toast.makeText(
                            EditUserActivity.this,
                            result.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<UpdateUserResponse> call,
                    Throwable t) {

                Toast.makeText(
                        EditUserActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
