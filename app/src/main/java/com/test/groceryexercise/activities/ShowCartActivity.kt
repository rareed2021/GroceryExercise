package com.test.groceryexercise.activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.CartListAdapter
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.models.Address
import com.test.groceryexercise.models.CheckoutTotal
import kotlinx.android.synthetic.main.activity_show_cart.*

class ShowCartActivity : ListingActivity(), CartListAdapter.OnUpdateTotals {
    override val contentResource: Int
        get() = R.layout.activity_show_cart
    override val showCartButton: Boolean
        get() = false
    override val showBackButton: Boolean
        get() = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val db = DBHelper(this)
        val items = db.cartItems
        CartListAdapter(this,items).init(recycler_cart_items)
        recycler_cart_items.adapter = CartListAdapter(this,items)
        recycler_cart_items.layoutManager = LinearLayoutManager(this)
        button_checkout.setOnClickListener {
            val intent = Intent(this, AddressListActivity::class.java)
            intent.putExtra(Address.CHECKOUT_KEY, true)
            startActivity(intent)
        }
    }

    override fun updateTotals(totals: CheckoutTotal) {
        text_discount.text = String.format("%.2f", totals.discount)
        text_subtotal.text = String.format("%.2f", totals.orderAmount)
        text_total.text = String.format("%.2f", totals.totalAmount)
    }
}