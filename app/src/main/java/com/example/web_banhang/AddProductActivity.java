package com.example.web_banhang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {

    private EditText edtProductName;
    private EditText edtProductPrice;
    private EditText edtProductStock;
    private EditText edtProductDescription;

    private ImageView imgProductPreview;

    private Button btnChooseImage;
    private Button btnSaveProduct;
    private Button btnCancel;

    private DatabaseHelper databaseHelper;

    // Lưu đường dẫn ảnh được chọn
    private String selectedImageUri = null;

    // Bộ chọn ảnh
    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri != null) {

                            selectedImageUri =
                                    uri.toString();

                            imgProductPreview.setImageURI(uri);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_product
        );

        // =========================
        // ÁNH XẠ GIAO DIỆN
        // =========================

        edtProductName =
                findViewById(R.id.edtProductName);

        edtProductPrice =
                findViewById(R.id.edtProductPrice);

        edtProductStock =
                findViewById(R.id.edtProductStock);

        edtProductDescription =
                findViewById(R.id.edtProductDescription);

        imgProductPreview =
                findViewById(R.id.imgProductPreview);

        btnChooseImage =
                findViewById(R.id.btnChooseImage);

        btnSaveProduct =
                findViewById(R.id.btnSaveProduct);

        btnCancel =
                findViewById(R.id.btnCancel);

        databaseHelper =
                new DatabaseHelper(this);

        // =========================
        // CHỌN ẢNH
        // =========================

        btnChooseImage.setOnClickListener(v -> {

            imagePicker.launch("image/*");

        });

        // =========================
        // LƯU
        // =========================

        btnSaveProduct.setOnClickListener(
                v -> saveProduct()
        );

        // =========================
        // HỦY
        // =========================

        btnCancel.setOnClickListener(
                v -> finish()
        );
    }

    private void saveProduct() {

        String name =
                edtProductName.getText()
                        .toString()
                        .trim();

        String priceText =
                edtProductPrice.getText()
                        .toString()
                        .trim();

        String stockText =
                edtProductStock.getText()
                        .toString()
                        .trim();

        String description =
                edtProductDescription.getText()
                        .toString()
                        .trim();

        // =========================
        // KIỂM TRA
        // =========================

        if (name.isEmpty()
                || priceText.isEmpty()
                || stockText.isEmpty()) {

            Toast.makeText(
                    this,
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        try {

            double price =
                    Double.parseDouble(priceText);

            int stock =
                    Integer.parseInt(stockText);

            if (price < 0) {

                Toast.makeText(
                        this,
                        "Giá không được nhỏ hơn 0",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (stock < 0) {

                Toast.makeText(
                        this,
                        "Số lượng không được nhỏ hơn 0",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // =========================
            // TẠO SẢN PHẨM
            // =========================

            Product product =
                    new Product(
                            name,
                            price,
                            description,
                            stock,
                            selectedImageUri
                    );

            // =========================
            // LƯU DATABASE
            // =========================

            long result =
                    databaseHelper.addProduct(product);

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Thêm sản phẩm thành công",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Thêm sản phẩm thất bại",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Giá hoặc số lượng không hợp lệ",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}