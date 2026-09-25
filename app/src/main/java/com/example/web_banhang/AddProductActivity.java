package com.example.web_banhang;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddProductActivity extends AppCompatActivity {

    private EditText edtProductCode;
    private EditText edtProductName;
    private EditText edtProductPrice;
    private EditText edtProductStock;
    private EditText edtSaleDate;
    private EditText edtProductDescription;

    private Spinner spinnerStatus;

    private ImageView imgProductPreview;

    private Button btnChooseImage;
    private Button btnCancel;
    private Button btnSaveProduct;

    private DatabaseHelper databaseHelper;

    private String selectedImagePath = "";

    // =========================================================
    // CHỌN ẢNH
    // =========================================================

    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri != null) {

                            saveImageToInternalStorage(uri);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);

        // =====================================================
        // ÁNH XẠ VIEW
        // =====================================================

        edtProductCode =
                findViewById(R.id.edtProductCode);

        edtProductName =
                findViewById(R.id.edtProductName);

        edtProductPrice =
                findViewById(R.id.edtProductPrice);

        edtProductStock =
                findViewById(R.id.edtProductStock);

        edtSaleDate =
                findViewById(R.id.edtSaleDate);

        edtProductDescription =
                findViewById(R.id.edtProductDescription);

        spinnerStatus =
                findViewById(R.id.spinnerStatus);

        imgProductPreview =
                findViewById(R.id.imgProductPreview);

        btnChooseImage =
                findViewById(R.id.btnChooseImage);

        btnCancel =
                findViewById(R.id.btnCancel);

        btnSaveProduct =
                findViewById(R.id.btnSaveProduct);

        databaseHelper =
                new DatabaseHelper(this);

        // =====================================================
        // TRẠNG THÁI
        // =====================================================

        String[] statusList = {
                "Đang bán",
                "Ngừng bán"
        };

        ArrayAdapter<String> statusAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        statusList
                );

        statusAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerStatus.setAdapter(statusAdapter);

        // =====================================================
        // TỰ ĐỘNG TẠO MÃ SẢN PHẨM
        // =====================================================

        generateNextProductCode();

        // =====================================================
        // TỰ ĐỘNG CHỌN NGÀY HIỆN TẠI
        // =====================================================

        setTodaySaleDate();

        // =====================================================
        // CHỌN NGÀY
        // =====================================================

        edtSaleDate.setOnClickListener(v -> {

            showDatePicker();
        });

        // =====================================================
        // CHỌN ẢNH
        // =====================================================

        btnChooseImage.setOnClickListener(v -> {

            imagePicker.launch("image/*");
        });

        // =====================================================
        // LƯU SẢN PHẨM
        // =====================================================

        btnSaveProduct.setOnClickListener(v -> {

            saveProduct();
        });

        // =====================================================
        // HỦY
        // =====================================================

        btnCancel.setOnClickListener(v -> {

            finish();
        });
    }

    // =============================================================
    // TỰ ĐỘNG TẠO MÃ SP THEO THỨ TỰ
    // =============================================================

    private void generateNextProductCode() {

        ArrayList<Product> products =
                databaseHelper.getAllProducts();

        int maxNumber = 0;

        if (products != null) {

            for (Product product : products) {

                String code =
                        product.getProductCode();

                if (code == null) {
                    continue;
                }

                code = code.trim()
                        .toUpperCase(Locale.ROOT);

                // =============================================
                // Lấy phần số của SP001, SP002...
                // =============================================

                if (code.startsWith("SP")) {

                    String numberPart =
                            code.substring(2);

                    try {

                        int number =
                                Integer.parseInt(numberPart);

                        if (number > maxNumber) {
                            maxNumber = number;
                        }

                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        int nextNumber = maxNumber + 1;

        String nextCode =
                String.format(
                        Locale.ROOT,
                        "SP%03d",
                        nextNumber
                );

        edtProductCode.setText(nextCode);

        // Đưa con trỏ về cuối
        edtProductCode.setSelection(
                edtProductCode.length()
        );
    }

    // =============================================================
    // TỰ ĐỘNG CHỌN NGÀY HIỆN TẠI
    // =============================================================

    private void setTodaySaleDate() {

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                );

        String today =
                dateFormat.format(new Date());

        edtSaleDate.setText(today);
    }

    // =============================================================
    // HIỂN THỊ DATE PICKER
    // =============================================================

    private void showDatePicker() {

        Calendar calendar =
                Calendar.getInstance();

        // Nếu ô ngày đã có dữ liệu thì lấy ngày đó
        try {

            String currentDate =
                    edtSaleDate.getText()
                            .toString()
                            .trim();

            if (!currentDate.isEmpty()) {

                Date date =
                        new SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                        ).parse(currentDate);

                if (date != null) {

                    calendar.setTime(date);
                }
            }

        } catch (Exception ignored) {
        }

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, selectedYear,
                         selectedMonth,
                         selectedDay) -> {

                            String selectedDate =
                                    String.format(
                                            Locale.getDefault(),
                                            "%02d/%02d/%04d",
                                            selectedDay,
                                            selectedMonth + 1,
                                            selectedYear
                                    );

                            edtSaleDate.setText(
                                    selectedDate
                            );
                        },
                        year,
                        month,
                        day
                );

        datePickerDialog.show();
    }

    // =============================================================
    // CHỌN VÀ LƯU ẢNH VÀO BỘ NHỚ APP
    // =============================================================

    private void saveImageToInternalStorage(Uri uri) {

        try {

            File imageDirectory =
                    new File(
                            getFilesDir(),
                            "product_images"
                    );

            if (!imageDirectory.exists()) {

                imageDirectory.mkdirs();
            }

            String fileName =
                    "product_"
                            + System.currentTimeMillis()
                            + ".jpg";

            File imageFile =
                    new File(
                            imageDirectory,
                            fileName
                    );

            InputStream inputStream =
                    getContentResolver()
                            .openInputStream(uri);

            FileOutputStream outputStream =
                    new FileOutputStream(
                            imageFile
                    );

            byte[] buffer =
                    new byte[4096];

            int length;

            while ((length =
                    inputStream.read(buffer)) > 0) {

                outputStream.write(
                        buffer,
                        0,
                        length
                );
            }

            outputStream.close();
            inputStream.close();

            selectedImagePath =
                    imageFile.getAbsolutePath();

            imgProductPreview.setImageURI(
                    Uri.fromFile(imageFile)
            );

            Toast.makeText(
                    this,
                    "Đã chọn ảnh sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Không thể lưu ảnh: "
                            + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // =============================================================
    // LƯU SẢN PHẨM
    // =============================================================

    private void saveProduct() {

        String productCode =
                edtProductCode.getText()
                        .toString()
                        .trim();

        String productName =
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
                spinnerStatus.getSelectedItem()
                        .toString();

        // =====================================================
        // KIỂM TRA DỮ LIỆU
        // =====================================================

        if (TextUtils.isEmpty(productCode)) {

            edtProductCode.setError(
                    "Vui lòng nhập mã sản phẩm"
            );

            edtProductCode.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(productName)) {

            edtProductName.setError(
                    "Vui lòng nhập tên sản phẩm"
            );

            edtProductName.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(priceText)) {

            edtProductPrice.setError(
                    "Vui lòng nhập giá sản phẩm"
            );

            edtProductPrice.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(stockText)) {

            edtProductStock.setError(
                    "Vui lòng nhập số lượng"
            );

            edtProductStock.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(saleDate)) {

            edtSaleDate.setError(
                    "Vui lòng chọn ngày bán"
            );

            edtSaleDate.requestFocus();

            return;
        }

        // =====================================================
        // KIỂM TRA MÃ SP TRÙNG
        // =====================================================

        if (databaseHelper.isProductCodeExists(
                productCode
        )) {

            edtProductCode.setError(
                    "Mã sản phẩm đã tồn tại"
            );

            edtProductCode.requestFocus();

            Toast.makeText(
                    this,
                    "Mã sản phẩm đã tồn tại",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =====================================================
        // CHUYỂN GIÁ
        // =====================================================

        double price;

        try {

            price =
                    Double.parseDouble(
                            priceText
                                    .replace(",", "")
                                    .replace(".", "")
                    );

        } catch (Exception e) {

            edtProductPrice.setError(
                    "Giá không hợp lệ"
            );

            edtProductPrice.requestFocus();

            return;
        }

        if (price < 0) {

            edtProductPrice.setError(
                    "Giá không được âm"
            );

            edtProductPrice.requestFocus();

            return;
        }

        // =====================================================
        // CHUYỂN TỒN KHO
        // =====================================================

        int stock;

        try {

            stock =
                    Integer.parseInt(stockText);

        } catch (Exception e) {

            edtProductStock.setError(
                    "Số lượng không hợp lệ"
            );

            edtProductStock.requestFocus();

            return;
        }

        if (stock < 0) {

            edtProductStock.setError(
                    "Số lượng không được âm"
            );

            edtProductStock.requestFocus();

            return;
        }

        // =====================================================
        // TẠO PRODUCT
        // =====================================================

        Product product =
                new Product(
                        0,
                        productCode,
                        productName,
                        price,
                        description,
                        stock,
                        selectedImagePath,
                        saleDate,
                        status
                );

        // =====================================================
        // THÊM VÀO DATABASE
        // =====================================================

        long result =
                databaseHelper.addProduct(
                        product
                );

        if (result > 0) {

            Toast.makeText(
                    this,
                    "Thêm sản phẩm thành công",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Không thể thêm sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}