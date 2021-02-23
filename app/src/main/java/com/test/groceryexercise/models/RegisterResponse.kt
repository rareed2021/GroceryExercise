package com.test.groceryexercise.models

data class RegisterResponse(
    val data: User?,
    val error: Boolean,
    val message: String?
)
