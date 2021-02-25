package com.test.groceryexercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.groceryexercise.R
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.models.Address
import kotlinx.android.synthetic.main.activity_select_payment.*

class SelectPaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment)
        init()
    }

    private fun init() {
        val address = intent.getSerializableExtra(Address.KEY) as? Address
        if(address==null){
            val intent = Intent(this, AddressListActivity::class.java)
            intent.putExtra(Address.CHECKOUT_KEY,true)
            startActivity(intent)
        }else{
            text_city.text = address.city
            text_house_number.text = address.houseNo
            text_pin.text = address.pincode.toString()
            text_streetname.text =address.streetName
            text_total.text = "\$"+DBHelper(this).cartCost.total.toString()

        }
    }
}