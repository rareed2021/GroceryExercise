package com.test.groceryexercise.models

import java.io.Serializable

data class Address(
    val __v: Int?,
    val _id: String?,
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String,
    val type: String,
    val userId: String
):Serializable{
    companion object{
        const val CHECKOUT_KEY = "checkout_after"
        const val KEY = "ADDRESS"
    }
}



data class AddressResponse(
    val count: Int,
    val data: List<Address>,
    val error: Boolean
)