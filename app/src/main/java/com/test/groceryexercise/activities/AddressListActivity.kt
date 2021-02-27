package com.test.groceryexercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.AddressAdapter
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.Address
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : ListingActivity() {
    private lateinit var adapter:AddressAdapter
    var toCheckout = false
    override val contentResource: Int
        get() = R.layout.activity_address_list


    override val showBackButton = true
    override val toolbarTitle ="My Address"
    override val showCartButton: Boolean
        get() = !toCheckout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
        toCheckout = intent.getBooleanExtra(Address.CHECKOUT_KEY,false)
        adapter = AddressAdapter(this,toCheckout)
        adapter.init(recycler_addresses)
        if(toCheckout){
            val user = SessionManager(this).user
            if(user==null){
                //if we're not logged in, go to login page.
                startActivity(Intent(this,LoginActivity::class.java))
            }
        }
    }

    private fun init() {
        toCheckout = intent.getBooleanExtra(Address.CHECKOUT_KEY,false)
        if(toCheckout){
            text_select_address.visibility= View.VISIBLE
        }
        button_add_address.setOnClickListener {
            startActivity(Intent(this,AddAddressActivity::class.java))
        }
    }

    override fun onRestart() {
        super.onRestart()
        adapter.updateData()
    }
}