package com.test.groceryexercise.models

data class CheckoutTotal(
    var orderAmount:Double = 0.0,
    var discount:Double = 0.0,
    var deliveryCharges:Double = 0.0,
    var totalAmount:Double = 0.0,
    var ourPrice:Double = 0.0,
    var _id:String? = null
)
