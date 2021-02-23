package com.test.groceryexercise.models

import java.io.Serializable

data class SubCategoryResponse(
    val count: Int,
    val data: List<SubCategory>,
    val error: Boolean
)

data class SubCategory(
    val __v: Int,
    val _id: String,
    val catId: Int,
    val position: Int,
    val status: Boolean,
    val subDescription: String,
    val subId: Int,
    val subImage: String,
    val subName: String
) : Serializable{
    companion object{
        fun getSubcategoryListingUrl(id:Int) = "https://grocery-second-app.herokuapp.com/api/subcategory/$id"
        const val KEY = "SUBCATEGORY"
    }
}