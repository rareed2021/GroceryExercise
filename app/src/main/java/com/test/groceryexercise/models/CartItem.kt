package com.test.groceryexercise.models

import java.io.Serializable

data class CartItem(
    val _id: String,
    val image: String,
    val mrp: Double,
    val price: Double,
    val productName: String,
    var amount:Int,
):Serializable{
    companion object{
        const val KEY = "CART_ITEM"
    }
}
