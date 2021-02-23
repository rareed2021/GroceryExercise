package com.test.groceryexercise.app

import Category
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.test.groceryexercise.activities.CatListingActivity
import com.test.groceryexercise.activities.ProductDetailActivity
import com.test.groceryexercise.models.Product
import com.test.groceryexercise.models.SubCategory
import com.test.groceryexercise.models.User
import java.util.*

class SessionManager(private val context: Context) {
    private val preference = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE)
    private val editor = preference.edit()


    val isLoggedIn:Boolean
        get() = preference.getString(KEY_USER,null) !=null
    var user: User?
        get(){
            val userString = preference.getString(KEY_USER,null)
            return if(userString!=null){
                Gson().fromJson(userString,User::class.java)
            }else{
                null
            }
        }
        set(value){
            if(value!=null) {
                val stringRep = Gson().toJson(value, User::class.java)
                editor.putString(KEY_USER, stringRep)
            }else{
                editor.remove(KEY_USER)
            }
            editor.apply()
        }
    val hasActive:Boolean
        get()=preference.contains(KEY_ACTIVE) && preference.contains((KEY_ACTIVE_TYPE))

    fun clearActive(){
        editor.remove(KEY_ACTIVE_TYPE)
            .remove(KEY_ACTIVE)
            .apply()
    }
    fun setActive(product: Product){
        val prodString = Gson().toJson(product, Product::class.java)
        setActive(prodString, KEY_PRODUCT)
    }
    fun setActive(subCat:Category){
        val subString = Gson().toJson(subCat, Category::class.java)
        setActive(subString, KEY_CATEGORY)
    }
    private fun setActive(s:String, k:String){
        editor.putString(KEY_ACTIVE,s)
            .putString(KEY_ACTIVE_TYPE,k)
            .apply()
    }
    fun loadActive():Boolean{
        val actType = preference.getString(KEY_ACTIVE_TYPE,null)
        val actString = preference.getString(KEY_ACTIVE,null)
        Log.d("myApp","Type: $actType, Data: $actString")
        if(actType!=null && actString!=null){
            val c = when(actType){
                KEY_PRODUCT -> Product::class.java
                KEY_CATEGORY -> Category::class.java
                else -> null
            }
            when(val o = Gson().fromJson(actString,c)){
                is Product ->{
                    val intent = Intent(context,ProductDetailActivity::class.java)
                    intent.putExtra(Product.KEY, o)
                    context.startActivity(intent)
                    return true
                }
                is Category ->{
                    val intent = Intent(context,CatListingActivity::class.java)
                    intent.putExtra(Category.KEY,o)
                    context.startActivity(intent)
                    return true
                }
            }
        }
        return false
    }

    private companion object{
        const val FILE_NAME = "user_state"
        const val KEY_USER = "USER"
        const val KEY_ACTIVE_TYPE = "ACTIVE_TYPE"
        const val KEY_ACTIVE = "ACTIVE"

        const val KEY_PRODUCT = "PRODUCT"
        const val KEY_CATEGORY = "CAT"
    }
}

