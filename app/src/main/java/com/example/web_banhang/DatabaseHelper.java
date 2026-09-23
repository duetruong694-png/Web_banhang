package com.example.web_banhang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shop.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_PRODUCT = "products";
    private static final String TABLE_CART = "cart_items";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // =========================
    // TẠO DATABASE
    // =========================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // =========================
        // BẢNG SẢN PHẨM
        // =========================

        String createProductTable =
                "CREATE TABLE " + TABLE_PRODUCT + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "description TEXT, " +
                        "stock INTEGER NOT NULL, " +
                        "imageUri TEXT" +
                        ")";

        db.execSQL(createProductTable);


        // =========================
        // BẢNG GIỎ HÀNG
        // =========================

        String createCartTable =
                "CREATE TABLE " + TABLE_CART + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT NOT NULL, " +
                        "productId INTEGER NOT NULL, " +
                        "productName TEXT NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "quantity INTEGER NOT NULL, " +
                        "imageUri TEXT" +
                        ")";

        db.execSQL(createCartTable);


        // =========================
        // BẢNG ĐƠN HÀNG
        // =========================

        String createOrderTable =
                "CREATE TABLE orders (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT NOT NULL, " +
                        "customerName TEXT NOT NULL, " +
                        "phone TEXT NOT NULL, " +
                        "address TEXT NOT NULL, " +
                        "paymentMethod TEXT NOT NULL, " +
                        "totalMoney REAL NOT NULL, " +
                        "status TEXT NOT NULL, " +
                        "orderDate TEXT NOT NULL" +
                        ")";

        db.execSQL(createOrderTable);


        // =========================
        // BẢNG CHI TIẾT ĐƠN HÀNG
        // =========================

        String createOrderItemTable =
                "CREATE TABLE order_items (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "orderId INTEGER NOT NULL, " +
                        "productId INTEGER NOT NULL, " +
                        "productName TEXT NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "quantity INTEGER NOT NULL" +
                        ")";

        db.execSQL(createOrderItemTable);
    }

    // =========================
    // NÂNG CẤP DATABASE
    // =========================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        // =========================
        // VERSION 2
        // =========================

        if (oldVersion < 2) {

            try {

                db.execSQL(
                        "ALTER TABLE " +
                                TABLE_PRODUCT +
                                " ADD COLUMN imageUri TEXT"
                );

            } catch (Exception ignored) {
            }
        }


        // =========================
        // VERSION 3
        // =========================

        if (oldVersion < 3) {

            String createCartTable =
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_CART + " (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "username TEXT NOT NULL, " +
                            "productId INTEGER NOT NULL, " +
                            "productName TEXT NOT NULL, " +
                            "price REAL NOT NULL, " +
                            "quantity INTEGER NOT NULL, " +
                            "imageUri TEXT" +
                            ")";

            db.execSQL(createCartTable);
        }


        // =========================
        // VERSION 4
        // =========================

        if (oldVersion < 4) {

            String createOrderTable =
                    "CREATE TABLE IF NOT EXISTS orders (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "username TEXT NOT NULL, " +
                            "customerName TEXT NOT NULL, " +
                            "phone TEXT NOT NULL, " +
                            "address TEXT NOT NULL, " +
                            "paymentMethod TEXT NOT NULL, " +
                            "totalMoney REAL NOT NULL, " +
                            "status TEXT NOT NULL, " +
                            "orderDate TEXT NOT NULL" +
                            ")";

            db.execSQL(createOrderTable);


            String createOrderItemTable =
                    "CREATE TABLE IF NOT EXISTS order_items (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "orderId INTEGER NOT NULL, " +
                            "productId INTEGER NOT NULL, " +
                            "productName TEXT NOT NULL, " +
                            "price REAL NOT NULL, " +
                            "quantity INTEGER NOT NULL" +
                            ")";

            db.execSQL(createOrderItemTable);
        }
    }

    // =========================
    // PRODUCT
    // =========================

    public long addProduct(Product product) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", product.getName());
        values.put("price", product.getPrice());
        values.put("description", product.getDescription());
        values.put("stock", product.getStock());
        values.put("imageUri", product.getImageUri());

        long result = db.insert(
                TABLE_PRODUCT,
                null,
                values
        );

        db.close();

        return result;
    }

    // =========================
    // LẤY TẤT CẢ SẢN PHẨM
    // =========================

    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> productList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " +
                        TABLE_PRODUCT +
                        " ORDER BY id DESC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        );

                double price =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("price")
                        );

                String description =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("description")
                        );

                int stock =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("stock")
                        );

                String imageUri =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("imageUri")
                        );

                Product product =
                        new Product(
                                id,
                                name,
                                price,
                                description,
                                stock,
                                imageUri
                        );

                productList.add(product);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return productList;
    }

    // =========================
    // SỬA PRODUCT
    // =========================

    public int updateProduct(Product product) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("name", product.getName());
        values.put("price", product.getPrice());
        values.put("description", product.getDescription());
        values.put("stock", product.getStock());
        values.put("imageUri", product.getImageUri());

        int result =
                db.update(
                        TABLE_PRODUCT,
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(product.getId())
                        }
                );

        db.close();

        return result;
    }

    // =========================
    // XÓA PRODUCT
    // =========================

    public int deleteProduct(int productId) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_PRODUCT,
                        "id = ?",
                        new String[]{
                                String.valueOf(productId)
                        }
                );

        db.close();

        return result;
    }

    // =========================
    // THÊM VÀO GIỎ HÀNG
    // =========================

    public long addToCart(
            String username,
            Product product,
            int quantity
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        // Kiểm tra sản phẩm đã có trong giỏ chưa
        Cursor cursor =
                db.rawQuery(
                        "SELECT id, quantity " +
                                "FROM " + TABLE_CART +
                                " WHERE username = ? " +
                                "AND productId = ?",
                        new String[]{
                                username,
                                String.valueOf(product.getId())
                        }
                );

        // =========================
        // ĐÃ CÓ TRONG GIỎ
        // =========================

        if (cursor.moveToFirst()) {

            int cartId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow("id")
                    );

            int oldQuantity =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow("quantity")
                    );

            int newQuantity =
                    oldQuantity + quantity;

            // Không vượt quá tồn kho
            if (newQuantity > product.getStock()) {

                cursor.close();
                db.close();

                return -2;
            }

            ContentValues values =
                    new ContentValues();

            values.put(
                    "quantity",
                    newQuantity
            );

            values.put(
                    "productName",
                    product.getName()
            );

            values.put(
                    "price",
                    product.getPrice()
            );

            values.put(
                    "imageUri",
                    product.getImageUri()
            );

            int result =
                    db.update(
                            TABLE_CART,
                            values,
                            "id = ?",
                            new String[]{
                                    String.valueOf(cartId)
                            }
                    );

            cursor.close();
            db.close();

            return result;
        }

        // =========================
        // CHƯA CÓ TRONG GIỎ
        // =========================

        if (quantity > product.getStock()) {

            cursor.close();
            db.close();

            return -2;
        }

        ContentValues values =
                new ContentValues();

        values.put(
                "username",
                username
        );

        values.put(
                "productId",
                product.getId()
        );

        values.put(
                "productName",
                product.getName()
        );

        values.put(
                "price",
                product.getPrice()
        );

        values.put(
                "quantity",
                quantity
        );

        values.put(
                "imageUri",
                product.getImageUri()
        );

        long result =
                db.insert(
                        TABLE_CART,
                        null,
                        values
                );

        cursor.close();
        db.close();

        return result;
    }

    // =========================
    // LẤY GIỎ HÀNG THEO USER
    // =========================

    public ArrayList<CartItem> getCartItems(
            String username
    ) {

        ArrayList<CartItem> cartList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                TABLE_CART +
                                " WHERE username = ? " +
                                "ORDER BY id DESC",
                        new String[]{
                                username
                        }
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        );

                String cartUsername =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("username")
                        );

                int productId =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("productId")
                        );

                String productName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("productName")
                        );

                double price =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("price")
                        );

                int quantity =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("quantity")
                        );

                String imageUri =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("imageUri")
                        );

                CartItem item =
                        new CartItem(
                                id,
                                cartUsername,
                                productId,
                                productName,
                                price,
                                quantity,
                                imageUri
                        );

                cartList.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return cartList;
    }

    // =========================
    // XÓA SẢN PHẨM KHỎI GIỎ
    // =========================

    public int deleteCartItem(int cartId) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_CART,
                        "id = ?",
                        new String[]{
                                String.valueOf(cartId)
                        }
                );

        db.close();

        return result;
    }

    // =========================
    // CẬP NHẬT SỐ LƯỢNG
    // =========================

    public int updateCartQuantity(
            int cartId,
            int quantity
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "quantity",
                quantity
        );

        int result =
                db.update(
                        TABLE_CART,
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(cartId)
                        }
                );

        db.close();

        return result;
    }

    // =========================
    // XÓA TOÀN BỘ GIỎ CỦA USER
    // =========================

    public int clearCart(String username) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_CART,
                        "username = ?",
                        new String[]{
                                username
                        }
                );

        db.close();

        return result;
    }
    public long createOrder(
            String username,
            String customerName,
            String phone,
            String address,
            String paymentMethod,
            double totalMoney,
            ArrayList<CartItem> cartList
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        long orderId = -1;

        db.beginTransaction();

        try {

            // =========================
            // TẠO ĐƠN HÀNG
            // =========================

            ContentValues orderValues =
                    new ContentValues();

            orderValues.put(
                    "username",
                    username
            );

            orderValues.put(
                    "customerName",
                    customerName
            );

            orderValues.put(
                    "phone",
                    phone
            );

            orderValues.put(
                    "address",
                    address
            );

            orderValues.put(
                    "paymentMethod",
                    paymentMethod
            );

            orderValues.put(
                    "totalMoney",
                    totalMoney
            );

            orderValues.put(
                    "status",
                    "Chờ xác nhận"
            );

            String orderDate =
                    new java.text.SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss",
                            java.util.Locale.getDefault()
                    ).format(
                            new java.util.Date()
                    );

            orderValues.put(
                    "orderDate",
                    orderDate
            );

            orderId =
                    db.insert(
                            "orders",
                            null,
                            orderValues
                    );

            if (orderId == -1) {

                db.endTransaction();

                db.close();

                return -1;
            }


            // =========================
            // LƯU CHI TIẾT ĐƠN
            // =========================

            for (CartItem item : cartList) {

                ContentValues itemValues =
                        new ContentValues();

                itemValues.put(
                        "orderId",
                        orderId
                );

                itemValues.put(
                        "productId",
                        item.getProductId()
                );

                itemValues.put(
                        "productName",
                        item.getProductName()
                );

                itemValues.put(
                        "price",
                        item.getPrice()
                );

                itemValues.put(
                        "quantity",
                        item.getQuantity()
                );

                long itemResult =
                        db.insert(
                                "order_items",
                                null,
                                itemValues
                        );

                if (itemResult == -1) {

                    db.endTransaction();

                    db.close();

                    return -1;
                }
            }


            // =========================
            // TRỪ TỒN KHO
            // =========================

            for (CartItem item : cartList) {

                Cursor cursor =
                        db.rawQuery(
                                "SELECT stock FROM products " +
                                        "WHERE id = ?",
                                new String[]{
                                        String.valueOf(
                                                item.getProductId()
                                        )
                                }
                        );

                if (cursor.moveToFirst()) {

                    int currentStock =
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                            "stock"
                                    )
                            );

                    int newStock =
                            currentStock -
                                    item.getQuantity();

                    if (newStock < 0) {

                        cursor.close();

                        db.endTransaction();

                        db.close();

                        return -2;
                    }

                    ContentValues stockValues =
                            new ContentValues();

                    stockValues.put(
                            "stock",
                            newStock
                    );

                    db.update(
                            TABLE_PRODUCT,
                            stockValues,
                            "id = ?",
                            new String[]{
                                    String.valueOf(
                                            item.getProductId()
                                    )
                            }
                    );
                }

                cursor.close();
            }


            // =========================
            // XÓA GIỎ HÀNG
            // =========================

            db.delete(
                    TABLE_CART,
                    "username = ?",
                    new String[]{
                            username
                    }
            );


            db.setTransactionSuccessful();

        } catch (Exception e) {

            orderId = -1;

        } finally {

            db.endTransaction();
            db.close();
        }

        return orderId;
    }
}