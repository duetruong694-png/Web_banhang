package com.example.web_banhang;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class EditProductActivity extends AppCompatActivity {

    private EditText edtProductName;
    private EditText edtProductPrice;
    private EditText edtProductStock;
    private EditText edtProductDescription;

    private ImageView imgProductPreview;

    private Button btnChooseImage;
    private Button btnUpdateProduct;
    private Button btnCancel;

    private DatabaseHelper databaseHelper;

    private int productId;

    // Ảnh hiện tại
    private String selectedImageUri = null;

    // Chọn ảnh mới
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
                R.layout.activity_edit_product
        );

        // =========================
        // ÁNH XẠ
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

        btnUpdateProduct =
                findViewById(R.id.btnUpdateProduct);

        btnCancel =
                findViewById(R.id.btnCancel);

        databaseHelper =
                new DatabaseHelper(this);

        // =========================
        // NHẬN ID SẢN PHẨM
        // =========================

        productId =
                getIntent().getIntExtra(
                        "product_id",
                        -1
                );

        if (productId == -1) {

            Toast.makeText(
                    this,
                    "Không tìm thấy sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // =========================
        // LOAD SẢN PHẨM
        // =========================

        loadProduct();

        // =========================
        // ĐỔI ẢNH
        // =========================

        btnChooseImage.setOnClickListener(v -> {

            imagePicker.launch("image/*");

        });

        // =========================
        // LƯU
        // =========================

        btnUpdateProduct.setOnClickListener(
                v -> updateProduct()
        );

        // =========================
        // HỦY
        // =========================

        btnCancel.setOnClickListener(
                v -> finish()
        );
    }

    private void loadProduct() {

        Product product = null;

        for (Product p :
                databaseHelper.getAllProducts()) {

            if (p.getId() == productId) {

                product = p;
                break;
            }
        }

        if (product == null) {

            Toast.makeText(
                    this,
                    "Không tìm thấy sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // =========================
        // HIỂN THỊ DỮ LIỆU
        // =========================

        edtProductName.setText(
                product.getName()
        );

        edtProductPrice.setText(
                String.valueOf(
                        product.getPrice()
                )
        );

        edtProductStock.setText(
                String.valueOf(
                        product.getStock()
                )
        );

        edtProductDescription.setText(
                product.getDescription()
        );

        // =========================
        // HIỂN THỊ ẢNH CŨ
        // =========================

        selectedImageUri =
                product.getImageUri();

        if (selectedImageUri != null
                && !selectedImageUri.isEmpty()) {

            try {

                imgProductPreview.setImageURI(
                        Uri.parse(selectedImageUri)
                );

            } catch (Exception e) {

                imgProductPreview.setImageResource(
                        R.drawable.ic_launcher_foreground
                );
            }

        } else {

            imgProductPreview.setImageResource(
                    R.drawable.ic_launcher_foreground
            );
        }
    }

    private void updateProduct() {

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
            // TẠO PRODUCT MỚI
            // =========================

            Product product =
                    new Product(
                            productId,
                            name,
                            price,
                            description,
                            stock,
                            selectedImageUri
                    );

            // =========================
            // UPDATE DATABASE
            // =========================

            int result =
                    databaseHelper.updateProduct(
                            product
                    );

            if (result > 0) {

                Toast.makeText(
                        this,
                        "Cập nhật sản phẩm thành công",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Cập nhật sản phẩm thất bại",
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