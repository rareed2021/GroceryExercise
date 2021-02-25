package com.test.groceryexercise.app

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.models.AddressResponse
import com.test.groceryexercise.models.User
import org.json.JSONObject

class ApiHelper(private val context: Context) {
    fun updateUser(){
        val user = SessionManager(context).user
        if(user!=null){
            val queue = Volley.newRequestQueue(context)
            val params = JSONObject(Gson().toJson(user, User::class.java))
            val request = JsonObjectRequest(
                Request.Method.PUT,
                Endpoints.putUserById(user._id),
                params,
                {
                    Log.d("myApp","User Updated")
                },
                {
                    Log.e("myApp",String(it.networkResponse.data))
                }
            )
            queue.add(request)
        }
    }
}