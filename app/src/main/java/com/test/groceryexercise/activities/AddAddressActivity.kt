package com.test.groceryexercise.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.Address
import kotlinx.android.synthetic.main.activity_add_address.*
import org.json.JSONObject

class AddAddressActivity : ListingActivity() {
    override val contentResource: Int
        get() = R.layout.activity_add_address

    override val showBackButton: Boolean
        get() = true

    override val toolbarTitle
        get()= if(mAddress==null) "New Address" else "Edit Address"
    override val showSearchBar = false

    //this will be set if we are editing an address
    var mAddress :Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun showError(s:String){
        text_error.text = s
        text_error.visibility=View.VISIBLE
    }
    private fun init() {
        mAddress = intent.getSerializableExtra(Address.KEY) as? Address
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
                        city,
                        num,
                        pin,
                        street,
                        null,
                        null,
                        type,
                        user._id
                    )
                    if(mAddress==null) {
                        Log.d("myApp","Adding address")
                        sendAddAddress(address)
                    }else{
                        Log.d("myApp","Editing Address")
                        sendAddAddress(address
                            , Endpoints.addressesById(mAddress?._id ?:"")
                            , Request.Method.PUT
                        )
                    }
                    button_add_address.visibility=View.GONE
                    progress_add.visibility=View.VISIBLE
                }else{
                    showError("Please Sign In")
                }
            }else{
                showError(getString(R.string.fill_in_all))
            }
        }
        mAddress?.also {
            setEditUI(it)
        }
    }

    private fun setEditUI(address:Address) {
        button_add_address.text = getString(R.string.edit_address_button)
        edit_city.text.clear()
        edit_city.text.insert(0,address.city)
        edit_street_name.text.clear()
        edit_street_name.text.insert(0,address.streetName)
        edit_post_number.text.apply{clear();insert(0,address.houseNo)}
        edit_pin_number.text.apply{clear();insert(0,address.pincode.toString())}
        edit_type.text.apply{clear();insert(0,address.type)}
    }

    private fun sendAddAddress(address: Address, endpoint:String =Endpoints.addressPost, method:Int=Request.Method.POST) {
        val queue  = Volley.newRequestQueue(this)
        val params = JSONObject(Gson().toJson(address, Address::class.java))
        val request = JsonObjectRequest(
            method,
            endpoint,
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