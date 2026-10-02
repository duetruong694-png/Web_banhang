package com.example.web_banhang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.web_banhang.api.ProductApi;

import java.util.ArrayList;

public class ProductApiAdapter
        extends RecyclerView.Adapter<ProductApiAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<ProductApi> products;

    private final boolean isLoggedIn;
    private final int userId;
    private final String role;

    public ProductApiAdapter(
            Context context,
            ArrayList<ProductApi> products,
            boolean isLoggedIn,
            int userId,
            String role
    ) {

        this.context = context;
        this.products = products;
        this.isLoggedIn = isLoggedIn;
        this.userId = userId;
        this.role = role;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_product,
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
                products.get(position);

        holder.tvName.setText(
                product.getName()
        );

        holder.tvPrice.setText(
                formatPrice(product.getPrice())
        );

        holder.tvStock.setText(
                "Kho: " + product.getStock()
        );

        holder.tvDescription.setText(
                product.getDescription() == null
                        ? ""
                        : product.getDescription()
        );

        // =========================
        // ADMIN KHÔNG MUA HÀNG
        // =========================

        if ("admin".equalsIgnoreCase(role)) {

            holder.btnBuy.setVisibility(
                    View.GONE
            );

            holder.btnCart.setVisibility(
                    View.GONE
            );

        } else {

            holder.btnBuy.setVisibility(
                    View.VISIBLE
            );

            holder.btnCart.setVisibility(
                    View.VISIBLE
            );

            if (product.getStock() <= 0) {

                holder.btnBuy.setEnabled(
                        false
                );

                holder.btnCart.setEnabled(
                        false
                );

                holder.btnBuy.setText(
                        "HẾT HÀNG"
                );

                holder.btnCart.setText(
                        "HẾT"
                );

            } else {

                holder.btnBuy.setEnabled(
                        true
                );

                holder.btnCart.setEnabled(
                        true
                );

                holder.btnBuy.setText(
                        "MUA NGAY"
                );

                holder.btnCart.setText(
                        "🛒"
                );
            }
        }

        // =========================
        // HÌNH ẢNH
        // =========================

        holder.imgProduct.setImageResource(
                android.R.drawable.ic_menu_gallery
        );

        // Click sản phẩm
        holder.itemView.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    context,
                                    ProductDetailActivity.class
                            );

                    intent.putExtra(
                            "product_id",
                            product.getId()
                    );

                    context.startActivity(
                            intent
                    );
                }
        );

        // Mua ngay
        holder.btnBuy.setOnClickListener(
                v -> {

                    if (!isLoggedIn
                            || userId == -1) {

                        Intent intent =
                                new Intent(
                                        context,
                                        LoginActivity.class
                                );

                        context.startActivity(
                                intent
                        );

                        return;
                    }

                    Intent intent =
                            new Intent(
                                    context,
                                    ProductDetailActivity.class
                            );

                    intent.putExtra(
                            "product_id",
                            product.getId()
                    );

                    intent.putExtra(
                            "buy_now",
                            true
                    );

                    context.startActivity(
                            intent
                    );
                }
        );

        // Thêm giỏ
        holder.btnCart.setOnClickListener(
                v -> {

                    if (!isLoggedIn
                            || userId == -1) {

                        Intent intent =
                                new Intent(
                                        context,
                                        LoginActivity.class
                                );

                        context.startActivity(
                                intent
                        );

                        return;
                    }

                    // Tạm thời mở chi tiết.
                    // Phần add_cart.php sẽ được nối
                    // trực tiếp ở bước CartActivity.
                    Intent intent =
                            new Intent(
                                    context,
                                    ProductDetailActivity.class
                            );

                    intent.putExtra(
                            "product_id",
                            product.getId()
                    );

                    intent.putExtra(
                            "add_to_cart",
                            true
                    );

                    context.startActivity(
                            intent
                    );
                }
        );
    }

    @Override
    public int getItemCount() {

        return products.size();
    }

    private String formatPrice(
            double price
    ) {

        return String.format(
                "%,.0f đ",
                price
        );
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgProduct;

        TextView tvName;
        TextView tvPrice;
        TextView tvStock;
        TextView tvDescription;

        Button btnBuy;
        Button btnCart;

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

            tvDescription =
                    itemView.findViewById(
                            R.id.tvProductDescription
                    );

            btnBuy =
                    itemView.findViewById(
                            R.id.btnBuy
                    );

            btnCart =
                    itemView.findViewById(
                            R.id.btnAddCart
                    );
        }
    }
}