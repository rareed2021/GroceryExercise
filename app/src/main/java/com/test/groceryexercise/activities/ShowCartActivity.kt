package com.test.groceryexercise.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.CartListAdapter
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.models.CheckoutTotal
import kotlinx.android.synthetic.main.activity_show_cart.*

class ShowCartActivity : AppCompatActivity(), CartListAdapter.OnUpdateTotals {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_cart)
        init()
    }

    private fun init() {
        val db = DBHelper(this)
        val items = db.cartItems
        recycler_cart_items.adapter = CartListAdapter(this,items)
        recycler_cart_items.layoutManager = LinearLayoutManager(this)
    }

    override fun updateTotals(totals: CheckoutTotal) {
        text_discount.text = String.format("%.2f", totals.discount)
        text_subtotal.text = String.format("%.2f", totals.subtotal)
        text_total.text = String.format("%.2f", totals.total)
    }
}