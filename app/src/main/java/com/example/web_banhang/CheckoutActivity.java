package com.example.web_banhang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtPhone;
    private EditText edtAddress;

    private RadioGroup radioPayment;

    private RadioButton radioCOD;
    private RadioButton radioBank;

    private TextView tvCheckoutTotal;

    private Button btnConfirmOrder;

    private DatabaseHelper databaseHelper;

    private ArrayList<CartItem> cartList;

    private String username;

    private double totalMoney = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkout);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);

        radioPayment = findViewById(R.id.radioPayment);

        radioCOD = findViewById(R.id.radioCOD);
        radioBank = findViewById(R.id.radioBank);

        tvCheckoutTotal = findViewById(R.id.tvCheckoutTotal);

        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        databaseHelper = new DatabaseHelper(this);

        username = getIntent().getStringExtra("username");

        if (username == null || username.trim().isEmpty()) {
            username = "user";
        }

        loadCart();

        btnConfirmOrder.setOnClickListener(v -> confirmOrder());
    }

    private void loadCart() {

        cartList = databaseHelper.getCartItems(username);

        if (cartList == null || cartList.isEmpty()) {

            Toast.makeText(
                    this,
                    "Giỏ hàng đang trống",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        totalMoney = 0;

        for (CartItem item : cartList) {
            totalMoney += item.getTotalPrice();
        }

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        tvCheckoutTotal.setText(
                "Tổng tiền: " +
                        formatter.format(totalMoney)
        );
    }

    private void confirmOrder() {

        String name =
                edtName.getText()
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

        if (name.isEmpty()) {

            edtName.setError(
                    "Vui lòng nhập họ tên"
            );

            edtName.requestFocus();

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

        int selectedId =
                radioPayment.getCheckedRadioButtonId();

        if (selectedId == -1) {

            Toast.makeText(
                    this,
                    "Vui lòng chọn phương thức thanh toán",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String paymentMethod;

        if (selectedId == R.id.radioCOD) {

            paymentMethod =
                    "Thanh toán khi nhận hàng";

        } else {

            paymentMethod =
                    "Chuyển khoản ngân hàng";
        }

        // Xóa giỏ hàng sau khi đặt hàng
        long orderId =
                databaseHelper.createOrder(
                        username,
                        name,
                        phone,
                        address,
                        paymentMethod,
                        totalMoney,
                        cartList
                );

        if (orderId > 0) {

            Toast.makeText(
                    CheckoutActivity.this,
                    "Đặt hàng thành công!",
                    Toast.LENGTH_LONG
            ).show();

            Intent intent =
                    new Intent(
                            CheckoutActivity.this,
                            UserActivity.class
                    );

            intent.putExtra(
                    "username",
                    username
            );

            startActivity(intent);

            finish();

        } else if (orderId == -2) {

            Toast.makeText(
                    CheckoutActivity.this,
                    "Số lượng sản phẩm không đủ trong kho",
                    Toast.LENGTH_LONG
            ).show();

        } else {

            Toast.makeText(
                    CheckoutActivity.this,
                    "Không thể tạo đơn hàng",
                    Toast.LENGTH_LONG
            ).show();
        }

        Toast.makeText(
                this,
                "Đặt hàng thành công!",
                Toast.LENGTH_LONG
        ).show();

        Intent intent =
                new Intent(
                        CheckoutActivity.this,
                        UserActivity.class
                );

        intent.putExtra(
                "username",
                username
        );

        startActivity(intent);

        finish();
    }
}