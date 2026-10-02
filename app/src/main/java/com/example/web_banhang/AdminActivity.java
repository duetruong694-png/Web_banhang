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

import com.example.web_banhang.api.ApiService;
import com.example.web_banhang.api.BasicResponse;
import com.example.web_banhang.api.ProductApi;
import com.example.web_banhang.api.ProductResponse;
import com.example.web_banhang.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private Button btnDanhSach;
    private Button btnThemSanPham;
    private Button btnDonHang;
    private Button btnDangXuat;

    private RecyclerView recyclerProducts;

    private ApiService apiService;

    private ProductAdminApiAdapter adapter;

    private final ArrayList<ProductApi> productList =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);

        // =====================================================
        // ÁNH XẠ VIEW
        // =====================================================

        btnDanhSach = findViewById(R.id.btnDanhSach);
        btnThemSanPham = findViewById(R.id.btnThemSanPham);
        btnDonHang = findViewById(R.id.btnDonHang);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        recyclerProducts = findViewById(R.id.recyclerProducts);

        // =====================================================
        // RETROFIT
        // =====================================================

        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        // =====================================================
        // RECYCLER VIEW
        // =====================================================

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, 2);

        recyclerProducts.setLayoutManager(
                gridLayoutManager
        );

        recyclerProducts.setHasFixedSize(false);

        // =====================================================
        // ADAPTER
        // =====================================================

        adapter = new ProductAdminApiAdapter(
                productList,
                new ProductAdminApiAdapter.OnProductActionListener() {

                    @Override
                    public void onEdit(ProductApi product) {
                        editProduct(product);
                    }

                    @Override
                    public void onDelete(ProductApi product) {
                        deleteProduct(product);
                    }
                }
        );

        recyclerProducts.setAdapter(adapter);

        // =====================================================
        // DANH SÁCH SẢN PHẨM
        // =====================================================

        btnDanhSach.setOnClickListener(v -> {
            loadProducts();
        });

        // =====================================================
        // THÊM SẢN PHẨM
        // =====================================================

        btnThemSanPham.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminActivity.this,
                    AddProductActivity.class
            );

            startActivity(intent);
        });

        // =====================================================
        // QUẢN LÝ ĐƠN HÀNG
        // =====================================================

        btnDonHang.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminActivity.this,
                    AdminOrderActivity.class
            );

            startActivity(intent);
        });

        // =====================================================
        // ĐĂNG XUẤT
        // =====================================================

        btnDangXuat.setOnClickListener(v -> {
            showLogoutDialog();
        });

        // =====================================================
        // LOAD SẢN PHẨM
        // =====================================================

        loadProducts();
    }

    // =========================================================
    // LOAD SẢN PHẨM TỪ MYSQL
    // =========================================================

    private void loadProducts() {

        apiService.getProducts()
                .enqueue(new Callback<ProductResponse>() {

                    @Override
                    public void onResponse(
                            Call<ProductResponse> call,
                            Response<ProductResponse> response
                    ) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    AdminActivity.this,
                                    "Không tải được sản phẩm",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        ProductResponse result =
                                response.body();

                        if (!result.isSuccess()) {

                            Toast.makeText(
                                    AdminActivity.this,
                                    result.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        List<ProductApi> products =
                                result.getProducts();

                        productList.clear();

                        if (products != null) {
                            productList.addAll(products);
                        }

                        adapter.notifyDataSetChanged();

                        if (productList.isEmpty()) {

                            Toast.makeText(
                                    AdminActivity.this,
                                    "Chưa có sản phẩm nào",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Toast.makeText(
                                    AdminActivity.this,
                                    "Đã tải "
                                            + productList.size()
                                            + " sản phẩm",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ProductResponse> call,
                            Throwable t
                    ) {

                        Toast.makeText(
                                AdminActivity.this,
                                "Lỗi kết nối: "
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    // =========================================================
    // SỬA SẢN PHẨM
    // =========================================================

    private void editProduct(ProductApi product) {

        if (product == null) {
            return;
        }

        Intent intent = new Intent(
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

    private void deleteProduct(ProductApi product) {

        if (product == null) {
            return;
        }

        new AlertDialog.Builder(this)

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

                            apiService.deleteProduct(
                                    product.getId()
                            ).enqueue(
                                    new Callback<BasicResponse>() {

                                        @Override
                                        public void onResponse(
                                                Call<BasicResponse> call,
                                                Response<BasicResponse> response
                                        ) {

                                            if (!response.isSuccessful()
                                                    || response.body() == null) {

                                                Toast.makeText(
                                                        AdminActivity.this,
                                                        "Xóa sản phẩm thất bại",
                                                        Toast.LENGTH_SHORT
                                                ).show();

                                                return;
                                            }

                                            BasicResponse result =
                                                    response.body();

                                            if (result.isSuccess()) {

                                                Toast.makeText(
                                                        AdminActivity.this,
                                                        "Đã xóa sản phẩm",
                                                        Toast.LENGTH_SHORT
                                                ).show();

                                                loadProducts();

                                            } else {

                                                Toast.makeText(
                                                        AdminActivity.this,
                                                        result.getMessage(),
                                                        Toast.LENGTH_SHORT
                                                ).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(
                                                Call<BasicResponse> call,
                                                Throwable t
                                        ) {

                                            Toast.makeText(
                                                    AdminActivity.this,
                                                    "Lỗi kết nối: "
                                                            + t.getMessage(),
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    }
                            );
                        }
                )
                .show();
    }

    // =========================================================
    // HỘP THOẠI ĐĂNG XUẤT
    // =========================================================

    private void showLogoutDialog() {

        new AlertDialog.Builder(this)

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

        // Xóa USER
        getSharedPreferences(
                "USER",
                MODE_PRIVATE
        ).edit()
                .clear()
                .apply();

        // Xóa LoginPrefs
        getSharedPreferences(
                "LoginPrefs",
                MODE_PRIVATE
        ).edit()
                .clear()
                .apply();

        // Xóa LOGIN cũ nếu còn
        getSharedPreferences(
                "LOGIN",
                MODE_PRIVATE
        ).edit()
                .clear()
                .apply();

        Toast.makeText(
                this,
                "Đăng xuất thành công",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(
                AdminActivity.this,
                LoginActivity.class
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }

    // =========================================================
    // KHI QUAY LẠI ADMIN
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (apiService != null) {
            loadProducts();
        }
    }
}