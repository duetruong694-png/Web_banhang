package com.example.web_banhang;

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

public class OrderHistoryActivity extends AppCompatActivity {

    private LinearLayout layoutOrders;
    private Button btnBack;

    private ApiService apiService;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_history);

        layoutOrders = findViewById(R.id.layoutOrders);
        btnBack = findViewById(R.id.btnBack);

        userId = getSharedPreferences(
                "USER",
                MODE_PRIVATE
        ).getInt("user_id", 0);

        if (userId == 0) {
            userId = getSharedPreferences(
                    "LoginPrefs",
                    MODE_PRIVATE
            ).getInt("user_id", 0);
        }

        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        btnBack.setOnClickListener(v -> finish());

        loadOrders();
    }

    private void loadOrders() {

        if (userId <= 0) {

            Toast.makeText(
                    this,
                    "Bạn chưa đăng nhập",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        apiService.getOrders(userId)
                .enqueue(new Callback<OrderResponse>() {

                    @Override
                    public void onResponse(
                            Call<OrderResponse> call,
                            Response<OrderResponse> response) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    OrderHistoryActivity.this,
                                    "Không lấy được lịch sử mua hàng",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        OrderResponse result =
                                response.body();

                        if (!result.isSuccess()) {

                            Toast.makeText(
                                    OrderHistoryActivity.this,
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
                            Throwable t) {

                        Toast.makeText(
                                OrderHistoryActivity.this,
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

            empty.setText("Bạn chưa có đơn hàng nào.");
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

        TextView tvOrderId = new TextView(this);

        tvOrderId.setText(
                "Đơn hàng #" + order.getId()
        );

        tvOrderId.setTextSize(20);
        tvOrderId.setTextColor(
                getResources().getColor(
                        android.R.color.black
                )
        );

        card.addView(tvOrderId);

        TextView tvDate = new TextView(this);

        tvDate.setText(
                "Ngày đặt: " +
                        safe(order.getOrderDate())
        );

        tvDate.setTextSize(16);

        card.addView(tvDate);

        TextView tvStatus = new TextView(this);

        tvStatus.setText(
                "Trạng thái: " +
                        getStatusText(order.getStatus())
        );

        tvStatus.setTextSize(16);

        card.addView(tvStatus);

        TextView tvTotal = new TextView(this);

        tvTotal.setText(
                "Tổng tiền: " +
                        formatMoney(order.getTotalMoney())
        );

        tvTotal.setTextSize(18);
        tvTotal.setPadding(0, 10, 0, 10);

        card.addView(tvTotal);

        // =========================
        // DANH SÁCH SẢN PHẨM
        // =========================

        List<OrderItemApi> items =
                order.getItems();

        if (items != null) {

            for (OrderItemApi item : items) {

                TextView tvItem =
                        new TextView(this);

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
        // NÚT XEM CHI TIẾT
        // =========================

        Button btnDetail =
                new Button(this);

        btnDetail.setText(
                "XEM CHI TIẾT"
        );

        btnDetail.setOnClickListener(v -> {

            android.content.Intent intent =
                    new android.content.Intent(
                            OrderHistoryActivity.this,
                            OrderDetailActivity.class
                    );

            intent.putExtra(
                    "order_id",
                    order.getId()
            );

            startActivity(intent);
        });

        card.addView(btnDetail);

        layoutOrders.addView(card);
    }

    private String formatMoney(double money) {

        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        new Locale("vi", "VN")
                );

        return formatter.format(money) + " đ";
    }

    private String safe(String value) {

        if (value == null || value.trim().isEmpty()) {
            return "Không có";
        }

        return value;
    }

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
}
