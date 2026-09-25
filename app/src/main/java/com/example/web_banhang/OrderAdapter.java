package com.example.web_banhang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderAdapter
        extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final ArrayList<Order> orderList;
    private final OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onView(Order order);
    }

    public OrderAdapter(
            ArrayList<Order> orderList,
            OnOrderActionListener listener
    ) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_order_admin,
                        parent,
                        false
                );

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull OrderViewHolder holder,
            int position
    ) {

        Order order = orderList.get(position);

        holder.tvOrderId.setText(
                "Đơn hàng #" + order.getId()
        );

        holder.tvCustomerName.setText(
                "👤 " + order.getCustomerName()
        );

        holder.tvPhone.setText(
                "📞 " + order.getPhone()
        );

        holder.tvOrderDate.setText(
                "🕒 " + order.getOrderDate()
        );

        holder.tvPaymentMethod.setText(
                "💳 " + order.getPaymentMethod()
        );

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        holder.tvTotalMoney.setText(
                formatter.format(order.getTotalMoney())
        );

        holder.tvStatus.setText(
                "Trạng thái: " + order.getStatus()
        );

        holder.btnView.setOnClickListener(
                v -> listener.onView(order)
        );
    }

    @Override
    public int getItemCount() {
        return orderList == null
                ? 0
                : orderList.size();
    }

    public static class OrderViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvOrderId;
        TextView tvCustomerName;
        TextView tvPhone;
        TextView tvOrderDate;
        TextView tvPaymentMethod;
        TextView tvTotalMoney;
        TextView tvStatus;

        Button btnView;

        public OrderViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            tvOrderId =
                    itemView.findViewById(
                            R.id.tvOrderId
                    );

            tvCustomerName =
                    itemView.findViewById(
                            R.id.tvCustomerName
                    );

            tvPhone =
                    itemView.findViewById(
                            R.id.tvPhone
                    );

            tvOrderDate =
                    itemView.findViewById(
                            R.id.tvOrderDate
                    );

            tvPaymentMethod =
                    itemView.findViewById(
                            R.id.tvPaymentMethod
                    );

            tvTotalMoney =
                    itemView.findViewById(
                            R.id.tvTotalMoney
                    );

            tvStatus =
                    itemView.findViewById(
                            R.id.tvStatus
                    );

            btnView =
                    itemView.findViewById(
                            R.id.btnView
                    );
        }
    }
}