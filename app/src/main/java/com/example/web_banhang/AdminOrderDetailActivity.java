package com.example.web_banhang;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.web_banhang.api.ApiService;
import com.example.web_banhang.api.BasicResponse;
import com.example.web_banhang.api.OrderApi;
import com.example.web_banhang.api.OrderDetailResponse;
import com.example.web_banhang.api.OrderItemApi;
import com.example.web_banhang.api.RetrofitClient;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId;
    private TextView tvCustomer;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvPayment;
    private TextView tvDate;
    private TextView tvTotal;

    private TextView tvProducts;

    private Spinner spinnerStatus;

    private Button btnUpdateStatus;
    private Button btnBack;

    private ApiService apiService;

    private int adminId;
    private int orderId;

    private OrderApi currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_order_detail);

        // =========================
        // ÁNH XẠ VIEW
        // =========================

        tvOrderId = findViewById(R.id.tvOrderId);
        tvCustomer = findViewById(R.id.tvCustomer);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvPayment = findViewById(R.id.tvPayment);
        tvDate = findViewById(R.id.tvDate);
        tvTotal = findViewById(R.id.tvTotal);
        tvProducts = findViewById(R.id.tvProducts);

        spinnerStatus = findViewById(R.id.spinnerStatus);

        btnUpdateStatus = findViewById(R.id.btnUpdateStatus);
        btnBack = findViewById(R.id.btnBack);

        // =========================
        // LẤY ADMIN ID
        // =========================

        adminId = getSharedPreferences(
                "USER",
                MODE_PRIVATE
        ).getInt("user_id", 0);

        if (adminId == 0) {

            adminId = getSharedPreferences(
                    "LoginPrefs",
                    MODE_PRIVATE
            ).getInt("user_id", 0);
        }

        // =========================
        // LẤY ORDER ID
        // =========================

        orderId = getIntent().getIntExtra(
                "order_id",
                0
        );

        // =========================
        // RETROFIT
        // =========================

        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        // =========================
        // NÚT QUAY LẠI
        // =========================

        btnBack.setOnClickListener(v -> finish());

        // =========================
        // NÚT CẬP NHẬT
        // =========================

        btnUpdateStatus.setOnClickListener(
                v -> updateOrderStatus()
        );

        // =========================
        // KIỂM TRA
        // =========================

        if (adminId <= 0) {

            Toast.makeText(
                    this,
                    "Không tìm thấy tài khoản admin",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        if (orderId <= 0) {

            Toast.makeText(
                    this,
                    "Không tìm thấy đơn hàng",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // =========================
        // LOAD CHI TIẾT
        // =========================

        loadOrderDetail();
    }

    // =====================================================
    // LOAD CHI TIẾT ĐƠN HÀNG
    // =====================================================

    private void loadOrderDetail() {

        apiService.adminGetOrderDetail(
                adminId,
                orderId
        ).enqueue(new Callback<OrderDetailResponse>() {

            @Override
            public void onResponse(
                    Call<OrderDetailResponse> call,
                    Response<OrderDetailResponse> response
            ) {

                if (!response.isSuccessful()
                        || response.body() == null) {

                    Toast.makeText(
                            AdminOrderDetailActivity.this,
                            "Không lấy được chi tiết đơn hàng",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                OrderDetailResponse result =
                        response.body();

                if (!result.isSuccess()) {

                    Toast.makeText(
                            AdminOrderDetailActivity.this,
                            result.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                currentOrder = result.getOrder();

                if (currentOrder == null) {

                    Toast.makeText(
                            AdminOrderDetailActivity.this,
                            "Không có dữ liệu đơn hàng",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                showOrderDetail(currentOrder);
            }

            @Override
            public void onFailure(
                    Call<OrderDetailResponse> call,
                    Throwable t
            ) {

                Toast.makeText(
                        AdminOrderDetailActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    // =====================================================
    // HIỂN THỊ CHI TIẾT
    // =====================================================

    private void showOrderDetail(OrderApi order) {

        tvOrderId.setText(
                "ĐƠN HÀNG #" + order.getId()
        );

        tvCustomer.setText(
                "Khách hàng: " +
                        safe(order.getCustomerName())
        );

        tvPhone.setText(
                "Số điện thoại: " +
                        safe(order.getPhone())
        );

        tvAddress.setText(
                "Địa chỉ: " +
                        safe(order.getAddress())
        );

        tvPayment.setText(
                "Phương thức thanh toán: " +
                        safe(order.getPaymentMethod())
        );

        tvDate.setText(
                "Ngày đặt: " +
                        safe(order.getOrderDate())
        );

        tvTotal.setText(
                "Tổng tiền: " +
                        formatMoney(order.getTotalMoney())
        );

        // =========================
        // SẢN PHẨM
        // =========================

        StringBuilder products =
                new StringBuilder();

        List<OrderItemApi> items =
                order.getItems();

        if (items != null && !items.isEmpty()) {

            for (OrderItemApi item : items) {

                products.append("• ")
                        .append(
                                safe(
                                        item.getProductName()
                                )
                        )
                        .append(" x")
                        .append(item.getQuantity())
                        .append(" - ")
                        .append(
                                formatMoney(
                                        item.getPrice()
                                                * item.getQuantity()
                                )
                        )
                        .append("\n");
            }

        } else {

            products.append(
                    "Không có sản phẩm"
            );
        }

        tvProducts.setText(
                products.toString()
        );

        // =========================
        // SPINNER TRẠNG THÁI
        // =========================

        setupStatusSpinner(
                order.getStatus()
        );
    }

    // =====================================================
    // SPINNER STATUS
    // =====================================================

    private void setupStatusSpinner(
            String currentStatus
    ) {

        List<String> statusList =
                new ArrayList<>();

        statusList.add("pending");
        statusList.add("confirmed");
        statusList.add("shipping");
        statusList.add("completed");
        statusList.add("cancelled");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        statusList
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerStatus.setAdapter(adapter);

        int selectedPosition = 0;

        for (int i = 0;
             i < statusList.size();
             i++) {

            if (statusList.get(i)
                    .equals(currentStatus)) {

                selectedPosition = i;
                break;
            }
        }

        spinnerStatus.setSelection(
                selectedPosition
        );
    }

    // =====================================================
    // CẬP NHẬT TRẠNG THÁI
    // =====================================================

    private void updateOrderStatus() {

        if (currentOrder == null) {

            Toast.makeText(
                    this,
                    "Chưa có dữ liệu đơn hàng",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String newStatus =
                spinnerStatus
                        .getSelectedItem()
                        .toString();

        String oldStatus =
                currentOrder.getStatus();

        if (newStatus.equals(oldStatus)) {

            Toast.makeText(
                    this,
                    "Trạng thái chưa thay đổi",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        apiService.updateOrderStatus(
                adminId,
                orderId,
                newStatus
        ).enqueue(new Callback<BasicResponse>() {

            @Override
            public void onResponse(
                    Call<BasicResponse> call,
                    Response<BasicResponse> response
            ) {

                if (!response.isSuccessful()
                        || response.body() == null) {

                    Toast.makeText(
                            AdminOrderDetailActivity.this,
                            "Cập nhật thất bại",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                BasicResponse result =
                        response.body();

                if (result.isSuccess()) {

                    Toast.makeText(
                            AdminOrderDetailActivity.this,
                            "Cập nhật trạng thái thành công",
                            Toast.LENGTH_SHORT
                    ).show();

                    loadOrderDetail();

                } else {

                    Toast.makeText(
                            AdminOrderDetailActivity.this,
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
                        AdminOrderDetailActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    // =====================================================
    // FORMAT TIỀN
    // =====================================================

    private String formatMoney(double money) {

        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        new Locale("vi", "VN")
                );

        return formatter.format(money) + " đ";
    }

    // =====================================================
    // XỬ LÝ NULL
    // =====================================================

    private String safe(String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return "Không có";
        }

        return value;
    }
}