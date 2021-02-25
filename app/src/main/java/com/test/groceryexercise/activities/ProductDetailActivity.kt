package com.test.groceryexercise.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.AmountButtonAdapter
import com.test.groceryexercise.app.Config
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.fragments.AddButtonFragment
import com.test.groceryexercise.models.AmountButtonSettings
import com.test.groceryexercise.models.Product
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_product_detail.text_price
class ProductDetailActivity : ListingActivity() {
    lateinit var mProduct : Product
    override val contentResource: Int
        get() = R.layout.activity_product_detail
    override val showBackButton: Boolean
        get() = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_product_detail)
        mProduct = intent.getSerializableExtra(Product.KEY) as Product
        init()
    }

    override fun setupToolbar(bar: Toolbar) {
        bar.title = mProduct.productName
        super.setupToolbar(bar)
    }

    private fun init() {
        val session = SessionManager(this)
        session.setActive(mProduct)
        text_product_name.text = mProduct.productName
        text_price.text = "$"+mProduct.price
        text_product_description.text = mProduct.description
        Picasso
            .get()
            .load(Config.IMAGE_BASE+mProduct.image)
            .into(image_product)
        Log.d("myApp","About to add fragment $mProduct")
        val adapter = AmountButtonAdapter(this, mProduct, AmountButtonSettings(verboseAmount = true))
        adapter.setOnAmountChangedListener {
            updateCartCount()
        }
        adapter.init(fragment_add)
        /*supportFragmentManager.beginTransaction()
            .add(R.id.fragment_add, AddButtonFragment.newInstance(mProduct,false))
            .commit()*/
    }
}