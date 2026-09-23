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

public class ProductAdapter
        extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final ArrayList<Product> productList;
    private final String username;
    private final DatabaseHelper databaseHelper;

    public ProductAdapter(
            ArrayList<Product> productList,
            String username,
            DatabaseHelper databaseHelper
    ) {
        this.productList = productList;
        this.username = username;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_product,
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

        Product product =
                productList.get(position);

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
                formatter.format(
                        product.getPrice()
                )
        );

        // =========================
        // TỒN KHO
        // =========================

        holder.tvProductStock.setText(
                "Tồn kho: " +
                        product.getStock()
        );

        // =========================
        // MÔ TẢ
        // =========================

        holder.tvProductDescription.setText(
                product.getDescription()
        );

        // =========================
        // ẢNH
        // =========================

        String imageUri =
                product.getImageUri();

        if (imageUri != null &&
                !imageUri.isEmpty()) {

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
        // SỐ LƯỢNG BAN ĐẦU
        // =========================

        holder.tvQuantity.setText("1");

        // =========================
        // NÚT -
        // =========================

        holder.btnMinus.setOnClickListener(v -> {

            int quantity =
                    Integer.parseInt(
                            holder.tvQuantity
                                    .getText()
                                    .toString()
                    );

            if (quantity > 1) {

                quantity--;

                holder.tvQuantity.setText(
                        String.valueOf(quantity)
                );
            }
        });

        // =========================
        // NÚT +
        // =========================

        holder.btnPlus.setOnClickListener(v -> {

            int quantity =
                    Integer.parseInt(
                            holder.tvQuantity
                                    .getText()
                                    .toString()
                    );

            if (quantity < product.getStock()) {

                quantity++;

                holder.tvQuantity.setText(
                        String.valueOf(quantity)
                );

            } else {

                Toast.makeText(
                        v.getContext(),
                        "Đã đạt số lượng tồn kho",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // =========================
        // THÊM VÀO GIỎ HÀNG
        // =========================

        holder.btnAddCart.setOnClickListener(v -> {

            int quantity =
                    Integer.parseInt(
                            holder.tvQuantity
                                    .getText()
                                    .toString()
                    );

            // Kiểm tra đăng nhập
            if (username == null ||
                    username.trim().isEmpty()) {

                Toast.makeText(
                        v.getContext(),
                        "Không xác định được tài khoản",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Kiểm tra số lượng
            if (quantity <= 0) {

                Toast.makeText(
                        v.getContext(),
                        "Số lượng không hợp lệ",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Kiểm tra tồn kho
            if (product.getStock() <= 0) {

                Toast.makeText(
                        v.getContext(),
                        "Sản phẩm đã hết hàng",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (quantity > product.getStock()) {

                Toast.makeText(
                        v.getContext(),
                        "Số lượng vượt quá tồn kho",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Thêm vào database
            long result =
                    databaseHelper.addToCart(
                            username,
                            product,
                            quantity
                    );

            if (result > 0) {

                Toast.makeText(
                        v.getContext(),
                        "Đã thêm vào giỏ hàng",
                        Toast.LENGTH_SHORT
                ).show();

            } else if (result == -2) {

                Toast.makeText(
                        v.getContext(),
                        "Số lượng trong giỏ vượt quá tồn kho",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        v.getContext(),
                        "Không thể thêm vào giỏ hàng",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // =========================
        // MUA NGAY
        // =========================

        holder.btnBuyNow.setOnClickListener(v -> {

            int quantity =
                    Integer.parseInt(
                            holder.tvQuantity
                                    .getText()
                                    .toString()
                    );

            if (product.getStock() <= 0) {

                Toast.makeText(
                        v.getContext(),
                        "Sản phẩm đã hết hàng",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (quantity > product.getStock()) {

                Toast.makeText(
                        v.getContext(),
                        "Số lượng vượt quá tồn kho",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                    v.getContext(),
                    "Chức năng mua ngay sẽ thực hiện ở bước thanh toán",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // =========================
    // VIEW HOLDER
    // =========================

    public static class ProductViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgProduct;

        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductStock;
        TextView tvProductDescription;
        TextView tvQuantity;

        Button btnMinus;
        Button btnPlus;
        Button btnAddCart;
        Button btnBuyNow;

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

            tvQuantity =
                    itemView.findViewById(
                            R.id.tvQuantity
                    );

            btnMinus =
                    itemView.findViewById(
                            R.id.btnMinus
                    );

            btnPlus =
                    itemView.findViewById(
                            R.id.btnPlus
                    );

            btnAddCart =
                    itemView.findViewById(
                            R.id.btnAddCart
                    );

            btnBuyNow =
                    itemView.findViewById(
                            R.id.btnBuyNow
                    );
        }
    }
}