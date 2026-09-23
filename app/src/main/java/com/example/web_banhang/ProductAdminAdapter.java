package com.example.web_banhang;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdminAdapter
        extends RecyclerView.Adapter<ProductAdminAdapter.ProductViewHolder> {

    private ArrayList<Product> productList;
    private OnProductActionListener listener;

    public interface OnProductActionListener {

        void onEdit(Product product);

        void onDelete(Product product);
    }

    public ProductAdminAdapter(
            ArrayList<Product> productList,
            OnProductActionListener listener
    ) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(
                R.layout.item_product_admin,
                parent,
                false
        );

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ProductViewHolder holder,
            int position
    ) {

        Product product = productList.get(position);

        // =========================
        // TÊN
        // =========================

        holder.tvProductName.setText(
                product.getName()
        );

        // =========================
        // GIÁ
        // =========================

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        holder.tvProductPrice.setText(
                formatter.format(product.getPrice())
        );

        // =========================
        // TỒN KHO
        // =========================

        holder.tvProductStock.setText(
                "Tồn kho: " + product.getStock()
        );

        // =========================
        // MÔ TẢ
        // =========================

        holder.tvProductDescription.setText(
                product.getDescription()
        );

        // =========================
        // HIỂN THỊ ẢNH
        // =========================

        String imageUri =
                product.getImageUri();

        if (imageUri != null
                && !imageUri.isEmpty()) {

            try {

                holder.imgProduct.setImageURI(
                        Uri.parse(imageUri)
                );

            } catch (Exception e) {

                holder.imgProduct.setImageResource(
                        R.drawable.ic_launcher_foreground
                );
            }

        } else {

            holder.imgProduct.setImageResource(
                    R.drawable.ic_launcher_foreground
            );
        }

        // =========================
        // NÚT SỬA
        // =========================

        holder.btnEdit.setOnClickListener(v ->
                listener.onEdit(product)
        );

        // =========================
        // NÚT XÓA
        // =========================

        holder.btnDelete.setOnClickListener(v ->
                listener.onDelete(product)
        );
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgProduct;

        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductStock;
        TextView tvProductDescription;

        Button btnEdit;
        Button btnDelete;

        public ProductViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgProduct =
                    itemView.findViewById(
                            R.id.imgProduct
                    );

            tvProductName =
                    itemView.findViewById(
                            R.id.tvProductName
                    );

            tvProductPrice =
                    itemView.findViewById(
                            R.id.tvProductPrice
                    );

            tvProductStock =
                    itemView.findViewById(
                            R.id.tvProductStock
                    );

            tvProductDescription =
                    itemView.findViewById(
                            R.id.tvProductDescription
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
}