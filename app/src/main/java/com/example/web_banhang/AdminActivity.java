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

        btnDanhSach =
                findViewById(R.id.btnDanhSach);

        btnThemSanPham =
                findViewById(R.id.btnThemSanPham);

        btnDangXuat =
                findViewById(R.id.btnDangXuat);

        recyclerProducts =
                findViewById(R.id.recyclerProducts);

        // =========================
        // DATABASE
        // =========================

        databaseHelper =
                new DatabaseHelper(this);

        // =========================
        // NÚT THÊM
        // =========================

        btnThemSanPham.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminActivity.this,
                    AddProductActivity.class
            );

            startActivity(intent);
        });

        // =========================
        // NÚT DANH SÁCH
        // =========================

        btnDanhSach.setOnClickListener(v -> {

            loadProducts();

        });

        // =========================
        // ĐĂNG XUẤT
        // =========================

        btnDangXuat.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);

            finish();
        });

        // =========================
        // LOAD SẢN PHẨM
        // =========================

        loadProducts();
    }

    // =========================
    // LOAD DATABASE
    // =========================

    private void loadProducts() {

        productList =
                databaseHelper.getAllProducts();

        adapter =
                new ProductAdminAdapter(
                        productList,
                        new ProductAdminAdapter.OnProductActionListener() {

                            @Override
                            public void onEdit(
                                    Product product) {

                                editProduct(product);
                            }

                            @Override
                            public void onDelete(
                                    Product product) {

                                deleteProduct(product);
                            }
                        }
                );

        recyclerProducts.setAdapter(adapter);
    }

    // =========================
    // XÓA SẢN PHẨM
    // =========================

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

    // =========================
    // SỬA SẢN PHẨM
    // =========================

    private void editProduct(Product product) {

        Intent intent = new Intent(
                AdminActivity.this,
                EditProductActivity.class
        );

        intent.putExtra("product_id", product.getId());

        startActivity(intent);
    }

    // =========================
    // KHI QUAY LẠI ADMIN
    // =========================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {
            loadProducts();
        }
    }
}