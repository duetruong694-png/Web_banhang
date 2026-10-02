package com.example.web_banhang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private Button btnDanhSach;
    private Button btnThemSanPham;
    private Button btnDonHang;
    private Button btnDangXuat;

    private RecyclerView recyclerProducts;

    private DatabaseHelper databaseHelper;
    private ProductAdminAdapter adapter;
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);

        // ==============================
        // ÁNH XẠ VIEW
        // ==============================

        btnDanhSach = findViewById(R.id.btnDanhSach);
        btnThemSanPham = findViewById(R.id.btnThemSanPham);
        btnDonHang = findViewById(R.id.btnDonHang);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        recyclerProducts = findViewById(R.id.recyclerProducts);

        // ==============================
        // KIỂM TRA NÚT ĐĂNG XUẤT
        // ==============================

        if (btnDangXuat == null) {

            Toast.makeText(
                    AdminActivity.this,
                    "LỖI: Không tìm thấy btnDangXuat",
                    Toast.LENGTH_LONG
            ).show();

        } else {

            // Đảm bảo nút có thể click
            btnDangXuat.setClickable(true);
            btnDangXuat.setFocusable(true);
            btnDangXuat.setEnabled(true);

            // ==============================
            // CLICK ĐĂNG XUẤT
            // ==============================

            btnDangXuat.setOnClickListener(v -> {

                Toast.makeText(
                        AdminActivity.this,
                        "ĐÃ CLICK NÚT ĐĂNG XUẤT",
                        Toast.LENGTH_SHORT
                ).show();

                showLogoutDialog();
            });
        }

        // ==============================
        // RECYCLER VIEW
        // ==============================

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, 2);

        recyclerProducts.setLayoutManager(gridLayoutManager);
        recyclerProducts.setHasFixedSize(false);
        recyclerProducts.setNestedScrollingEnabled(true);

        // ==============================
        // DATABASE
        // ==============================

        databaseHelper = new DatabaseHelper(this);

        // ==============================
        // NÚT DANH SÁCH
        // ==============================

        btnDanhSach.setOnClickListener(v -> {

            Toast.makeText(
                    AdminActivity.this,
                    "Đang tải danh sách sản phẩm...",
                    Toast.LENGTH_SHORT
            ).show();

            loadProducts();
        });

        // ==============================
        // NÚT THÊM SẢN PHẨM
        // ==============================

        btnThemSanPham.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminActivity.this,
                            AddProductActivity.class
                    );

            startActivity(intent);
        });

        // ==============================
        // NÚT QUẢN LÝ ĐƠN HÀNG
        // ==============================

        btnDonHang.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminActivity.this,
                            OrderManagementActivity.class
                    );

            startActivity(intent);
        });

        // ==============================
        // LOAD SẢN PHẨM
        // ==============================

        loadProducts();
    }

    // =========================================================
    // HIỂN THỊ HỘP THOẠI ĐĂNG XUẤT
    // =========================================================

    private void showLogoutDialog() {

        new AlertDialog.Builder(AdminActivity.this)

                .setTitle("Đăng xuất")

                .setMessage(
                        "Bạn có chắc chắn muốn đăng xuất khỏi tài khoản Admin không?"
                )

                .setNegativeButton(
                        "Hủy",
                        null
                )

                .setPositiveButton(
                        "Đăng xuất",
                        (dialog, which) -> logout()
                )

                .show();
    }

    // =========================================================
    // ĐĂNG XUẤT
    // =========================================================

    private void logout() {

        // Lấy SharedPreferences LOGIN
        SharedPreferences preferences =
                getSharedPreferences(
                        "LOGIN",
                        MODE_PRIVATE
                );

        // Xóa toàn bộ trạng thái đăng nhập
        preferences.edit()
                .clear()
                .apply();

        // Thông báo
        Toast.makeText(
                AdminActivity.this,
                "Đăng xuất thành công",
                Toast.LENGTH_SHORT
        ).show();

        // Chuyển về LoginActivity
        Intent intent =
                new Intent(
                        AdminActivity.this,
                        LoginActivity.class
                );

        // Xóa AdminActivity khỏi back stack
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }

    // =========================================================
    // LOAD DANH SÁCH SẢN PHẨM
    // =========================================================

    private void loadProducts() {

        if (databaseHelper == null) {
            return;
        }

        productList =
                databaseHelper.getAllProducts();

        if (productList == null) {
            productList = new ArrayList<>();
        }

        adapter =
                new ProductAdminAdapter(
                        productList,
                        new ProductAdminAdapter.OnProductActionListener() {

                            @Override
                            public void onEdit(Product product) {
                                editProduct(product);
                            }

                            @Override
                            public void onDelete(Product product) {
                                deleteProduct(product);
                            }
                        }
                );

        recyclerProducts.setAdapter(adapter);

        if (productList.isEmpty()) {

            Toast.makeText(
                    AdminActivity.this,
                    "Chưa có sản phẩm nào",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // =========================================================
    // SỬA SẢN PHẨM
    // =========================================================

    private void editProduct(Product product) {

        if (product == null) {
            return;
        }

        Intent intent =
                new Intent(
                        AdminActivity.this,
                        EditProductActivity.class
                );

        intent.putExtra(
                "product_id",
                product.getId()
        );

        startActivity(intent);
    }

    // =========================================================
    // XÓA SẢN PHẨM
    // =========================================================

    private void deleteProduct(Product product) {

        if (product == null) {
            return;
        }

        new AlertDialog.Builder(AdminActivity.this)

                .setTitle("Xóa sản phẩm")

                .setMessage(
                        "Bạn có chắc muốn xóa \""
                                + product.getName()
                                + "\" không?"
                )

                .setNegativeButton(
                        "Hủy",
                        null
                )

                .setPositiveButton(
                        "Xóa",
                        (dialog, which) -> {

                            int result =
                                    databaseHelper.deleteProduct(
                                            product.getId()
                                    );

                            if (result > 0) {

                                Toast.makeText(
                                        AdminActivity.this,
                                        "Đã xóa sản phẩm",
                                        Toast.LENGTH_SHORT
                                ).show();

                                loadProducts();

                            } else {

                                Toast.makeText(
                                        AdminActivity.this,
                                        "Không thể xóa sản phẩm",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )

                .show();
    }

    // =========================================================
    // KHI QUAY LẠI ADMIN
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {
            loadProducts();
        }
    }
}