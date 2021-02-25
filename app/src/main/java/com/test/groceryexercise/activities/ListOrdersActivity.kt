package com.test.groceryexercise.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.OrderListAdapter
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.OrderResponse
import kotlinx.android.synthetic.main.activity_list_orders.*

class ListOrdersActivity : ListingActivity() {
    override val contentResource: Int
        get() = R.layout.activity_list_orders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        getData()
    }

    private fun getData() {
        val queue  = Volley.newRequestQueue(this)
        val user = SessionManager(this).user
        if(user!=null) {
            val request = StringRequest(
                Request.Method.GET,
                Endpoints.ordersByUserId(user._id),
                {
                    val response = Gson().fromJson(it,OrderResponse::class.java)
                    recycler_orders.adapter = OrderListAdapter(this,response.data)
                    recycler_orders.layoutManager = LinearLayoutManager(this)
                    Log.d("myApp","Got ${response.data.size} orders")
                },
                {
                    Log.e("myApp",String(it.networkResponse.data))
                }
            )
            queue.add(request)
        }
    }
}