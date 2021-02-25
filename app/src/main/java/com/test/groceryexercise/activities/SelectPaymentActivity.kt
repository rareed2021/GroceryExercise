package com.test.groceryexercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.Address
import com.test.groceryexercise.models.Order
import com.test.groceryexercise.models.Payment
import kotlinx.android.synthetic.main.activity_select_payment.*
import org.json.JSONObject

class SelectPaymentActivity : AppCompatActivity() {
    lateinit var address:Address
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment)
        init()
    }

    private fun init() {
        val add = intent.getSerializableExtra(Address.KEY) as? Address
        if(add==null){
            val intent = Intent(this, AddressListActivity::class.java)
            intent.putExtra(Address.CHECKOUT_KEY,true)
            startActivity(intent)
        }else{
            address=add
            text_city.text = address.city
            text_house_number.text = address.houseNo
            text_pin.text = address.pincode.toString()
            text_streetname.text =address.streetName
            text_total.text = "\$"+DBHelper(this).cartCost.totalAmount.toString()
            button_cod.setOnClickListener {
                sendData()
            }
        }
    }

    private fun sendData() {
        val params = JSONObject(Gson().toJson(generateOrder(), Order::class.java))
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.orderPost,
            params,
            {
                DBHelper(this).clearCart()
                startActivity(Intent(this, OrderConfirmedActivity::class.java))
            },
            {
                Log.e("myApp", String(it.networkResponse.data))
            }
        )
        queue.add(request)
    }

    private fun generateOrder(): Order{
        val db = DBHelper(this)
        val user = SessionManager(this).user
        return Order(
            db.cartCost,
            Payment("cash","completed"),
            db.cartItems,
            address,
            user?._id?:"",
            user,
        )
    }
}