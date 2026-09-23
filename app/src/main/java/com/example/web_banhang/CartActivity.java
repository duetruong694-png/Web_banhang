package com.example.web_banhang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;

    private TextView tvCartUsername;
    private TextView tvCartTotalMoney;

    private Button btnCheckout;
    private Button btnBackToShop;

    private DatabaseHelper databaseHelper;

    private CartAdapter cartAdapter;

    private ArrayList<CartItem> cartList;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_cart
        );

        recyclerCart =
                findViewById(
                        R.id.recyclerCart
                );

        tvCartUsername =
                findViewById(
                        R.id.tvCartUsername
                );

        tvCartTotalMoney =
                findViewById(
                        R.id.tvCartTotalMoney
                );

        btnCheckout =
                findViewById(
                        R.id.btnCheckout
                );

        btnBackToShop =
                findViewById(
                        R.id.btnBackToShop
                );

        databaseHelper =
                new DatabaseHelper(this);

        // =========================
        // LẤY USERNAME
        // =========================

        username =
                getIntent()
                        .getStringExtra(
                                "username"
                        );

        if (username == null ||
                username.trim().isEmpty()) {

            username = "user";
        }

        tvCartUsername.setText(
                "Tài khoản: " +
                        username
        );

        // =========================
        // RECYCLER VIEW
        // =========================

        recyclerCart.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // =========================
        // LOAD GIỎ
        // =========================

        loadCart();

        // =========================
        // THANH TOÁN
        // =========================

        btnCheckout.setOnClickListener(v -> {

            if (cartList == null || cartList.isEmpty()) {

                Toast.makeText(
                        CartActivity.this,
                        "Giỏ hàng đang trống",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent =
                    new Intent(
                            CartActivity.this,
                            CheckoutActivity.class
                    );

            intent.putExtra(
                    "username",
                    username
            );

            startActivity(intent);
        });

        // =========================
        // QUAY LẠI SHOP
        // =========================

        btnBackToShop.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            CartActivity.this,
                            UserActivity.class
                    );

            intent.putExtra(
                    "username",
                    username
            );

            startActivity(intent);

            finish();
        });
    }

    // =========================
    // LOAD CART
    // =========================

    private void loadCart() {

        cartList =
                databaseHelper.getCartItems(
                        username
                );

        cartAdapter =
                new CartAdapter(
                        cartList,
                        databaseHelper
                );

        recyclerCart.setAdapter(
                cartAdapter
        );

        calculateTotal();
    }

    // =========================
    // TÍNH TỔNG TIỀN
    // =========================

    private void calculateTotal() {

        double totalMoney = 0;

        if (cartList != null) {

            for (CartItem item :
                    cartList) {

                totalMoney +=
                        item.getTotalPrice();
            }
        }

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        tvCartTotalMoney.setText(
                "Tổng tiền: " +
                        formatter.format(
                                totalMoney
                        )
        );
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {

            loadCart();
        }
    }
}