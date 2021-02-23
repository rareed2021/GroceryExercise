package com.test.groceryexercise.models

data class LoginResponse(
    val token: String?,
    val user: User?,
    val error:Boolean?,
    val message:String?
)