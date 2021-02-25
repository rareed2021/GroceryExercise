package com.test.groceryexercise.models

data class CheckoutTotal(
    var subtotal:Double = 0.0,
    var discount:Double = 0.0,
    var delivery:Double = 0.0,
    var total:Double = 0.0,
    var ourPrice:Double = 0.0,
    var id:String? = null
)
