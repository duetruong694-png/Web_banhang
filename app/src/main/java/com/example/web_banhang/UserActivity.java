package com.example.web_banhang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.web_banhang.api.ApiService;
import com.example.web_banhang.api.ProductApi;
import com.example.web_banhang.api.ProductResponse;
import com.example.web_banhang.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private Button btnAccount;
    private TextView tvWelcome;
    private EditText edtSearch;
    private RecyclerView recyclerProducts;

    private ApiService apiService;

    private final ArrayList<ProductApi> productList =
            new ArrayList<>();

    private ProductApiAdapter adapter;

    private boolean isLoggedIn = false;
    private int userId = -1;
    private String username = "";
    private String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        // =========================
        // ÁNH XẠ VIEW
        // =========================

        btnAccount = findViewById(R.id.btnAccount);
        tvWelcome = findViewById(R.id.tvWelcome);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerProducts = findViewById(R.id.recyclerProducts);

        // =========================
        // RETROFIT
        // =========================

        apiService =
                RetrofitClient
                        .getClient()
                        .create(ApiService.class);

        // =========================
        // RECYCLER VIEW
        // =========================

        recyclerProducts.setLayoutManager(
                new GridLayoutManager(
                        this,
                        2
                )
        );

        recyclerProducts.setHasFixedSize(false);

        // =========================
        // LOAD LOGIN
        // =========================

        loadLoginState();

        updateLoginUI();

        // =========================
        // LOAD SẢN PHẨM MYSQL
        // =========================

        loadProducts();

        // =========================
        // TÀI KHOẢN
        // =========================

        btnAccount.setOnClickListener(v -> {

            setupButtonPress(v);

            v.postDelayed(
                    this::showAccountMenu,
                    100
            );
        });

        // =========================
        // TÌM KIẾM
        // =========================

        edtSearch.addTextChangedListener(
                new TextWatcher() {

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

                        filterProducts(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );

        setupButtonEffect(btnAccount);
    }

    // =========================================================
    // LOGIN STATE
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

        userId =
                preferences.getInt(
                        "user_id",
                        -1
                );

        username =
                preferences.getString(
                        "username",
                        ""
                );

        role =
                preferences.getString(
                        "role",
                        ""
                );

        if (username == null) {
            username = "";
        }

        if (role == null) {
            role = "";
        }
    }

    // =========================================================
    // UPDATE LOGIN UI
    // =========================================================

    private void updateLoginUI() {

        if (isLoggedIn && userId != -1) {

            tvWelcome.setVisibility(
                    View.VISIBLE
            );

            if (username.isEmpty()) {

                tvWelcome.setText(
                        "Xin chào!"
                );

            } else {

                tvWelcome.setText(
                        "Xin chào, " + username
                );
            }

            if ("admin".equalsIgnoreCase(role)) {

                btnAccount.setText(
                        "👤 Admin"
                );

            } else {

                btnAccount.setText(
                        "👤 Tài khoản"
                );
            }

        } else {

            tvWelcome.setVisibility(
                    View.VISIBLE
            );

            tvWelcome.setText(
                    "Chào mừng bạn đến với DAPP"
            );

            btnAccount.setText(
                    "👤 Tài khoản"
            );
        }
    }

    // =========================================================
    // ACCOUNT MENU
    // =========================================================

    private void showAccountMenu() {

        PopupMenu popupMenu =
                new PopupMenu(
                        this,
                        btnAccount,
                        Gravity.END
                );

        if (!isLoggedIn || userId == -1) {

            // =========================
            // CHƯA ĐĂNG NHẬP
            // =========================

            popupMenu.getMenu().add(
                    "🔐 Đăng nhập"
            );

            popupMenu.getMenu().add(
                    "📝 Đăng ký"
            );

            popupMenu.setOnMenuItemClickListener(
                    item -> {

                        String title =
                                item.getTitle().toString();

                        if (title.contains("Đăng nhập")) {

                            openLogin();

                        } else if (
                                title.contains("Đăng ký")
                        ) {

                            openRegister();
                        }

                        return true;
                    }
            );

        } else if (
                "admin".equalsIgnoreCase(role)
        ) {

            // =========================
            // ADMIN
            // =========================

            popupMenu.getMenu().add(
                    "👤 Thông tin tài khoản"
            );

            popupMenu.getMenu().add(
                    "🏠 Xem trang chủ"
            );

            popupMenu.getMenu().add(
                    "⚙️ Trang quản trị"
            );

            popupMenu.getMenu().add(
                    "📦 Lịch sử đơn hàng"
            );

            popupMenu.getMenu().add(
                    "🚪 Đăng xuất"
            );

            popupMenu.setOnMenuItemClickListener(
                    item -> {

                        String title =
                                item.getTitle().toString();

                        if (title.contains(
                                "Thông tin tài khoản"
                        )) {

                            openUserInfo();

                        } else if (
                                title.contains(
                                        "Xem trang chủ"
                                )
                        ) {

                            popupMenu.dismiss();

                        } else if (
                                title.contains(
                                        "Trang quản trị"
                                )
                        ) {

                            openAdmin();

                        } else if (
                                title.contains(
                                        "Lịch sử đơn hàng"
                                )
                        ) {

                            openAdminOrders();

                        } else if (
                                title.contains(
                                        "Đăng xuất"
                                )
                        ) {

                            logout();
                        }

                        return true;
                    }
            );

        } else {

            // =========================
            // USER
            // =========================

            popupMenu.getMenu().add(
                    "👤 Thông tin tài khoản"
            );

            popupMenu.getMenu().add(
                    "✏️ Sửa thông tin"
            );

            popupMenu.getMenu().add(
                    "🛒 Giỏ hàng"
            );

            popupMenu.getMenu().add(
                    "📦 Lịch sử mua hàng"
            );

            popupMenu.getMenu().add(
                    "🗑️ Xóa tài khoản"
            );

            popupMenu.getMenu().add(
                    "🚪 Đăng xuất"
            );

            popupMenu.setOnMenuItemClickListener(
                    item -> {

                        String title =
                                item.getTitle().toString();

                        if (title.contains(
                                "Thông tin tài khoản"
                        )) {

                            openUserInfo();

                        } else if (
                                title.contains(
                                        "Sửa thông tin"
                                )
                        ) {

                            openEditUser();

                        } else if (
                                title.contains(
                                        "Giỏ hàng"
                                )
                        ) {

                            openCart();

                        } else if (
                                title.contains(
                                        "Lịch sử mua hàng"
                                )
                        ) {

                            openOrderHistory();

                        } else if (
                                title.contains(
                                        "Xóa tài khoản"
                                )
                        ) {

                            deleteAccount();

                        } else if (
                                title.contains(
                                        "Đăng xuất"
                                )
                        ) {

                            logout();
                        }

                        return true;
                    }
            );
        }

        popupMenu.show();
    }

    // =========================================================
    // LOAD PRODUCTS FROM PHP
    // =========================================================

    private void loadProducts() {

        apiService
                .getProducts()
                .enqueue(
                        new Callback<ProductResponse>() {

                            @Override
                            public void onResponse(
                                    Call<ProductResponse> call,
                                    Response<ProductResponse> response
                            ) {

                                if (!response.isSuccessful()
                                        || response.body() == null) {

                                    Toast.makeText(
                                            UserActivity.this,
                                            "Không tải được sản phẩm",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    return;
                                }

                                ProductResponse result =
                                        response.body();

                                if (!result.isSuccess()) {

                                    Toast.makeText(
                                            UserActivity.this,
                                            result.getMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    return;
                                }

                                productList.clear();

                                List<ProductApi> products =
                                        result.getProducts();

                                if (products != null) {

                                    productList.addAll(
                                            products
                                    );
                                }

                                setupProductAdapter(
                                        productList
                                );
                            }

                            @Override
                            public void onFailure(
                                    Call<ProductResponse> call,
                                    Throwable t
                            ) {

                                Toast.makeText(
                                        UserActivity.this,
                                        "Không kết nối được máy chủ",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private void filterProducts(
            String keyword
    ) {

        String key =
                keyword
                        .trim()
                        .toLowerCase();

        ArrayList<ProductApi> filtered =
                new ArrayList<>();

        for (ProductApi product :
                productList) {

            String name =
                    product.getName() == null
                            ? ""
                            : product.getName()
                            .toLowerCase();

            String description =
                    product.getDescription() == null
                            ? ""
                            : product.getDescription()
                            .toLowerCase();

            String code =
                    product.getProductCode() == null
                            ? ""
                            : product.getProductCode()
                            .toLowerCase();

            if (key.isEmpty()
                    || name.contains(key)
                    || description.contains(key)
                    || code.contains(key)) {

                filtered.add(product);
            }
        }

        setupProductAdapter(filtered);
    }

    // =========================================================
    // PRODUCT ADAPTER
    // =========================================================

    private void setupProductAdapter(
            ArrayList<ProductApi> list
    ) {

        adapter =
                new ProductApiAdapter(
                        this,
                        list,
                        isLoggedIn,
                        userId,
                        role
                );

        recyclerProducts.setAdapter(
                adapter
        );
    }

    // =========================================================
    // LOGIN
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
    // REGISTER
    // =========================================================

    private void openRegister() {

        Intent intent =
                new Intent(
                        UserActivity.this,
                        RegisterActivity.class
                );

        startActivity(intent);
    }

    // =========================================================
    // USER INFO
    // =========================================================

    private void openUserInfo() {

        Intent intent =
                new Intent(
                        UserActivity.this,
                        UserInfoActivity.class
                );

        intent.putExtra(
                "user_id",
                userId
        );

        startActivity(intent);
    }

    // =========================================================
    // EDIT USER
    // =========================================================

    private void openEditUser() {

        Intent intent =
                new Intent(
                        UserActivity.this,
                        EditUserActivity.class
                );

        intent.putExtra(
                "user_id",
                userId
        );

        startActivity(intent);
    }

    // =========================================================
    // CART
    // =========================================================

    private void openCart() {

        Intent intent =
                new Intent(
                        UserActivity.this,
                        CartActivity.class
                );

        intent.putExtra(
                "user_id",
                userId
        );

        intent.putExtra(
                "username",
                username
        );

        startActivity(intent);
    }

    // =========================================================
    // ORDER HISTORY
    // =========================================================

    private void openOrderHistory() {

        Intent intent =
                new Intent(
                        UserActivity.this,
                        OrderHistoryActivity.class
                );

        intent.putExtra(
                "user_id",
                userId
        );

        startActivity(intent);
    }

    // =========================================================
    // ADMIN
    // =========================================================

    private void openAdmin() {

        Intent intent =
                new Intent(
                        UserActivity.this,
                        AdminActivity.class
                );

        intent.putExtra(
                "user_id",
                userId
        );

        startActivity(intent);
    }

    // =========================================================
    // ADMIN ORDERS
    // =========================================================

    private void openAdminOrders() {

        Intent intent =
                new Intent(
                        UserActivity.this,
                        AdminOrderActivity.class
                );

        intent.putExtra(
                "user_id",
                userId
        );

        startActivity(intent);
    }

    // =========================================================
    // DELETE ACCOUNT
    // =========================================================

    private void deleteAccount() {

        Toast.makeText(
                this,
                "Chức năng xóa tài khoản sẽ được xác nhận ở màn hình tài khoản",
                Toast.LENGTH_SHORT
        ).show();

        openUserInfo();
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        SharedPreferences preferences =
                getSharedPreferences(
                        "LOGIN",
                        MODE_PRIVATE
                );

        preferences.edit()
                .clear()
                .apply();

        isLoggedIn = false;
        userId = -1;
        username = "";
        role = "";

        updateLoginUI();

        Toast.makeText(
                this,
                "Đã đăng xuất",
                Toast.LENGTH_SHORT
        ).show();

        loadProducts();
    }

    // =========================================================
    // BUTTON PRESS
    // =========================================================

    private void setupButtonPress(
            View view
    ) {

        view.animate()
                .scaleX(0.94f)
                .scaleY(0.94f)
                .setDuration(80)
                .withEndAction(() -> {

                    view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();

                })
                .start();
    }

    // =========================================================
    // BUTTON EFFECT
    // =========================================================

    private void setupButtonEffect(
            Button button
    ) {

        button.setOnTouchListener(
                (v, event) -> {

                    switch (
                            event.getAction()
                    ) {

                        case MotionEvent.ACTION_DOWN:

                            v.animate()
                                    .scaleX(0.94f)
                                    .scaleY(0.94f)
                                    .setDuration(80)
                                    .start();

                            v.setAlpha(0.75f);

                            break;

                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:

                            v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                                    .start();

                            v.setAlpha(1f);

                            break;
                    }

                    return false;
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