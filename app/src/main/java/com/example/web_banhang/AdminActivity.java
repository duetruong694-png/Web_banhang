package com.example.web_banhang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

        // =========================
        // ÁNH XẠ
        // =========================

        btnDanhSach = findViewById(R.id.btnDanhSach);
        btnThemSanPham = findViewById(R.id.btnThemSanPham);
        btnDonHang = findViewById(R.id.btnDonHang);
        btnDangXuat = findViewById(R.id.btnDangXuat);

        recyclerProducts = findViewById(
                R.id.recyclerProducts
        );

        databaseHelper = new DatabaseHelper(this);

        // =========================
        // DANH SÁCH SẢN PHẨM
        // =========================

        btnDanhSach.setOnClickListener(
                v -> loadProducts()
        );

        // =========================
        // THÊM SẢN PHẨM
        // =========================

        btnThemSanPham.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminActivity.this,
                            AddProductActivity.class
                    );

            startActivity(intent);
        });

        // =========================
        // QUẢN LÝ ĐƠN HÀNG
        // =========================

        btnDonHang.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminActivity.this,
                            OrderManagementActivity.class
                    );

            startActivity(intent);
        });

        // =========================
        // ĐĂNG XUẤT
        // =========================

        btnDangXuat.setOnClickListener(v -> {

            getSharedPreferences(
                    "LOGIN",
                    MODE_PRIVATE
            )
                    .edit()
                    .clear()
                    .apply();

            Intent intent =
                    new Intent(
                            AdminActivity.this,
                            LoginActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();
        });

        // =========================
        // LOAD
        // =========================

        loadProducts();
    }

    // =====================================================
    // LOAD PRODUCT
    // =====================================================

    private void loadProducts() {

        productList =
                databaseHelper.getAllProducts();

        adapter =
                new ProductAdminAdapter(
                        productList,
                        new ProductAdminAdapter.OnProductActionListener() {

                            @Override
                            public void onEdit(
                                    Product product
                            ) {
                                editProduct(product);
                            }

                            @Override
                            public void onDelete(
                                    Product product
                            ) {
                                deleteProduct(product);
                            }
                        }
                );

        recyclerProducts.setAdapter(adapter);
    }

    // =====================================================
    // DELETE
    // =====================================================

    private void deleteProduct(Product product) {

        new androidx.appcompat.app.AlertDialog.Builder(this)

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

                            databaseHelper.deleteProduct(
                                    product.getId()
                            );

                            Toast.makeText(
                                    AdminActivity.this,
                                    "Đã xóa sản phẩm",
                                    Toast.LENGTH_SHORT
                            ).show();

                            loadProducts();
                        }
                )

                .show();
    }

    // =====================================================
    // EDIT
    // =====================================================

    private void editProduct(Product product) {

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

    // =====================================================
    // RESUME
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {
            loadProducts();
        }
    }
}