package com.example.web_banhang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shop.db";

    // =====================================================
    // DATABASE VERSION
    // =====================================================

    private static final int DATABASE_VERSION = 6;

    private static final String TABLE_PRODUCT = "products";
    private static final String TABLE_CART = "cart_items";
    private static final String TABLE_ORDER = "orders";
    private static final String TABLE_ORDER_ITEM = "order_items";
    private static final String TABLE_USER = "users";

    public DatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    // =====================================================
    // CREATE DATABASE
    // =====================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // -------------------------------------------------
        // PRODUCTS
        // -------------------------------------------------

        String createProductTable =
                "CREATE TABLE " + TABLE_PRODUCT + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "productCode TEXT, " +
                        "name TEXT NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "description TEXT, " +
                        "stock INTEGER NOT NULL, " +
                        "imageUri TEXT, " +
                        "saleDate TEXT, " +
                        "status TEXT DEFAULT 'Đang bán'" +
                        ")";

        db.execSQL(createProductTable);

        // -------------------------------------------------
        // CART
        // -------------------------------------------------

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

        // -------------------------------------------------
        // ORDERS
        // -------------------------------------------------

        String createOrderTable =
                "CREATE TABLE " + TABLE_ORDER + " (" +
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

        // -------------------------------------------------
        // ORDER ITEMS
        // -------------------------------------------------

        String createOrderItemTable =
                "CREATE TABLE " + TABLE_ORDER_ITEM + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "orderId INTEGER NOT NULL, " +
                        "productId INTEGER NOT NULL, " +
                        "productName TEXT NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "quantity INTEGER NOT NULL" +
                        ")";

        db.execSQL(createOrderItemTable);

        // -------------------------------------------------
        // USERS
        // -------------------------------------------------

        String createUserTable =
                "CREATE TABLE " + TABLE_USER + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT UNIQUE NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "fullName TEXT, " +
                        "phone TEXT, " +
                        "address TEXT, " +
                        "role TEXT NOT NULL" +
                        ")";

        db.execSQL(createUserTable);

        // -------------------------------------------------
        // ADMIN MẶC ĐỊNH
        // -------------------------------------------------

        ContentValues adminValues = new ContentValues();

        adminValues.put("username", "admin");
        adminValues.put("password", "123456");
        adminValues.put("fullName", "Quản trị viên");
        adminValues.put("phone", "");
        adminValues.put("address", "");
        adminValues.put("role", "admin");

        db.insert(
                TABLE_USER,
                null,
                adminValues
        );

        // -------------------------------------------------
        // USER MẶC ĐỊNH
        // -------------------------------------------------

        ContentValues userValues = new ContentValues();

        userValues.put("username", "user");
        userValues.put("password", "123456");
        userValues.put("fullName", "Khách hàng");
        userValues.put("phone", "");
        userValues.put("address", "");
        userValues.put("role", "user");

        db.insert(
                TABLE_USER,
                null,
                userValues
        );
    }

    // =====================================================
    // DATABASE UPGRADE
    // =====================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        // =================================================
        // VERSION 2
        // =================================================

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

        // =================================================
        // VERSION 3
        // =================================================

        if (oldVersion < 3) {

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_CART +
                            " (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "username TEXT NOT NULL, " +
                            "productId INTEGER NOT NULL, " +
                            "productName TEXT NOT NULL, " +
                            "price REAL NOT NULL, " +
                            "quantity INTEGER NOT NULL, " +
                            "imageUri TEXT" +
                            ")"
            );
        }

        // =================================================
        // VERSION 4
        // =================================================

        if (oldVersion < 4) {

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_ORDER +
                            " (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "username TEXT NOT NULL, " +
                            "customerName TEXT NOT NULL, " +
                            "phone TEXT NOT NULL, " +
                            "address TEXT NOT NULL, " +
                            "paymentMethod TEXT NOT NULL, " +
                            "totalMoney REAL NOT NULL, " +
                            "status TEXT NOT NULL, " +
                            "orderDate TEXT NOT NULL" +
                            ")"
            );

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_ORDER_ITEM +
                            " (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "orderId INTEGER NOT NULL, " +
                            "productId INTEGER NOT NULL, " +
                            "productName TEXT NOT NULL, " +
                            "price REAL NOT NULL, " +
                            "quantity INTEGER NOT NULL" +
                            ")"
            );
        }

        // =================================================
        // VERSION 5
        // =================================================

        if (oldVersion < 5) {

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_USER +
                            " (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "username TEXT UNIQUE NOT NULL, " +
                            "password TEXT NOT NULL, " +
                            "fullName TEXT, " +
                            "phone TEXT, " +
                            "address TEXT, " +
                            "role TEXT NOT NULL" +
                            ")"
            );

            createDefaultUsers(db);
        }

        // =================================================
        // VERSION 6
        // THÊM THUỘC TÍNH SẢN PHẨM
        // =================================================

        if (oldVersion < 6) {

            try {
                db.execSQL(
                        "ALTER TABLE " +
                                TABLE_PRODUCT +
                                " ADD COLUMN productCode TEXT"
                );
            } catch (Exception ignored) {
            }

            try {
                db.execSQL(
                        "ALTER TABLE " +
                                TABLE_PRODUCT +
                                " ADD COLUMN saleDate TEXT"
                );
            } catch (Exception ignored) {
            }

            try {
                db.execSQL(
                        "ALTER TABLE " +
                                TABLE_PRODUCT +
                                " ADD COLUMN status TEXT DEFAULT 'Đang bán'"
                );
            } catch (Exception ignored) {
            }

            // Các sản phẩm cũ
            try {
                db.execSQL(
                        "UPDATE " +
                                TABLE_PRODUCT +
                                " SET status = 'Đang bán' " +
                                "WHERE status IS NULL OR status = ''"
                );
            } catch (Exception ignored) {
            }
        }
    }

    // =====================================================
    // TẠO USER MẶC ĐỊNH
    // =====================================================

    private void createDefaultUsers(SQLiteDatabase db) {

        Cursor adminCursor = db.rawQuery(
                "SELECT id FROM " +
                        TABLE_USER +
                        " WHERE username = ?",
                new String[]{"admin"}
        );

        if (!adminCursor.moveToFirst()) {

            ContentValues values = new ContentValues();

            values.put("username", "admin");
            values.put("password", "123456");
            values.put("fullName", "Quản trị viên");
            values.put("phone", "");
            values.put("address", "");
            values.put("role", "admin");

            db.insert(
                    TABLE_USER,
                    null,
                    values
            );
        }

        adminCursor.close();

        Cursor userCursor = db.rawQuery(
                "SELECT id FROM " +
                        TABLE_USER +
                        " WHERE username = ?",
                new String[]{"user"}
        );

        if (!userCursor.moveToFirst()) {

            ContentValues values = new ContentValues();

            values.put("username", "user");
            values.put("password", "123456");
            values.put("fullName", "Khách hàng");
            values.put("phone", "");
            values.put("address", "");
            values.put("role", "user");

            db.insert(
                    TABLE_USER,
                    null,
                    values
            );
        }

        userCursor.close();
    }

    // =====================================================
    // PRODUCT
    // =====================================================

    public long addProduct(Product product) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                "productCode",
                product.getProductCode()
        );

        values.put(
                "name",
                product.getName()
        );

        values.put(
                "price",
                product.getPrice()
        );

        values.put(
                "description",
                product.getDescription()
        );

        values.put(
                "stock",
                product.getStock()
        );

        values.put(
                "imageUri",
                product.getImageUri()
        );

        values.put(
                "saleDate",
                product.getSaleDate()
        );

        values.put(
                "status",
                product.getStatus()
        );

        long result = db.insert(
                TABLE_PRODUCT,
                null,
                values
        );

        db.close();

        return result;
    }

    // =====================================================
    // KIỂM TRA MÃ SẢN PHẨM
    // =====================================================

    public boolean isProductCodeExists(String productCode) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " +
                        TABLE_PRODUCT +
                        " WHERE productCode = ?",
                new String[]{
                        productCode
                }
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }

    // =====================================================
    // KIỂM TRA MÃ SẢN PHẨM KHI SỬA
    // =====================================================

    public boolean isProductCodeExists(
            String productCode,
            int excludeProductId
    ) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " +
                        TABLE_PRODUCT +
                        " WHERE productCode = ? " +
                        "AND id != ?",
                new String[]{
                        productCode,
                        String.valueOf(excludeProductId)
                }
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }

    // =====================================================
    // GET ALL PRODUCTS
    // =====================================================

    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> list =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                TABLE_PRODUCT +
                                " ORDER BY id DESC",
                        null
                );

        if (cursor.moveToFirst()) {

            do {

                list.add(cursorToProduct(cursor));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    // =====================================================
    // GET PRODUCT BY ID
    // =====================================================

    public Product getProductById(int productId) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                TABLE_PRODUCT +
                                " WHERE id = ?",
                        new String[]{
                                String.valueOf(productId)
                        }
                );

        Product product = null;

        if (cursor.moveToFirst()) {

            product = cursorToProduct(cursor);
        }

        cursor.close();
        db.close();

        return product;
    }

    // =====================================================
    // CONVERT CURSOR -> PRODUCT
    // =====================================================

    private Product cursorToProduct(Cursor cursor) {

        int id =
                cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

        String productCode =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "productCode"
                        )
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
                        cursor.getColumnIndexOrThrow(
                                "description"
                        )
                );

        int stock =
                cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                "stock"
                        )
                );

        String imageUri =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "imageUri"
                        )
                );

        String saleDate =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "saleDate"
                        )
                );

        String status =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "status"
                        )
                );

        if (productCode == null) {
            productCode = "";
        }

        if (saleDate == null) {
            saleDate = "";
        }

        if (status == null || status.isEmpty()) {
            status = "Đang bán";
        }

        return new Product(
                id,
                productCode,
                name,
                price,
                description,
                stock,
                imageUri,
                saleDate,
                status
        );
    }

    // =====================================================
    // UPDATE PRODUCT
    // =====================================================

    public int updateProduct(Product product) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "productCode",
                product.getProductCode()
        );

        values.put(
                "name",
                product.getName()
        );

        values.put(
                "price",
                product.getPrice()
        );

        values.put(
                "description",
                product.getDescription()
        );

        values.put(
                "stock",
                product.getStock()
        );

        values.put(
                "imageUri",
                product.getImageUri()
        );

        values.put(
                "saleDate",
                product.getSaleDate()
        );

        values.put(
                "status",
                product.getStatus()
        );

        int result =
                db.update(
                        TABLE_PRODUCT,
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(
                                        product.getId()
                                )
                        }
                );

        db.close();

        return result;
    }

    // =====================================================
    // DELETE PRODUCT
    // =====================================================

    public int deleteProduct(int productId) {

        SQLiteDatabase db =
                getWritableDatabase();

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

    // =====================================================
    // CART
    // =====================================================

    public long addToCart(
            String username,
            Product product,
            int quantity
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, quantity " +
                                "FROM " + TABLE_CART +
                                " WHERE username = ? " +
                                "AND productId = ?",
                        new String[]{
                                username,
                                String.valueOf(
                                        product.getId()
                                )
                        }
                );

        if (cursor.moveToFirst()) {

            int cartId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "id"
                            )
                    );

            int oldQuantity =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "quantity"
                            )
                    );

            int newQuantity =
                    oldQuantity + quantity;

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

    public ArrayList<CartItem> getCartItems(
            String username
    ) {

        ArrayList<CartItem> list =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

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
                                cursor.getColumnIndexOrThrow(
                                        "id"
                                )
                        );

                String cartUsername =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "username"
                                )
                        );

                int productId =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "productId"
                                )
                        );

                String productName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "productName"
                                )
                        );

                double price =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "price"
                                )
                        );

                int quantity =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "quantity"
                                )
                        );

                String imageUri =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "imageUri"
                                )
                        );

                list.add(
                        new CartItem(
                                id,
                                cartUsername,
                                productId,
                                productName,
                                price,
                                quantity,
                                imageUri
                        )
                );

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public int updateCartQuantity(
            int cartId,
            int quantity
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

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

    public int deleteCartItem(int cartId) {

        SQLiteDatabase db =
                getWritableDatabase();

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

    public int clearCart(String username) {

        SQLiteDatabase db =
                getWritableDatabase();

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

    // =====================================================
    // CREATE ORDER
    // =====================================================

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
                getWritableDatabase();

        long orderId = -1;

        db.beginTransaction();

        try {

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
                    new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss",
                            Locale.getDefault()
                    ).format(
                            new Date()
                    );

            orderValues.put(
                    "orderDate",
                    orderDate
            );

            orderId =
                    db.insert(
                            TABLE_ORDER,
                            null,
                            orderValues
                    );

            if (orderId == -1) {
                return -1;
            }

            // ---------------------------------------------
            // ORDER ITEMS
            // ---------------------------------------------

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

                long result =
                        db.insert(
                                TABLE_ORDER_ITEM,
                                null,
                                itemValues
                        );

                if (result == -1) {
                    return -1;
                }
            }

            // ---------------------------------------------
            // TRỪ TỒN KHO
            // ---------------------------------------------

            for (CartItem item : cartList) {

                Cursor cursor =
                        db.rawQuery(
                                "SELECT stock FROM " +
                                        TABLE_PRODUCT +
                                        " WHERE id = ?",
                                new String[]{
                                        String.valueOf(
                                                item.getProductId()
                                        )
                                }
                        );

                if (cursor.moveToFirst()) {

                    int stock =
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                            "stock"
                                    )
                            );

                    int newStock =
                            stock -
                                    item.getQuantity();

                    if (newStock < 0) {

                        cursor.close();

                        return -2;
                    }

                    ContentValues values =
                            new ContentValues();

                    values.put(
                            "stock",
                            newStock
                    );

                    db.update(
                            TABLE_PRODUCT,
                            values,
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

            // ---------------------------------------------
            // XÓA CART
            // ---------------------------------------------

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

    // =====================================================
    // LẤY DANH SÁCH ĐƠN HÀNG
    // =====================================================

    public ArrayList<Order> getAllOrders() {

        ArrayList<Order> list =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                TABLE_ORDER +
                                " ORDER BY id DESC",
                        null
                );

        if (cursor.moveToFirst()) {

            do {

                Order order = new Order();

                order.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        )
                );

                order.setUsername(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "username"
                                )
                        )
                );

                order.setCustomerName(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "customerName"
                                )
                        )
                );

                order.setPhone(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "phone"
                                )
                        )
                );

                order.setAddress(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "address"
                                )
                        )
                );

                order.setPaymentMethod(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "paymentMethod"
                                )
                        )
                );

                order.setTotalMoney(
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "totalMoney"
                                )
                        )
                );

                order.setStatus(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "status"
                                )
                        )
                );

                order.setOrderDate(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "orderDate"
                                )
                        )
                );

                list.add(order);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    // =====================================================
    // LẤY ĐƠN HÀNG THEO ID
    // =====================================================

    public Order getOrderById(int orderId) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                TABLE_ORDER +
                                " WHERE id = ?",
                        new String[]{
                                String.valueOf(orderId)
                        }
                );

        Order order = null;

        if (cursor.moveToFirst()) {

            order = new Order();

            order.setId(
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow("id")
                    )
            );

            order.setUsername(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "username"
                            )
                    )
            );

            order.setCustomerName(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "customerName"
                            )
                    )
            );

            order.setPhone(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "phone"
                            )
                    )
            );

            order.setAddress(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "address"
                            )
                    )
            );

            order.setPaymentMethod(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "paymentMethod"
                            )
                    )
            );

            order.setTotalMoney(
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                    "totalMoney"
                            )
                    )
            );

            order.setStatus(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "status"
                            )
                    )
            );

            order.setOrderDate(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "orderDate"
                            )
                    )
            );
        }

        cursor.close();
        db.close();

        return order;
    }

    // =====================================================
    // CẬP NHẬT TRẠNG THÁI ĐƠN
    // =====================================================

    public int updateOrderStatus(
            int orderId,
            String status
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "status",
                status
        );

        int result =
                db.update(
                        TABLE_ORDER,
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(orderId)
                        }
                );

        db.close();

        return result;
    }

    // =====================================================
    // LẤY CHI TIẾT SẢN PHẨM TRONG ĐƠN
    // =====================================================

    public ArrayList<OrderItem> getOrderItems(
            int orderId
    ) {

        ArrayList<OrderItem> list =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                TABLE_ORDER_ITEM +
                                " WHERE orderId = ? " +
                                "ORDER BY id ASC",
                        new String[]{
                                String.valueOf(orderId)
                        }
                );

        if (cursor.moveToFirst()) {

            do {

                OrderItem item =
                        new OrderItem();

                item.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "id"
                                )
                        )
                );

                item.setOrderId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "orderId"
                                )
                        )
                );

                item.setProductId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "productId"
                                )
                        )
                );

                item.setProductName(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "productName"
                                )
                        )
                );

                item.setPrice(
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "price"
                                )
                        )
                );

                item.setQuantity(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "quantity"
                                )
                        )
                );

                list.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    // =====================================================
    // USER
    // =====================================================

    public long registerUser(
            String username,
            String password,
            String fullName,
            String phone,
            String address
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("username", username);
        values.put("password", password);
        values.put("fullName", fullName);
        values.put("phone", phone);
        values.put("address", address);
        values.put("role", "user");

        long result;

        try {

            result =
                    db.insertOrThrow(
                            TABLE_USER,
                            null,
                            values
                    );

        } catch (Exception e) {

            result = -1;
        }

        db.close();

        return result;
    }

    public User loginUser(
            String username,
            String password
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                TABLE_USER +
                                " WHERE username = ? " +
                                "AND password = ?",
                        new String[]{
                                username,
                                password
                        }
                );

        User user = null;

        if (cursor.moveToFirst()) {

            user =
                    cursorToUser(cursor);
        }

        cursor.close();
        db.close();

        return user;
    }

    public User getUser(
            String username
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                TABLE_USER +
                                " WHERE username = ?",
                        new String[]{
                                username
                        }
                );

        User user = null;

        if (cursor.moveToFirst()) {

            user =
                    cursorToUser(cursor);
        }

        cursor.close();
        db.close();

        return user;
    }

    private User cursorToUser(Cursor cursor) {

        return new User(
                cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                ),
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "username"
                        )
                ),
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "password"
                        )
                ),
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "fullName"
                        )
                ),
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "phone"
                        )
                ),
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "address"
                        )
                ),
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "role"
                        )
                )
        );
    }

    public int updateUser(
            String username,
            String fullName,
            String phone,
            String address
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("fullName", fullName);
        values.put("phone", phone);
        values.put("address", address);

        int result =
                db.update(
                        TABLE_USER,
                        values,
                        "username = ?",
                        new String[]{
                                username
                        }
                );

        db.close();

        return result;
    }
}