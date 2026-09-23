package com.example.web_banhang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private Button btnCart;
    private Button btnUserLogout;

    private RecyclerView recyclerProducts;

    private DatabaseHelper databaseHelper;

    private ProductAdapter adapter;

    private ArrayList<Product> productList;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_user
        );

        btnCart =
                findViewById(
                        R.id.btnCart
                );

        btnUserLogout =
                findViewById(
                        R.id.btnUserLogout
                );

        recyclerProducts =
                findViewById(
                        R.id.recyclerProducts
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

        // =========================
        // RECYCLER
        // =========================

        recyclerProducts.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // =========================
        // LOAD PRODUCT
        // =========================

        loadProducts();

        // =========================
        // GIỎ HÀNG
        // =========================

        btnCart.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            UserActivity.this,
                            CartActivity.class
                    );

            intent.putExtra(
                    "username",
                    username
            );

            startActivity(intent);
        });

        // =========================
        // ĐĂNG XUẤT
        // =========================

        btnUserLogout.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            UserActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);

            finish();
        });
    }

    // =========================
    // LOAD PRODUCT
    // =========================

    private void loadProducts() {

        productList =
                databaseHelper.getAllProducts();

        adapter =
                new ProductAdapter(
                        productList,
                        username,
                        databaseHelper
                );

        recyclerProducts.setAdapter(
                adapter
        );
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {

            loadProducts();
        }
    }
}