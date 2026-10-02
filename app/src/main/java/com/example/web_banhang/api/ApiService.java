package com.example.web_banhang.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterResponse> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("fullName") String fullName,
            @Field("phone") String phone,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("user_info.php")
    Call<UserInfoResponse> getUserInfo(
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("update_user.php")
    Call<UpdateUserResponse> updateUser(
            @Field("user_id") int userId,
            @Field("fullName") String fullName,
            @Field("phone") String phone,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("delete_user.php")
    Call<DeleteUserResponse> deleteUser(
            @Field("user_id") int userId
    );

    @GET("get_products.php")
    Call<ProductResponse> getProducts();

    @GET("get_products.php")
    Call<ProductResponse> getProduct(
            @Query("id") int productId
    );

    @FormUrlEncoded
    @POST("add_product.php")
    Call<BasicResponse> addProduct(
            @Field("productCode") String productCode,
            @Field("Ten") String name,
            @Field("Gia") double price,
            @Field("description") String description,
            @Field("SL") int stock,
            @Field("Anh") String image,
            @Field("saleDate") String saleDate,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("update_product.php")
    Call<BasicResponse> updateProduct(
            @Field("id") int productId,
            @Field("productCode") String productCode,
            @Field("Ten") String name,
            @Field("Gia") double price,
            @Field("description") String description,
            @Field("SL") int stock,
            @Field("Anh") String image,
            @Field("saleDate") String saleDate,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("delete_product.php")
    Call<BasicResponse> deleteProduct(
            @Field("id") int productId
    );

    @FormUrlEncoded
    @POST("add_cart.php")
    Call<BasicResponse> addCart(
            @Field("user_id") int userId,
            @Field("product_id") int productId,
            @Field("quantity") int quantity
    );

    @FormUrlEncoded
    @POST("get_cart.php")
    Call<CartResponse> getCart(
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("update_cart.php")
    Call<BasicResponse> updateCart(
            @Field("user_id") int userId,
            @Field("product_id") int productId,
            @Field("quantity") int quantity
    );

    @FormUrlEncoded
    @POST("delete_cart.php")
    Call<BasicResponse> deleteCart(
            @Field("user_id") int userId,
            @Field("product_id") int productId
    );

    @FormUrlEncoded
    @POST("create_order.php")
    Call<CreateOrderResponse> createOrder(
            @Field("user_id") int userId,
            @Field("customerName") String customerName,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("paymentMethod") String paymentMethod
    );

    @FormUrlEncoded
    @POST("get_orders.php")
    Call<OrderResponse> getOrders(
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("get_order_detail.php")
    Call<OrderDetailResponse> getOrderDetail(
            @Field("user_id") int userId,
            @Field("order_id") int orderId
    );

    @FormUrlEncoded
    @POST("cancel_order.php")
    Call<BasicResponse> cancelOrder(
            @Field("user_id") int userId,
            @Field("order_id") int orderId
    );

    @FormUrlEncoded
    @POST("admin_get_orders.php")
    Call<OrderResponse> adminGetOrders(
            @Field("user_id") int adminId
    );

    @FormUrlEncoded
    @POST("admin_get_order_detail.php")
    Call<OrderDetailResponse> adminGetOrderDetail(
            @Field("user_id") int adminId,
            @Field("order_id") int orderId
    );

    @FormUrlEncoded
    @POST("update_order_status.php")
    Call<BasicResponse> updateOrderStatus(
            @Field("user_id") int adminId,
            @Field("order_id") int orderId,
            @Field("status") String status
    );
}

