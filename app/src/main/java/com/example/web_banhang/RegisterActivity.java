package com.example.web_banhang;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtRegisterUsername;
    private EditText edtRegisterPassword;
    private EditText edtRegisterFullName;
    private EditText edtRegisterPhone;
    private EditText edtRegisterAddress;

    private Button btnRegisterAccount;
    private Button btnBackLogin;

    private TextView tvShowRegisterPassword;

    private DatabaseHelper databaseHelper;

    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        // ==============================
        // ÁNH XẠ VIEW
        // ==============================

        edtRegisterUsername =
                findViewById(R.id.edtRegisterUsername);

        edtRegisterPassword =
                findViewById(R.id.edtRegisterPassword);

        edtRegisterFullName =
                findViewById(R.id.edtRegisterFullName);

        edtRegisterPhone =
                findViewById(R.id.edtRegisterPhone);

        edtRegisterAddress =
                findViewById(R.id.edtRegisterAddress);

        btnRegisterAccount =
                findViewById(R.id.btnRegisterAccount);

        btnBackLogin =
                findViewById(R.id.btnBackLogin);

        tvShowRegisterPassword =
                findViewById(R.id.tvShowRegisterPassword);

        databaseHelper =
                new DatabaseHelper(this);

        // ==============================
        // HIỆU ỨNG MỞ TRANG
        // ==============================

        animatePage();

        // ==============================
        // HIỆN / ẨN MẬT KHẨU
        // ==============================

        tvShowRegisterPassword.setOnClickListener(v ->
                togglePassword()
        );

        // ==============================
        // ĐĂNG KÝ
        // ==============================

        btnRegisterAccount.setOnClickListener(v -> {

            buttonPressAnimation(btnRegisterAccount);

            btnRegisterAccount.postDelayed(
                    this::registerAccount,
                    120
            );
        });

        // ==============================
        // QUAY LẠI LOGIN
        // ==============================

        btnBackLogin.setOnClickListener(v -> {

            buttonPressAnimation(btnBackLogin);

            btnBackLogin.postDelayed(() -> {

                finish();

                overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );

            }, 100);
        });

        // ==============================
        // FOCUS ANIMATION
        // ==============================

        setFocusAnimation(edtRegisterUsername);
        setFocusAnimation(edtRegisterPassword);
        setFocusAnimation(edtRegisterFullName);
        setFocusAnimation(edtRegisterPhone);
        setFocusAnimation(edtRegisterAddress);
    }

    // =========================================================
    // ĐĂNG KÝ
    // =========================================================

    private void registerAccount() {

        String username =
                edtRegisterUsername
                        .getText()
                        .toString()
                        .trim();

        String password =
                edtRegisterPassword
                        .getText()
                        .toString()
                        .trim();

        String fullName =
                edtRegisterFullName
                        .getText()
                        .toString()
                        .trim();

        String phone =
                edtRegisterPhone
                        .getText()
                        .toString()
                        .trim();

        String address =
                edtRegisterAddress
                        .getText()
                        .toString()
                        .trim();

        // ==============================
        // KIỂM TRA USERNAME
        // ==============================

        if (username.isEmpty()) {

            showError(
                    edtRegisterUsername,
                    "Vui lòng nhập tên đăng nhập"
            );

            return;
        }

        if (username.length() < 3) {

            showError(
                    edtRegisterUsername,
                    "Tên đăng nhập phải có ít nhất 3 ký tự"
            );

            return;
        }

        // ==============================
        // KIỂM TRA PASSWORD
        // ==============================

        if (password.isEmpty()) {

            showError(
                    edtRegisterPassword,
                    "Vui lòng nhập mật khẩu"
            );

            return;
        }

        if (password.length() < 6) {

            showError(
                    edtRegisterPassword,
                    "Mật khẩu phải có ít nhất 6 ký tự"
            );

            return;
        }

        // ==============================
        // KIỂM TRA HỌ TÊN
        // ==============================

        if (fullName.isEmpty()) {

            showError(
                    edtRegisterFullName,
                    "Vui lòng nhập họ tên"
            );

            return;
        }

        // ==============================
        // KIỂM TRA SỐ ĐIỆN THOẠI
        // ==============================

        if (phone.isEmpty()) {

            showError(
                    edtRegisterPhone,
                    "Vui lòng nhập số điện thoại"
            );

            return;
        }

        if (phone.length() < 9) {

            showError(
                    edtRegisterPhone,
                    "Số điện thoại không hợp lệ"
            );

            return;
        }

        // ==============================
        // KIỂM TRA ĐỊA CHỈ
        // ==============================

        if (address.isEmpty()) {

            showError(
                    edtRegisterAddress,
                    "Vui lòng nhập địa chỉ"
            );

            return;
        }

        // ==============================
        // KIỂM TRA USER ĐÃ TỒN TẠI
        // ==============================

        User oldUser =
                databaseHelper.getUser(username);

        if (oldUser != null) {

            edtRegisterUsername.setError(
                    "Tên đăng nhập đã tồn tại"
            );

            edtRegisterUsername.requestFocus();

            shakeView(edtRegisterUsername);

            Toast.makeText(
                    this,
                    "Tên đăng nhập đã tồn tại",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // ==============================
        // ĐĂNG KÝ DATABASE
        // ==============================

        long result =
                databaseHelper.registerUser(
                        username,
                        password,
                        fullName,
                        phone,
                        address
                );

        // ==============================
        // THÀNH CÔNG
        // ==============================

        if (result > 0) {

            Toast.makeText(
                    this,
                    "Đăng ký tài khoản thành công! 🎉",
                    Toast.LENGTH_LONG
            ).show();

            Intent intent =
                    new Intent(
                            RegisterActivity.this,
                            LoginActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            );

            startActivity(intent);

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Đăng ký thất bại",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // =========================================================
    // HIỆN / ẨN PASSWORD
    // =========================================================

    private void togglePassword() {

        if (passwordVisible) {

            edtRegisterPassword.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
            );

            tvShowRegisterPassword.setText("👁");

            passwordVisible = false;

        } else {

            edtRegisterPassword.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            );

            tvShowRegisterPassword.setText("🙈");

            passwordVisible = true;
        }

        edtRegisterPassword.setSelection(
                edtRegisterPassword.length()
        );

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

        tvShowRegisterPassword.startAnimation(animation);
    }

    // =========================================================
    // HIỆU ỨNG FOCUS
    // =========================================================

    private void setFocusAnimation(EditText editText) {

        editText.setOnFocusChangeListener(
                (v, hasFocus) -> {

                    if (hasFocus) {

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

                        editText.startAnimation(animation);
                    }
                }
        );
    }

    // =========================================================
    // HIỆU ỨNG BUTTON
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
    // HIỆU ỨNG LỖI
    // =========================================================

    private void showError(
            EditText editText,
            String message
    ) {

        editText.setError(message);

        editText.requestFocus();

        shakeView(editText);
    }

    private void shakeView(View view) {

        TranslateAnimation animation =
                new TranslateAnimation(
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

        finish();

        overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
    }
}