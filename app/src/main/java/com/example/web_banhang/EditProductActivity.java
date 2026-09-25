package com.example.web_banhang;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class EditProductActivity extends AppCompatActivity {

    private EditText edtProductCode;
    private EditText edtProductName;
    private EditText edtProductPrice;
    private EditText edtProductStock;
    private EditText edtSaleDate;
    private EditText edtProductDescription;

    private Spinner spinnerStatus;

    private ImageView imgProductPreview;

    private Button btnChooseImage;
    private Button btnUpdateProduct;
    private Button btnCancel;

    private DatabaseHelper databaseHelper;

    private int productId;

    private Product currentProduct;

    private String selectedImagePath = null;

    // =========================================================
    // CHỌN ẢNH
    // =========================================================

    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri == null) {
                            return;
                        }

                        String savedPath =
                                copyImageToInternalStorage(uri);

                        if (savedPath != null) {

                            selectedImagePath =
                                    savedPath;

                            imgProductPreview
                                    .setImageURI(
                                            Uri.fromFile(
                                                    new File(
                                                            savedPath
                                                    )
                                            )
                                    );

                            Toast.makeText(
                                    EditProductActivity.this,
                                    "Đã chọn ảnh mới",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Toast.makeText(
                                    EditProductActivity.this,
                                    "Không thể lưu ảnh",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
            );

    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_edit_product
        );

        databaseHelper =
                new DatabaseHelper(this);

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

        // -----------------------------------------------------
        // VIEW
        // -----------------------------------------------------

        edtProductCode =
                findViewById(
                        R.id.edtProductCode
                );

        edtProductName =
                findViewById(
                        R.id.edtProductName
                );

        edtProductPrice =
                findViewById(
                        R.id.edtProductPrice
                );

        edtProductStock =
                findViewById(
                        R.id.edtProductStock
                );

        edtSaleDate =
                findViewById(
                        R.id.edtSaleDate
                );

        edtProductDescription =
                findViewById(
                        R.id.edtProductDescription
                );

        spinnerStatus =
                findViewById(
                        R.id.spinnerStatus
                );

        imgProductPreview =
                findViewById(
                        R.id.imgProductPreview
                );

        btnChooseImage =
                findViewById(
                        R.id.btnChooseImage
                );

        btnUpdateProduct =
                findViewById(
                        R.id.btnUpdateProduct
                );

        btnCancel =
                findViewById(
                        R.id.btnCancel
                );

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        String[] statuses = {
                "Đang bán",
                "Ngừng bán"
        };

        ArrayAdapter<String> statusAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        statuses
                );

        statusAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerStatus.setAdapter(
                statusAdapter
        );

        // -----------------------------------------------------
        // DATE
        // -----------------------------------------------------

        edtSaleDate.setFocusable(false);
        edtSaleDate.setClickable(true);

        edtSaleDate.setOnClickListener(v ->
                showDatePicker()
        );

        // -----------------------------------------------------
        // IMAGE
        // -----------------------------------------------------

        btnChooseImage.setOnClickListener(v ->
                imagePicker.launch("image/*")
        );

        // -----------------------------------------------------
        // UPDATE
        // -----------------------------------------------------

        btnUpdateProduct.setOnClickListener(v ->
                updateProduct()
        );

        // -----------------------------------------------------
        // CANCEL
        // -----------------------------------------------------

        btnCancel.setOnClickListener(v ->
                finish()
        );

        loadProduct();
    }

    // =========================================================
    // LOAD PRODUCT
    // =========================================================

    private void loadProduct() {

        currentProduct =
                databaseHelper.getProductById(
                        productId
                );

        if (currentProduct == null) {

            Toast.makeText(
                    this,
                    "Không tìm thấy sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        edtProductCode.setText(
                currentProduct.getProductCode()
        );

        edtProductName.setText(
                currentProduct.getName()
        );

        edtProductPrice.setText(
                String.valueOf(
                        currentProduct.getPrice()
                )
        );

        edtProductStock.setText(
                String.valueOf(
                        currentProduct.getStock()
                )
        );

        edtSaleDate.setText(
                currentProduct.getSaleDate()
        );

        edtProductDescription.setText(
                currentProduct.getDescription()
        );

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        String status =
                currentProduct.getStatus();

        if ("Ngừng bán".equals(status)) {

            spinnerStatus.setSelection(1);

        } else {

            spinnerStatus.setSelection(0);
        }

        // -----------------------------------------------------
        // IMAGE
        // -----------------------------------------------------

        selectedImagePath =
                currentProduct.getImageUri();

        loadImage(
                selectedImagePath
        );
    }

    // =========================================================
    // LOAD IMAGE
    // =========================================================

    private void loadImage(
            String imagePath
    ) {

        if (imagePath != null
                && !imagePath.trim().isEmpty()) {

            try {

                File file =
                        new File(imagePath);

                if (file.exists()) {

                    imgProductPreview
                            .setImageURI(
                                    Uri.fromFile(file)
                            );

                    return;
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        imgProductPreview.setImageResource(
                android.R.drawable.ic_menu_gallery
        );
    }

    // =========================================================
    // DATE PICKER
    // =========================================================

    private void showDatePicker() {

        Calendar calendar =
                Calendar.getInstance();

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog =
                new DatePickerDialog(
                        this,
                        (view, selectedYear,
                         selectedMonth,
                         selectedDay) -> {

                            String date =
                                    String.format(
                                            "%02d/%02d/%04d",
                                            selectedDay,
                                            selectedMonth + 1,
                                            selectedYear
                                    );

                            edtSaleDate.setText(date);
                        },
                        year,
                        month,
                        day
                );

        dialog.show();
    }

    // =========================================================
    // UPDATE
    // =========================================================

    private void updateProduct() {

        String productCode =
                edtProductCode.getText()
                        .toString()
                        .trim();

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

        String saleDate =
                edtSaleDate.getText()
                        .toString()
                        .trim();

        String description =
                edtProductDescription.getText()
                        .toString()
                        .trim();

        String status =
                spinnerStatus
                        .getSelectedItem()
                        .toString();

        // -----------------------------------------------------
        // VALIDATE
        // -----------------------------------------------------

        if (productCode.isEmpty()) {

            edtProductCode.setError(
                    "Vui lòng nhập mã sản phẩm"
            );

            return;
        }

        if (name.isEmpty()) {

            edtProductName.setError(
                    "Vui lòng nhập tên sản phẩm"
            );

            return;
        }

        if (priceText.isEmpty()) {

            edtProductPrice.setError(
                    "Vui lòng nhập giá"
            );

            return;
        }

        if (stockText.isEmpty()) {

            edtProductStock.setError(
                    "Vui lòng nhập tồn kho"
            );

            return;
        }

        if (saleDate.isEmpty()) {

            edtSaleDate.setError(
                    "Vui lòng chọn ngày bán"
            );

            return;
        }

        double price;

        int stock;

        try {

            price =
                    Double.parseDouble(
                            priceText
                    );

        } catch (NumberFormatException e) {

            edtProductPrice.setError(
                    "Giá không hợp lệ"
            );

            return;
        }

        try {

            stock =
                    Integer.parseInt(
                            stockText
                    );

        } catch (NumberFormatException e) {

            edtProductStock.setError(
                    "Tồn kho không hợp lệ"
            );

            return;
        }

        if (price < 0) {

            edtProductPrice.setError(
                    "Giá không được âm"
            );

            return;
        }

        if (stock < 0) {

            edtProductStock.setError(
                    "Tồn kho không được âm"
            );

            return;
        }

        // -----------------------------------------------------
        // CHECK PRODUCT CODE
        // -----------------------------------------------------

        if (databaseHelper.isProductCodeExists(
                productCode,
                productId
        )) {

            edtProductCode.setError(
                    "Mã sản phẩm đã tồn tại"
            );

            return;
        }

        // -----------------------------------------------------
        // CREATE PRODUCT
        // -----------------------------------------------------

        Product product =
                new Product(
                        productId,
                        productCode,
                        name,
                        price,
                        description,
                        stock,
                        selectedImagePath,
                        saleDate,
                        status
                );

        // -----------------------------------------------------
        // UPDATE DATABASE
        // -----------------------------------------------------

        int result =
                databaseHelper.updateProduct(
                        product
                );

        if (result > 0) {

            Toast.makeText(
                    this,
                    "✓ Cập nhật sản phẩm thành công",
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
    }

    // =========================================================
    // COPY IMAGE
    // =========================================================

    private String copyImageToInternalStorage(
            Uri uri
    ) {

        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {

            String extension = ".jpg";

            String mimeType =
                    getContentResolver()
                            .getType(uri);

            if (mimeType != null) {

                if (mimeType.contains("png")) {

                    extension = ".png";

                } else if (mimeType.contains("webp")) {

                    extension = ".webp";

                } else if (mimeType.contains("jpeg")) {

                    extension = ".jpg";
                }
            }

            String fileName =
                    "product_"
                            + System.currentTimeMillis()
                            + extension;

            File imageDirectory =
                    new File(
                            getFilesDir(),
                            "product_images"
                    );

            if (!imageDirectory.exists()) {
                imageDirectory.mkdirs();
            }

            File imageFile =
                    new File(
                            imageDirectory,
                            fileName
                    );

            inputStream =
                    getContentResolver()
                            .openInputStream(uri);

            if (inputStream == null) {
                return null;
            }

            outputStream =
                    new FileOutputStream(
                            imageFile
                    );

            byte[] buffer =
                    new byte[8192];

            int length;

            while (
                    (length =
                            inputStream.read(buffer))
                            > 0
            ) {

                outputStream.write(
                        buffer,
                        0,
                        length
                );
            }

            outputStream.flush();

            return imageFile.getAbsolutePath();

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        } finally {

            try {

                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (Exception ignored) {
            }

            try {

                if (outputStream != null) {
                    outputStream.close();
                }

            } catch (Exception ignored) {
            }
        }
    }
}