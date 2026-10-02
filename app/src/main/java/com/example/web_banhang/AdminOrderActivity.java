package com.example.web_banhang;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.web_banhang.api.ApiService;
import com.example.web_banhang.api.OrderApi;
import com.example.web_banhang.api.OrderItemApi;
import com.example.web_banhang.api.OrderResponse;
import com.example.web_banhang.api.RetrofitClient;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderActivity extends AppCompatActivity {

    private LinearLayout layoutOrders;
    private Button btnBack;

    private ApiService apiService;
    private int adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_order);

        layoutOrders = findViewById(R.id.layoutOrders);
        btnBack = findViewById(R.id.btnBack);

        // Lấy ID admin
        adminId = getSharedPreferences("USER", MODE_PRIVATE)
                .getInt("user_id", 0);

        if (adminId == 0) {
            adminId = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                    .getInt("user_id", 0);
        }

        // Retrofit
        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Tải danh sách đơn hàng
        loadOrders();
    }

    private void loadOrders() {

        if (adminId <= 0) {

            Toast.makeText(
                    this,
                    "Không tìm thấy tài khoản admin",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        apiService.adminGetOrders(adminId)
                .enqueue(new Callback<OrderResponse>() {

                    @Override
                    public void onResponse(
                            Call<OrderResponse> call,
                            Response<OrderResponse> response
                    ) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    AdminOrderActivity.this,
                                    "Không lấy được danh sách đơn hàng",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        OrderResponse result = response.body();

                        if (!result.isSuccess()) {

                            Toast.makeText(
                                    AdminOrderActivity.this,
                                    result.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        showOrders(result.getOrders());
                    }

                    @Override
                    public void onFailure(
                            Call<OrderResponse> call,
                            Throwable t
                    ) {

                        Toast.makeText(
                                AdminOrderActivity.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void showOrders(List<OrderApi> orders) {

        layoutOrders.removeAllViews();

        if (orders == null || orders.isEmpty()) {

            TextView empty = new TextView(this);

            empty.setText("Chưa có đơn hàng nào.");
            empty.setTextSize(18);
            empty.setPadding(20, 40, 20, 40);

            layoutOrders.addView(empty);

            return;
        }

        for (OrderApi order : orders) {
            addOrderView(order);
        }
    }

    private void addOrderView(OrderApi order) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(25, 20, 25, 20);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, 20);

        card.setLayoutParams(cardParams);

        // =========================
        // MÃ ĐƠN HÀNG
        // =========================

        TextView tvOrderId = new TextView(this);

        tvOrderId.setText(
                "ĐƠN HÀNG #" + order.getId()
        );

        tvOrderId.setTextSize(20);

        tvOrderId.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(tvOrderId);

        // =========================
        // KHÁCH HÀNG
        // =========================

        TextView tvCustomer = new TextView(this);

        tvCustomer.setText(
                "Khách hàng: " +
                        safe(order.getCustomerName())
        );

        tvCustomer.setTextSize(16);

        card.addView(tvCustomer);

        // =========================
        // SỐ ĐIỆN THOẠI
        // =========================

        TextView tvPhone = new TextView(this);

        tvPhone.setText(
                "SĐT: " +
                        safe(order.getPhone())
        );

        tvPhone.setTextSize(16);

        card.addView(tvPhone);

        // =========================
        // ĐỊA CHỈ
        // =========================

        TextView tvAddress = new TextView(this);

        tvAddress.setText(
                "Địa chỉ: " +
                        safe(order.getAddress())
        );

        tvAddress.setTextSize(16);

        card.addView(tvAddress);

        // =========================
        // NGÀY ĐẶT
        // =========================

        TextView tvDate = new TextView(this);

        tvDate.setText(
                "Ngày đặt: " +
                        safe(order.getOrderDate())
        );

        tvDate.setTextSize(15);

        card.addView(tvDate);

        // =========================
        // TRẠNG THÁI
        // =========================

        TextView tvStatus = new TextView(this);

        tvStatus.setText(
                "Trạng thái: " +
                        getStatusText(order.getStatus())
        );

        tvStatus.setTextSize(16);

        card.addView(tvStatus);

        // =========================
        // PHƯƠNG THỨC THANH TOÁN
        // =========================

        TextView tvPayment = new TextView(this);

        tvPayment.setText(
                "Thanh toán: " +
                        safe(order.getPaymentMethod())
        );

        tvPayment.setTextSize(16);

        card.addView(tvPayment);

        // =========================
        // TỔNG TIỀN
        // =========================

        TextView tvTotal = new TextView(this);

        tvTotal.setText(
                "Tổng tiền: " +
                        formatMoney(order.getTotalMoney())
        );

        tvTotal.setTextSize(18);

        tvTotal.setPadding(
                0,
                10,
                0,
                10
        );

        card.addView(tvTotal);

        // =========================
        // DANH SÁCH SẢN PHẨM
        // =========================

        List<OrderItemApi> items = order.getItems();

        if (items != null && !items.isEmpty()) {

            TextView tvProducts = new TextView(this);

            tvProducts.setText("Sản phẩm:");

            tvProducts.setTextSize(17);

            tvProducts.setTypeface(
                    null,
                    Typeface.BOLD
            );

            card.addView(tvProducts);

            for (OrderItemApi item : items) {

                TextView tvItem = new TextView(this);

                tvItem.setText(
                        "• " +
                                safe(item.getProductName()) +
                                " x" +
                                item.getQuantity() +
                                " - " +
                                formatMoney(
                                        item.getPrice()
                                                * item.getQuantity()
                                )
                );

                tvItem.setTextSize(15);

                tvItem.setPadding(
                        10,
                        5,
                        10,
                        5
                );

                card.addView(tvItem);
            }
        }

        // =========================
        // NÚT XEM / XỬ LÝ
        // =========================

        Button btnDetail = new Button(this);

        btnDetail.setText(
                "XEM / XỬ LÝ ĐƠN HÀNG"
        );

        btnDetail.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminOrderActivity.this,
                    AdminOrderDetailActivity.class
            );

            intent.putExtra(
                    "order_id",
                    order.getId()
            );

            startActivity(intent);
        });

        card.addView(btnDetail);

        // Thêm card vào danh sách
        layoutOrders.addView(card);
    }

    // =========================
    // ĐỊNH DẠNG TIỀN
    // =========================

    private String formatMoney(double money) {

        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        new Locale("vi", "VN")
                );

        return formatter.format(money) + " đ";
    }

    // =========================
    // XỬ LÝ TEXT NULL
    // =========================

    private String safe(String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Không có";
        }

        return value;
    }

    // =========================
    // ĐỔI TRẠNG THÁI ĐƠN HÀNG
    // =========================

    private String getStatusText(String status) {

        if (status == null) {
            return "Không xác định";
        }

        switch (status) {

            case "pending":
                return "Chờ xác nhận";

            case "confirmed":
                return "Đã xác nhận";

            case "shipping":
                return "Đang giao hàng";

            case "completed":
                return "Đã hoàn thành";

            case "cancelled":
                return "Đã hủy";

            default:
                return status;
        }
    }

    // =========================
    // KHI QUAY LẠI MÀN HÌNH
    // =========================

    @Override
    protected void onResume() {

        super.onResume();

        if (apiService != null) {
            loadOrders();
        }
    }
}