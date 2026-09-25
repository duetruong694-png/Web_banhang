package com.example.web_banhang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnLogout;
    private Button btnCart;

    private TextView tvWelcome;
    private EditText edtSearch;

    private RecyclerView recyclerProducts;

    private DatabaseHelper databaseHelper;
    private ProductAdapter adapter;

    private ArrayList<Product> productList;

    private String username = null;
    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        // =========================
        // ÁNH XẠ VIEW
        // =========================

        btnLogin = findViewById(R.id.btnLogin);
        btnLogout = findViewById(R.id.btnLogout);
        btnCart = findViewById(R.id.btnCart);

        tvWelcome = findViewById(R.id.tvWelcome);
        edtSearch = findViewById(R.id.edtSearch);

        recyclerProducts = findViewById(R.id.recyclerProducts);

        databaseHelper = new DatabaseHelper(this);

        productList = new ArrayList<>();

        // =========================
        // RECYCLER VIEW - 2 CỘT
        // =========================

        recyclerProducts.setLayoutManager(
                new GridLayoutManager(this, 2)
        );

        recyclerProducts.setHasFixedSize(false);

        // =========================
        // LOGIN
        // =========================

        loadLoginState();

        updateLoginUI();

        // =========================
        // LOAD SẢN PHẨM
        // =========================

        loadProducts();

        // =========================
        // ĐĂNG NHẬP
        // =========================

        btnLogin.setOnClickListener(v -> {

            openLogin();

        });

        // =========================
        // ĐĂNG XUẤT
        // =========================

        btnLogout.setOnClickListener(v -> {

            SharedPreferences preferences =
                    getSharedPreferences(
                            "LOGIN",
                            MODE_PRIVATE
                    );

            preferences.edit()
                    .clear()
                    .apply();

            username = null;
            isLoggedIn = false;

            updateLoginUI();

            loadProducts();
        });

        // =========================
        // GIỎ HÀNG
        // =========================

        btnCart.setOnClickListener(v -> {

            if (!isLoggedIn || username == null) {

                openLogin();

                return;
            }

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
        // TÌM KIẾM
        // =========================

        edtSearch.addTextChangedListener(
                new android.text.TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        searchProducts(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            android.text.Editable s
                    ) {
                    }
                }
        );

        // =========================
        // HIỆU ỨNG NÚT
        // =========================

        setupButtonEffect(btnLogin);

        setupButtonEffect(btnLogout);

        setupButtonEffect(btnCart);
    }

    // =========================================================
    // TRẠNG THÁI LOGIN
    // =========================================================

    private void loadLoginState() {

        SharedPreferences preferences =
                getSharedPreferences(
                        "LOGIN",
                        MODE_PRIVATE
                );

        isLoggedIn =
                preferences.getBoolean(
                        "isLoggedIn",
                        false
                );

        if (isLoggedIn) {

            username =
                    preferences.getString(
                            "username",
                            null
                    );

        } else {

            username = null;
        }
    }

    // =========================================================
    // CẬP NHẬT GIAO DIỆN LOGIN
    // =========================================================

    private void updateLoginUI() {

        if (isLoggedIn && username != null) {

            btnLogin.setVisibility(View.GONE);

            btnLogout.setVisibility(View.VISIBLE);

            tvWelcome.setVisibility(View.VISIBLE);

            tvWelcome.setText(
                    "Xin chào, " + username
            );

        } else {

            btnLogin.setVisibility(View.VISIBLE);

            btnLogout.setVisibility(View.GONE);

            tvWelcome.setVisibility(View.GONE);
        }
    }

    // =========================================================
    // LOAD SẢN PHẨM
    // =========================================================

    private void loadProducts() {

        productList.clear();

        ArrayList<Product> products =
                databaseHelper.getAllProducts();

        if (products != null) {

            productList.addAll(products);
        }

        adapter =
                new ProductAdapter(
                        productList,
                        username,
                        databaseHelper
                );

        recyclerProducts.setAdapter(adapter);
    }

    // =========================================================
    // TÌM KIẾM
    // =========================================================

    private void searchProducts(String keyword) {

        keyword =
                keyword.trim().toLowerCase();

        ArrayList<Product> filteredList =
                new ArrayList<>();

        ArrayList<Product> allProducts =
                databaseHelper.getAllProducts();

        if (allProducts == null) {
            return;
        }

        if (keyword.isEmpty()) {

            filteredList.addAll(
                    allProducts
            );

        } else {

            for (Product product : allProducts) {

                String name =
                        product.getName() == null
                                ? ""
                                : product.getName().toLowerCase();

                String description =
                        product.getDescription() == null
                                ? ""
                                : product.getDescription().toLowerCase();

                if (name.contains(keyword)
                        || description.contains(keyword)) {

                    filteredList.add(product);
                }
            }
        }

        adapter =
                new ProductAdapter(
                        filteredList,
                        username,
                        databaseHelper
                );

        recyclerProducts.setAdapter(adapter);
    }

    // =========================================================
    // MỞ LOGIN
    // =========================================================

    private void openLogin() {

        Intent intent =
                new Intent(
                        UserActivity.this,
                        LoginActivity.class
                );

        intent.putExtra(
                "from_home",
                true
        );

        startActivity(intent);
    }

    // =========================================================
    // HIỆU ỨNG BUTTON
    // =========================================================

    private void setupButtonEffect(Button button) {

        button.setOnTouchListener(
                new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(
                            View v,
                            MotionEvent event
                    ) {

                        switch (event.getAction()) {

                            // =========================
                            // NHẤN XUỐNG
                            // =========================

                            case MotionEvent.ACTION_DOWN:

                                v.animate()
                                        .scaleX(0.94f)
                                        .scaleY(0.94f)
                                        .setDuration(80)
                                        .start();

                                v.setAlpha(0.70f);

                                return false;

                            // =========================
                            // THẢ RA
                            // =========================

                            case MotionEvent.ACTION_UP:

                                v.animate()
                                        .scaleX(1.0f)
                                        .scaleY(1.0f)
                                        .setDuration(100)
                                        .start();

                                v.setAlpha(1.0f);

                                return false;

                            // =========================
                            // HỦY
                            // =========================

                            case MotionEvent.ACTION_CANCEL:

                                v.animate()
                                        .scaleX(1.0f)
                                        .scaleY(1.0f)
                                        .setDuration(100)
                                        .start();

                                v.setAlpha(1.0f);

                                return false;
                        }

                        return false;
                    }
                }
        );

        // =========================
        // HOVER
        // =========================

        button.setOnHoverListener(
                new View.OnHoverListener() {

                    @Override
                    public boolean onHover(
                            View v,
                            MotionEvent event
                    ) {

                        switch (event.getAction()) {

                            case MotionEvent.ACTION_HOVER_ENTER:

                                v.animate()
                                        .scaleX(1.05f)
                                        .scaleY(1.05f)
                                        .setDuration(150)
                                        .start();

                                v.setAlpha(0.82f);

                                break;

                            case MotionEvent.ACTION_HOVER_EXIT:

                                v.animate()
                                        .scaleX(1.0f)
                                        .scaleY(1.0f)
                                        .setDuration(150)
                                        .start();

                                v.setAlpha(1.0f);

                                break;
                        }

                        return false;
                    }
                }
        );
    }

    // =========================================================
    // RESUME
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        loadLoginState();

        updateLoginUI();

        loadProducts();
    }
}