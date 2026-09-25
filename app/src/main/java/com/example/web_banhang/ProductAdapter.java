package com.example.web_banhang;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter
        extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> productList;
    private String username;
    private DatabaseHelper databaseHelper;

    public ProductAdapter(
            ArrayList<Product> productList,
            String username,
            DatabaseHelper databaseHelper
    ) {

        this.productList = productList;
        this.username = username;
        this.databaseHelper = databaseHelper;
    }

    // =========================================================
    // CREATE ITEM
    // =========================================================

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

    // =========================================================
    // BIND ITEM
    // =========================================================

    @Override
    public void onBindViewHolder(
            @NonNull ProductViewHolder holder,
            int position
    ) {

        Product product =
                productList.get(position);

        // -----------------------------------------------------
        // TÊN
        // -----------------------------------------------------

        holder.tvProductName.setText(
                product.getName()
        );

        // -----------------------------------------------------
        // GIÁ
        // -----------------------------------------------------

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        holder.tvProductPrice.setText(
                formatter.format(
                        product.getPrice()
                )
        );

        // -----------------------------------------------------
        // TỒN KHO
        // -----------------------------------------------------

        holder.tvProductStock.setText(
                "Còn lại: " + product.getStock()
        );

        // -----------------------------------------------------
        // ẨN MÔ TẢ
        // -----------------------------------------------------

        if (holder.tvProductDescription != null) {

            holder.tvProductDescription.setVisibility(
                    View.GONE
            );
        }

        // -----------------------------------------------------
        // ẢNH
        // -----------------------------------------------------

        setProductImage(
                holder.imgProduct,
                product
        );

        // -----------------------------------------------------
        // RESET QUANTITY
        // -----------------------------------------------------

        holder.quantity = 1;

        holder.tvQuantity.setText("1");

        // -----------------------------------------------------
        // BUTTON EFFECT
        // -----------------------------------------------------

        setupButtonEffect(holder.btnMinus);
        setupButtonEffect(holder.btnPlus);
        setupButtonEffect(holder.btnAddCart);
        setupButtonEffect(holder.btnBuyNow);

        // -----------------------------------------------------
        // NÚT -
        // -----------------------------------------------------

        holder.btnMinus.setOnClickListener(v -> {

            if (holder.quantity > 1) {

                holder.quantity--;

                holder.tvQuantity.setText(
                        String.valueOf(
                                holder.quantity
                        )
                );
            }
        });

        // -----------------------------------------------------
        // NÚT +
        // -----------------------------------------------------

        holder.btnPlus.setOnClickListener(v -> {

            if (product.getStock() <= 0) {

                Toast.makeText(
                        v.getContext(),
                        "Sản phẩm đã hết hàng",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (holder.quantity <
                    product.getStock()) {

                holder.quantity++;

                holder.tvQuantity.setText(
                        String.valueOf(
                                holder.quantity
                        )
                );

            } else {

                Toast.makeText(
                        v.getContext(),
                        "Số lượng vượt quá tồn kho",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // -----------------------------------------------------
        // CLICK ẢNH
        // -----------------------------------------------------

        holder.imgProduct.setOnClickListener(v ->
                openProductDetail(
                        v.getContext(),
                        product
                )
        );

        // -----------------------------------------------------
        // CLICK TÊN
        // -----------------------------------------------------

        holder.tvProductName.setOnClickListener(v ->
                openProductDetail(
                        v.getContext(),
                        product
                )
        );

        // -----------------------------------------------------
        // THÊM GIỎ
        // -----------------------------------------------------

        holder.btnAddCart.setOnClickListener(v -> {

            Context context =
                    v.getContext();

            if (!isLoggedIn()) {

                openLogin(context);
                return;
            }

            if (product.getStock() <= 0) {

                Toast.makeText(
                        context,
                        "Sản phẩm đã hết hàng",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            int quantity =
                    holder.quantity;

            if (quantity <= 0) {

                Toast.makeText(
                        context,
                        "Số lượng không hợp lệ",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (quantity >
                    product.getStock()) {

                Toast.makeText(
                        context,
                        "Số lượng vượt quá tồn kho",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            long result =
                    databaseHelper.addToCart(
                            username,
                            product,
                            quantity
                    );

            if (result != -1) {

                Toast.makeText(
                        context,
                        "✓ Đã thêm vào giỏ hàng",
                        Toast.LENGTH_SHORT
                ).show();

                successAnimation(
                        holder.btnAddCart
                );

            } else {

                Toast.makeText(
                        context,
                        "Không thể thêm vào giỏ hàng",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // -----------------------------------------------------
        // MUA NGAY
        // -----------------------------------------------------

        holder.btnBuyNow.setOnClickListener(v -> {

            Context context =
                    v.getContext();

            if (!isLoggedIn()) {

                openLogin(context);
                return;
            }

            if (product.getStock() <= 0) {

                Toast.makeText(
                        context,
                        "Sản phẩm đã hết hàng",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            int quantity =
                    holder.quantity;

            if (quantity <= 0) {

                Toast.makeText(
                        context,
                        "Số lượng không hợp lệ",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (quantity >
                    product.getStock()) {

                Toast.makeText(
                        context,
                        "Số lượng vượt quá tồn kho",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            long result =
                    databaseHelper.addToCart(
                            username,
                            product,
                            quantity
                    );

            if (result == -1) {

                Toast.makeText(
                        context,
                        "Không thể thêm sản phẩm vào giỏ",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent =
                    new Intent(
                            context,
                            CartActivity.class
                    );

            intent.putExtra(
                    "username",
                    username
            );

            context.startActivity(intent);
        });
    }

    // =========================================================
    // HIỂN THỊ ẢNH
    // =========================================================

    private void setProductImage(
            ImageView imageView,
            Product product
    ) {

        imageView.setImageResource(
                android.R.drawable.ic_menu_gallery
        );

        // -----------------------------------------------------
        // ƯU TIÊN ẢNH ĐÃ LƯU
        // -----------------------------------------------------

        String imagePath =
                product.getImageUri();

        if (imagePath != null
                && !imagePath.trim().isEmpty()) {

            try {

                File imageFile =
                        new File(imagePath);

                if (imageFile.exists()) {

                    imageView.setImageURI(
                            Uri.fromFile(imageFile)
                    );

                    return;
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        // -----------------------------------------------------
        // NẾU KHÔNG CÓ ẢNH ĐÃ LƯU
        // THÌ DÙNG DRAWABLE
        // -----------------------------------------------------

        setDrawableImage(
                imageView,
                product
        );
    }

    // =========================================================
    // ẢNH DRAWABLE
    // =========================================================

    private void setDrawableImage(
            ImageView imageView,
            Product product
    ) {

        String productName =
                product.getName();

        if (productName == null) {

            return;
        }

        productName =
                productName.trim()
                        .toLowerCase(
                                Locale.getDefault()
                        );

        // -----------------------------------------------------
        // BÚT CHÌ
        // -----------------------------------------------------

        if (productName.contains("bút chì")
                || productName.contains("but chi")
                || productName.contains("butchi")) {

            imageView.setImageResource(
                    R.drawable.butchi
            );

            return;
        }

        // -----------------------------------------------------
        // TẨY
        // -----------------------------------------------------

        if (productName.contains("tẩy")
                || productName.contains("tay")) {

            imageView.setImageResource(
                    R.drawable.tay
            );

            return;
        }

        // -----------------------------------------------------
        // VỞ
        // -----------------------------------------------------

        if (productName.contains("vở")
                || productName.contains("vo")) {

            imageView.setImageResource(
                    R.drawable.vo
            );
        }
    }

    // =========================================================
    // CHI TIẾT
    // =========================================================

    private void openProductDetail(
            Context context,
            Product product
    ) {

        Intent intent =
                new Intent(
                        context,
                        ProductDetailActivity.class
                );

        intent.putExtra(
                "name",
                product.getName()
        );

        intent.putExtra(
                "price",
                product.getPrice()
        );

        intent.putExtra(
                "stock",
                product.getStock()
        );

        intent.putExtra(
                "description",
                product.getDescription()
        );

        intent.putExtra(
                "imageUri",
                product.getImageUri()
        );

        intent.putExtra(
                "productName",
                product.getName()
        );

        context.startActivity(intent);
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private boolean isLoggedIn() {

        return username != null
                && !username.trim().isEmpty();
    }

    private void openLogin(
            Context context
    ) {

        Intent intent =
                new Intent(
                        context,
                        LoginActivity.class
                );

        intent.putExtra(
                "from_home",
                true
        );

        context.startActivity(intent);
    }

    // =========================================================
    // BUTTON EFFECT
    // =========================================================

    private void setupButtonEffect(
            Button button
    ) {

        if (button == null) {
            return;
        }

        button.setOnHoverListener(
                (v, event) -> {

                    if (event.getAction() ==
                            MotionEvent.ACTION_HOVER_ENTER) {

                        v.animate()
                                .scaleX(1.06f)
                                .scaleY(1.06f)
                                .alpha(0.82f)
                                .setDuration(150)
                                .start();

                    } else if (
                            event.getAction() ==
                                    MotionEvent.ACTION_HOVER_EXIT
                    ) {

                        v.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .alpha(1.0f)
                                .setDuration(150)
                                .start();
                    }

                    return false;
                }
        );

        button.setOnTouchListener(
                (v, event) -> {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            v.animate()
                                    .scaleX(0.90f)
                                    .scaleY(0.90f)
                                    .alpha(0.65f)
                                    .setDuration(70)
                                    .start();

                            break;

                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:

                            v.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .alpha(1.0f)
                                    .setDuration(120)
                                    .start();

                            break;
                    }

                    return false;
                }
        );
    }

    // =========================================================
    // SUCCESS ANIMATION
    // =========================================================

    private void successAnimation(
            Button button
    ) {

        button.animate()
                .scaleX(1.12f)
                .scaleY(1.12f)
                .setDuration(100)
                .withEndAction(() ->
                        button.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100)
                                .start()
                )
                .start();
    }

    // =========================================================
    // COUNT
    // =========================================================

    @Override
    public int getItemCount() {

        if (productList == null) {
            return 0;
        }

        return productList.size();
    }

    // =========================================================
    // VIEW HOLDER
    // =========================================================

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

        int quantity = 1;

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