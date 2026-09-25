package com.example.web_banhang;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId;
    private TextView tvCustomerName;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvPaymentMethod;
    private TextView tvOrderDate;
    private TextView tvTotalMoney;

    private Spinner spinnerStatus;

    private Button btnSaveStatus;
    private Button btnBack;

    private RecyclerView recyclerOrderItems;

    private DatabaseHelper databaseHelper;

    private int orderId;

    private Order order;

    private ArrayList<OrderItem> orderItemList;

    private OrderItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_detail);

        // =========================
        // ÁNH XẠ
        // =========================

        tvOrderId = findViewById(R.id.tvOrderId);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvTotalMoney = findViewById(R.id.tvTotalMoney);

        spinnerStatus = findViewById(R.id.spinnerStatus);

        btnSaveStatus = findViewById(R.id.btnSaveStatus);
        btnBack = findViewById(R.id.btnBack);

        recyclerOrderItems = findViewById(
                R.id.recyclerOrderItems
        );

        databaseHelper = new DatabaseHelper(this);

        // =========================
        // NHẬN ORDER ID
        // =========================

        orderId = getIntent().getIntExtra(
                "order_id",
                -1
        );

        if (orderId == -1) {

            Toast.makeText(
                    this,
                    "Không tìm thấy đơn hàng",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // =========================
        // RECYCLER
        // =========================

        recyclerOrderItems.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // =========================
        // SPINNER STATUS
        // =========================

        setupStatusSpinner();

        // =========================
        // LOAD
        // =========================

        loadOrder();

        // =========================
        // QUAY LẠI
        // =========================

        btnBack.setOnClickListener(
                v -> finish()
        );

        // =========================
        // LƯU TRẠNG THÁI
        // =========================

        btnSaveStatus.setOnClickListener(
                v -> saveStatus()
        );
    }

    // =====================================================
    // SPINNER
    // =====================================================

    private void setupStatusSpinner() {

        String[] statuses = {
                "Chờ xác nhận",
                "Đang giao",
                "Đã giao",
                "Đã hủy"
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

        spinnerStatus.setAdapter(statusAdapter);
    }

    // =====================================================
    // LOAD ORDER
    // =====================================================

    private void loadOrder() {

        order =
                databaseHelper.getOrderById(
                        orderId
                );

        if (order == null) {

            Toast.makeText(
                    this,
                    "Không tìm thấy đơn hàng",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // =========================
        // THÔNG TIN ĐƠN
        // =========================

        tvOrderId.setText(
                "Đơn hàng #" + order.getId()
        );

        tvCustomerName.setText(
                "👤 " + order.getCustomerName()
        );

        tvPhone.setText(
                "📞 " + order.getPhone()
        );

        tvAddress.setText(
                "📍 " + order.getAddress()
        );

        tvPaymentMethod.setText(
                "💳 " + order.getPaymentMethod()
        );

        tvOrderDate.setText(
                "🕒 " + order.getOrderDate()
        );

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        tvTotalMoney.setText(
                formatter.format(
                        order.getTotalMoney()
                )
        );

        // =========================
        // CHỌN STATUS HIỆN TẠI
        // =========================

        String currentStatus =
                order.getStatus();

        if ("Đang giao".equals(currentStatus)) {

            spinnerStatus.setSelection(1);

        } else if ("Đã giao".equals(currentStatus)) {

            spinnerStatus.setSelection(2);

        } else if ("Đã hủy".equals(currentStatus)) {

            spinnerStatus.setSelection(3);

        } else {

            spinnerStatus.setSelection(0);
        }

        // =========================
        // LOAD SẢN PHẨM
        // =========================

        loadOrderItems();
    }

    // =====================================================
    // LOAD ORDER ITEMS
    // =====================================================

    private void loadOrderItems() {

        orderItemList =
                databaseHelper.getOrderItems(
                        orderId
                );

        adapter =
                new OrderItemAdapter(
                        orderItemList
                );

        recyclerOrderItems.setAdapter(
                adapter
        );
    }

    // =====================================================
    // SAVE STATUS
    // =====================================================

    private void saveStatus() {

        String status =
                spinnerStatus.getSelectedItem()
                        .toString();

        int result =
                databaseHelper.updateOrderStatus(
                        orderId,
                        status
                );

        if (result > 0) {

            Toast.makeText(
                    this,
                    "Đã cập nhật trạng thái đơn hàng",
                    Toast.LENGTH_SHORT
            ).show();

            loadOrder();

        } else {

            Toast.makeText(
                    this,
                    "Cập nhật trạng thái thất bại",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}