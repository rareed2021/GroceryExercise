package com.test.groceryexercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.AddressAdapter
import com.test.groceryexercise.models.Address
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : AppCompatActivity() {
    private lateinit var adapter:AddressAdapter
    var toCheckout = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        init()
    }

    private fun init() {
        toCheckout = intent.getBooleanExtra(Address.CHECKOUT_KEY,false)
        adapter = AddressAdapter(this,toCheckout)
        recycler_addresses.adapter = adapter
        recycler_addresses.layoutManager = LinearLayoutManager(this)
        button_add_address.setOnClickListener {
            startActivity(Intent(this,AddAddressActivity::class.java))
        }
    }

    override fun onRestart() {
        super.onRestart()
        adapter.updateData()
    }
}