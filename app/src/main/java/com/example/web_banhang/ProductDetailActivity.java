package com.example.web_banhang;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProductDetail;
    private TextView tvProductNameDetail;
    private TextView tvProductPriceDetail;
    private TextView tvProductStockDetail;
    private TextView tvProductDescriptionDetail;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_product_detail
        );

        imgProductDetail =
                findViewById(R.id.imgProductDetail);

        tvProductNameDetail =
                findViewById(R.id.tvProductNameDetail);

        tvProductPriceDetail =
                findViewById(R.id.tvProductPriceDetail);

        tvProductStockDetail =
                findViewById(R.id.tvProductStockDetail);

        tvProductDescriptionDetail =
                findViewById(
                        R.id.tvProductDescriptionDetail
                );

        btnBack =
                findViewById(R.id.btnBack);

        String name =
                getIntent().getStringExtra("name");

        double price =
                getIntent().getDoubleExtra(
                        "price",
                        0
                );

        int stock =
                getIntent().getIntExtra(
                        "stock",
                        0
                );

        String description =
                getIntent().getStringExtra(
                        "description"
                );

        String imageUri =
                getIntent().getStringExtra(
                        "imageUri"
                );

        tvProductNameDetail.setText(name);

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        tvProductPriceDetail.setText(
                formatter.format(price)
        );

        tvProductStockDetail.setText(
                "Số lượng còn lại: " + stock
        );

        if (description == null
                || description.trim().isEmpty()) {

            tvProductDescriptionDetail.setText(
                    "Chưa có mô tả sản phẩm."
            );

        } else {

            tvProductDescriptionDetail.setText(
                    description
            );
        }

        try {

            if (imageUri != null
                    && !imageUri.isEmpty()) {

                imgProductDetail.setImageURI(
                        Uri.parse(imageUri)
                );

            }

        } catch (Exception e) {
            // giữ ảnh mặc định
        }

        btnBack.setOnClickListener(v -> finish());
    }
}