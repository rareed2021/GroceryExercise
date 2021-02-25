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
    fun fieldString(field:String):String{
        return when(field){
            "email"->email
            "firstName"->firstName
            "mobile"->mobile
            else -> ""
        }
    }
    fun setField(field:String, value:String):User{
        return when(field){
            "email"->copy(email=value)
            "firstName"->copy(firstName=value)
            "mobile"->copy(mobile=value)
            else -> this
        }
    }
}