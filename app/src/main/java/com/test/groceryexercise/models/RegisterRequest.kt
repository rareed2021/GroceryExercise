package com.test.groceryexercise.models

data class RegisterRequest(
    var firstName:String,
    var email:String,
    var password:String,
    var mobile:String
)
