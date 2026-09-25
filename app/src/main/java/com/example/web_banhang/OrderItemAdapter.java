package com.example.web_banhang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderItemAdapter
        extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private final ArrayList<OrderItem> itemList;

    public OrderItemAdapter(
            ArrayList<OrderItem> itemList
    ) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_order_detail,
                        parent,
                        false
                );

        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull OrderItemViewHolder holder,
            int position
    ) {

        OrderItem item =
                itemList.get(position);

        holder.tvProductName.setText(
                item.getProductName()
        );

        holder.tvQuantity.setText(
                "Số lượng: " + item.getQuantity()
        );

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        holder.tvPrice.setText(
                "Đơn giá: "
                        + formatter.format(
                        item.getPrice()
                )
        );

        holder.tvTotal.setText(
                "Thành tiền: "
                        + formatter.format(
                        item.getTotalPrice()
                )
        );
    }

    @Override
    public int getItemCount() {

        return itemList == null
                ? 0
                : itemList.size();
    }

    public static class OrderItemViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvProductName;
        TextView tvQuantity;
        TextView tvPrice;
        TextView tvTotal;

        public OrderItemViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            tvProductName =
                    itemView.findViewById(
                            R.id.tvProductName
                    );

            tvQuantity =
                    itemView.findViewById(
                            R.id.tvQuantity
                    );

            tvPrice =
                    itemView.findViewById(
                            R.id.tvPrice
                    );

            tvTotal =
                    itemView.findViewById(
                            R.id.tvTotal
                    );
        }
    }
}