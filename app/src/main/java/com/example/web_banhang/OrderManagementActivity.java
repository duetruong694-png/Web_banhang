package com.example.web_banhang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private Button btnBack;

    private DatabaseHelper databaseHelper;
    private OrderAdapter adapter;

    private ArrayList<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_management);

        // =========================
        // ÁNH XẠ
        // =========================

        recyclerOrders = findViewById(R.id.recyclerOrders);
        btnBack = findViewById(R.id.btnBack);

        databaseHelper = new DatabaseHelper(this);

        // =========================
        // RECYCLER VIEW
        // =========================

        recyclerOrders.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // =========================
        // QUAY LẠI ADMIN
        // =========================

        btnBack.setOnClickListener(v -> finish());

        // =========================
        // LOAD ĐƠN HÀNG
        // =========================

        loadOrders();
    }

    private void loadOrders() {

        orderList =
                databaseHelper.getAllOrders();

        if (orderList == null || orderList.isEmpty()) {

            Toast.makeText(
                    this,
                    "Chưa có đơn hàng nào",
                    Toast.LENGTH_SHORT
            ).show();
        }

        adapter = new OrderAdapter(
                orderList,
                new OrderAdapter.OnOrderActionListener() {

                    @Override
                    public void onView(Order order) {

                        Intent intent =
                                new Intent(
                                        OrderManagementActivity.this,
                                        OrderDetailActivity.class
                                );

                        intent.putExtra(
                                "order_id",
                                order.getId()
                        );

                        startActivity(intent);
                    }
                }
        );

        recyclerOrders.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadOrders();
        }
    }
}