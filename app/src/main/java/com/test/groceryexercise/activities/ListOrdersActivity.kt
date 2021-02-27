package com.test.groceryexercise.activities


import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.OrderListAdapter
import com.test.groceryexercise.adapters.OrderTabAdapter
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.Order
import com.test.groceryexercise.models.OrderResponse
import kotlinx.android.synthetic.main.activity_list_orders.*
import kotlinx.android.synthetic.main.fragment_list_order.*

class ListOrdersActivity : ListingActivity(), OrderListAdapter.OnChangeOrder {
    override val contentResource: Int
        get() = R.layout.activity_list_orders

    override val showBackButton: Boolean
        get() = true

    override val showSearchBar = false

    lateinit var mPager: OrderTabAdapter

    var selectedOrder :Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }


    private fun getData(){
        val queue = Volley.newRequestQueue(this)
        val user = SessionManager(this).user
        if (user != null) {
            val request = StringRequest(
                Request.Method.GET,
                Endpoints.ordersByUserId(user._id),
                { responseString ->
                    val response = Gson().fromJson(responseString, OrderResponse::class.java)
                    val orders = response.data.map { it.cleanUpOrder(this) }
                    mPager = OrderTabAdapter(supportFragmentManager, orders)
                    pager_order.adapter= mPager
                    tabs_order.setupWithViewPager(pager_order)
                    Log.d("myApp", "Got ${response.data.size} orders")
                },
                {
                    Log.e("myApp", String(it.networkResponse.data))
                }
            )
            queue.add(request)
        }
    }
    private fun init() {
        getData()
    }

    override fun onChangeOrder(order: Order) {
        selectedOrder = order
        mPager.selectedOrder=order
        pager_order.currentItem=1
        mPager.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        //Go back from order detail to order list
        if(pager_order.currentItem==1){
            pager_order.currentItem=0
        }else{
            finish()
        }
    }
}