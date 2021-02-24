package com.test.groceryexercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.RenderProcessGoneDetail
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.Address
import kotlinx.android.synthetic.main.activity_add_address.*
import org.json.JSONObject

class AddAddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        init()
    }

    private fun showError(s:String){
        text_error.text = s
        text_error.visibility=View.VISIBLE
    }
    private fun init() {
        button_add_address.setOnClickListener {
            val user = SessionManager(this).user
            text_error.visibility= View.GONE
            val num = edit_post_number.text.toString()
            val street = edit_street_name.text.toString()
            val city= edit_city.text.toString()
            val pin = edit_pin_number.text.toString().toIntOrNull()
            val type = edit_type.text.toString()
            if(num != "" && street!="" && city!="" && pin!=null){
                if(user!=null) {
                    val address = Address(
                        null,
                        null,
                        city,
                        num,
                        pin,
                        street,
                        type,
                        user._id
                    )
                    sendData(address)
                    button_add_address.visibility=View.GONE
                    progress_add.visibility=View.VISIBLE
                }else{
                    showError("Please Sign In")
                }
            }else{
                showError(getString(R.string.fill_in_all))
            }
        }
    }
    private fun sendData(address: Address) {
        val queue  = Volley.newRequestQueue(this)
        val params = JSONObject(Gson().toJson(address, Address::class.java))
        val request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.addressPost,
            params,
            {
                //all done here. Will refresh from api when we go back
                finish()
            },
            {
                Log.e("myApp", it.toString())
                Log.e("myApp",String(it.networkResponse.data))
                button_add_address.visibility=View.VISIBLE
                progress_add.visibility=View.GONE
                showError("Error Uploading Address")
            }
        )
        queue.add(request)
    }
}