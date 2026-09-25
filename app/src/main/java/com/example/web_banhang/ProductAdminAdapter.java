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

import java.io.File;
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

        // =====================================================
        // MÃ SẢN PHẨM
        // =====================================================

        holder.tvProductCode.setText(
                "Mã SP: " + product.getProductCode()
        );

        // =====================================================
        // TÊN SẢN PHẨM
        // =====================================================

        holder.tvProductName.setText(
                product.getName()
        );

        // =====================================================
        // GIÁ
        // =====================================================

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        holder.tvProductPrice.setText(
                formatter.format(product.getPrice())
        );

        // =====================================================
        // TỒN KHO
        // =====================================================

        holder.tvProductStock.setText(
                "Tồn kho: " + product.getStock()
        );

        // =====================================================
        // NGÀY BÁN
        // =====================================================

        String saleDate = product.getSaleDate();

        if (saleDate == null || saleDate.isEmpty()) {

            holder.tvSaleDate.setText(
                    "Ngày bán: Chưa chọn"
            );

        } else {

            holder.tvSaleDate.setText(
                    "Ngày bán: " + saleDate
            );
        }

        // =====================================================
        // TRẠNG THÁI
        // =====================================================

        String status = product.getStatus();

        if (status == null || status.isEmpty()) {
            status = "Đang bán";
        }

        holder.tvProductStatus.setText(
                "Trạng thái: " + status
        );

        // =====================================================
        // MÔ TẢ
        // =====================================================

        String description = product.getDescription();

        if (description == null || description.isEmpty()) {

            holder.tvProductDescription.setText(
                    "Chưa có mô tả"
            );

        } else {

            holder.tvProductDescription.setText(
                    description
            );
        }

        // =====================================================
        // HIỂN THỊ ẢNH
        // =====================================================

        holder.imgProduct.setImageResource(
                R.drawable.ic_launcher_foreground
        );

        String imageUri = product.getImageUri();

        if (imageUri != null && !imageUri.isEmpty()) {

            try {

                File imageFile = new File(imageUri);

                if (imageFile.exists()) {

                    holder.imgProduct.setImageURI(
                            Uri.fromFile(imageFile)
                    );

                } else if (imageUri.startsWith("content://")) {

                    holder.imgProduct.setImageURI(
                            Uri.parse(imageUri)
                    );

                } else {

                    // =================================================
                    // ẢNH DRAWABLE THEO TÊN SẢN PHẨM
                    // =================================================

                    String productName =
                            product.getName()
                                    .toLowerCase(Locale.ROOT);

                    if (productName.contains("bút chì")
                            || productName.contains("but chi")) {

                        holder.imgProduct.setImageResource(
                                R.drawable.butchi
                        );

                    } else if (productName.contains("tẩy")
                            || productName.contains("tay")) {

                        holder.imgProduct.setImageResource(
                                R.drawable.tay
                        );

                    } else if (productName.contains("vở")
                            || productName.contains("vo")) {

                        holder.imgProduct.setImageResource(
                                R.drawable.vo
                        );
                    }
                }

            } catch (Exception e) {

                holder.imgProduct.setImageResource(
                        R.drawable.ic_launcher_foreground
                );
            }
        }

        // =====================================================
        // NÚT SỬA
        // =====================================================

        holder.btnEdit.setOnClickListener(v ->
                listener.onEdit(product)
        );

        // =====================================================
        // NÚT XÓA
        // =====================================================

        holder.btnDelete.setOnClickListener(v ->
                listener.onDelete(product)
        );
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // =========================================================
    // VIEW HOLDER
    // =========================================================

    public static class ProductViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgProduct;

        TextView tvProductCode;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductStock;
        TextView tvSaleDate;
        TextView tvProductStatus;
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

            tvProductCode =
                    itemView.findViewById(
                            R.id.tvProductCode
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

            tvSaleDate =
                    itemView.findViewById(
                            R.id.tvSaleDate
                    );

            tvProductStatus =
                    itemView.findViewById(
                            R.id.tvProductStatus
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