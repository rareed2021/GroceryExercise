package com.test.groceryexercise.app

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.activities.ListingActivity
import com.test.groceryexercise.models.AddressResponse
import com.test.groceryexercise.models.User
import org.json.JSONObject

class ApiHelper(private val context: Context) {
    fun updateUser(newuser : User){
        val session =SessionManager(context)
        val user = session.user
        if(user!=null){
            val queue = Volley.newRequestQueue(context)
            val params = JSONObject()
            if(user.email!=newuser.email){
                params.put("email",newuser.email)
            }
            if(user.mobile!=newuser.mobile){
                params.put("mobile",newuser.mobile)
            }
            if(user.firstName!=newuser.firstName){
                params.put("firstName",newuser.firstName)
            }
            Log.d("myApp",user._id)
            Log.d("myApp",Endpoints.putUserById(user._id?:""))
            val request = JsonObjectRequest(
                Request.Method.POST,
                Endpoints.putUserById(user._id?:""),
                params,
                {
                    session.user = newuser
                    if(context is ListingActivity)
                        context.updateHeader()
                    Log.d("myApp",it.toString(2))
                },
                {
                    Log.e("myApp",String(it.networkResponse.data))
                }
            )
            queue.add(request)
        }
    }
}