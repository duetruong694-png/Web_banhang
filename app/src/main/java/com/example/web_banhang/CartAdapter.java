package com.example.web_banhang;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter
        extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final ArrayList<CartItem> cartList;
    private final DatabaseHelper databaseHelper;

    public CartAdapter(
            ArrayList<CartItem> cartList,
            DatabaseHelper databaseHelper
    ) {
        this.cartList = cartList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_cart,
                                parent,
                                false
                        );

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CartViewHolder holder,
            int position
    ) {

        CartItem item =
                cartList.get(position);

        // =========================
        // TÊN
        // =========================

        holder.tvCartProductName.setText(
                item.getProductName()
        );

        // =========================
        // GIÁ
        // =========================

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        holder.tvCartPrice.setText(
                "Giá: " +
                        formatter.format(
                                item.getPrice()
                        )
        );

        // =========================
        // SỐ LƯỢNG
        // =========================

        holder.tvCartQuantity.setText(
                String.valueOf(
                        item.getQuantity()
                )
        );

        // =========================
        // THÀNH TIỀN
        // =========================

        holder.tvCartTotal.setText(
                "Thành tiền: " +
                        formatter.format(
                                item.getTotalPrice()
                        )
        );

        // =========================
        // ẢNH
        // =========================

        String imageUri =
                item.getImageUri();

        if (imageUri != null &&
                !imageUri.isEmpty()) {

            try {

                holder.imgCartProduct.setImageURI(
                        Uri.parse(imageUri)
                );

            } catch (Exception e) {

                holder.imgCartProduct.setImageResource(
                        R.drawable.ic_launcher_foreground
                );
            }

        } else {

            holder.imgCartProduct.setImageResource(
                    R.drawable.ic_launcher_foreground
            );
        }

        // =========================
        // GIẢM SỐ LƯỢNG
        // =========================

        holder.btnCartMinus.setOnClickListener(v -> {

            int quantity =
                    item.getQuantity();

            if (quantity > 1) {

                quantity--;

                int result =
                        databaseHelper.updateCartQuantity(
                                item.getId(),
                                quantity
                        );

                if (result > 0) {

                    item.setQuantity(
                            quantity
                    );

                    notifyItemChanged(
                            holder.getBindingAdapterPosition()
                    );
                }

            } else {

                Toast.makeText(
                        v.getContext(),
                        "Số lượng tối thiểu là 1",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // =========================
        // TĂNG SỐ LƯỢNG
        // =========================

        holder.btnCartPlus.setOnClickListener(v -> {

            int quantity =
                    item.getQuantity();

            quantity++;

            int result =
                    databaseHelper.updateCartQuantity(
                            item.getId(),
                            quantity
                    );

            if (result > 0) {

                item.setQuantity(
                        quantity
                );

                notifyItemChanged(
                        holder.getBindingAdapterPosition()
                );
            }
        });

        // =========================
        // XÓA
        // =========================

        holder.btnCartDelete.setOnClickListener(v -> {

            int adapterPosition =
                    holder.getBindingAdapterPosition();

            if (adapterPosition ==
                    RecyclerView.NO_POSITION) {

                return;
            }

            CartItem deleteItem =
                    cartList.get(
                            adapterPosition
                    );

            int result =
                    databaseHelper.deleteCartItem(
                            deleteItem.getId()
                    );

            if (result > 0) {

                cartList.remove(
                        adapterPosition
                );

                notifyItemRemoved(
                        adapterPosition
                );

                Toast.makeText(
                        v.getContext(),
                        "Đã xóa sản phẩm khỏi giỏ hàng",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgCartProduct;

        TextView tvCartProductName;
        TextView tvCartPrice;
        TextView tvCartQuantity;
        TextView tvCartTotal;

        Button btnCartMinus;
        Button btnCartPlus;
        Button btnCartDelete;

        public CartViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            imgCartProduct =
                    itemView.findViewById(
                            R.id.imgCartProduct
                    );

            tvCartProductName =
                    itemView.findViewById(
                            R.id.tvCartProductName
                    );

            tvCartPrice =
                    itemView.findViewById(
                            R.id.tvCartPrice
                    );

            tvCartQuantity =
                    itemView.findViewById(
                            R.id.tvCartQuantity
                    );

            tvCartTotal =
                    itemView.findViewById(
                            R.id.tvCartTotal
                    );

            btnCartMinus =
                    itemView.findViewById(
                            R.id.btnCartMinus
                    );

            btnCartPlus =
                    itemView.findViewById(
                            R.id.btnCartPlus
                    );

            btnCartDelete =
                    itemView.findViewById(
                            R.id.btnCartDelete
                    );
        }
    }
}