package com.example.web_banhang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.web_banhang.api.ProductApi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdminApiAdapter
        extends RecyclerView.Adapter<ProductAdminApiAdapter.ViewHolder> {

    public interface OnProductActionListener {

        void onEdit(ProductApi product);

        void onDelete(ProductApi product);
    }

    private final ArrayList<ProductApi> productList;
    private final OnProductActionListener listener;

    public ProductAdminApiAdapter(
            ArrayList<ProductApi> productList,
            OnProductActionListener listener
    ) {

        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(
                R.layout.item_admin_product,
                parent,
                false
        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        ProductApi product =
                productList.get(position);

        holder.tvName.setText(
                safe(product.getName())
        );

        holder.tvPrice.setText(
                formatMoney(product.getPrice())
        );

        holder.tvStock.setText(
                "Tồn kho: " +
                        product.getStock()
        );

        holder.tvCode.setText(
                "Mã SP: " +
                        safe(product.getProductCode())
        );

        holder.tvStatus.setText(
                "Trạng thái: " +
                        getStatusText(
                                product.getStatus()
                        )
        );

        // Chưa dùng thư viện tải ảnh,
        // nên giữ ảnh mặc định.
        holder.imgProduct.setImageResource(
                android.R.drawable.ic_menu_gallery
        );

        holder.btnEdit.setOnClickListener(v -> {

            if (listener != null) {
                listener.onEdit(product);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {

            if (listener != null) {
                listener.onDelete(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgProduct;

        TextView tvName;
        TextView tvPrice;
        TextView tvStock;
        TextView tvCode;
        TextView tvStatus;

        Button btnEdit;
        Button btnDelete;

        public ViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgProduct =
                    itemView.findViewById(
                            R.id.imgProduct
                    );

            tvName =
                    itemView.findViewById(
                            R.id.tvProductName
                    );

            tvPrice =
                    itemView.findViewById(
                            R.id.tvProductPrice
                    );

            tvStock =
                    itemView.findViewById(
                            R.id.tvProductStock
                    );

            tvCode =
                    itemView.findViewById(
                            R.id.tvProductCode
                    );

            tvStatus =
                    itemView.findViewById(
                            R.id.tvProductStatus
                    );

            btnEdit =
                    itemView.findViewById(
                            R.id.btnEdit
                    );

            btnDelete =
                    itemView.findViewById(
                            R.id.btnDelete
                    );
        }
    }

    private String formatMoney(
            double money
    ) {

        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        new Locale("vi", "VN")
                );

        return formatter.format(money)
                + " đ";
    }

    private String safe(String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return "Không có";
        }

        return value;
    }

    private String getStatusText(
            String status
    ) {

        if (status == null) {
            return "Không xác định";
        }

        switch (status) {

            case "active":
                return "Đang bán";

            case "inactive":
                return "Ngừng bán";

            case "out_of_stock":
                return "Hết hàng";

            default:
                return status;
        }
    }
}