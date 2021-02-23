package com.test.groceryexercise.models

import java.io.Serializable

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val created:String?,
    val email: String,
    val firstName: String,
    val mobile: String,
    val password: String
):Serializable{
    companion object{
        const val KEY = "USER"
    }
}