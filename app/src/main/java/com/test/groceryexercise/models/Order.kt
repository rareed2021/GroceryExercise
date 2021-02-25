package com.test.groceryexercise.models

data class Order(
    val orderSummary: CheckoutTotal,
    val payment: Payment,
    val products: List<CartItem>,
    val shippingAddress: Address,
    val userId: String,
    val user: User?=null,
    val orderStatus: String?=null,
    val __v: Int?=null,
    val _id: String?=null,
    val date: String?=null,
)

data class OrderSummary(
    val deliveryCharges: Int,
    val discount: Int,
    val orderAmount: Int,
    val ourPrice: Int,
    val totalAmount: Int,
    val _id: String?,
)

data class Payment(
    val paymentMode: String,//cash
    val paymentStatus: String,//completed
    val _id: String?=null,
)


data class ShippingAddress(
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String
)


data class OrderResponse(
    val error:Boolean,
    val count:Int,
    val data: List<Order>
)