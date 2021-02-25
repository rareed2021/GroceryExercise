package com.test.groceryexercise.adapters

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.databinding.PlusMinusButtonBinding
import com.test.groceryexercise.models.CartItem
import com.test.groceryexercise.models.Product
import kotlinx.android.synthetic.main.plus_minus_button.view.*

class AmountButtonAdapter(val context: Context, val itemId:String, alwaysShow:Boolean=false) {
    var mProduct : Product? =null
    var mItem :CartItem? = null
    var initialItem :CartItem? = null
    var db :DBHelper = DBHelper(context)
    var showFull = alwaysShow
    var mBinding : PlusMinusButtonBinding?=null
    var mView :View? = null
    private var handler : ((Int)->Unit)? = null
    constructor(context:Context, product: Product) : this(context, product._id){
        mProduct = product
    }
    constructor(context:Context, item:CartItem,alwaysShow:Boolean=true) : this(context, item._id,alwaysShow){
        mItem=item
        initialItem =item
    }



    fun setupAddButton(button:TextView){
        button.setOnClickListener {
            showFull = true
            addToCartIfNot()
            Log.d("myApp", "Adding to cart.  Product:$mProduct")
            setupVisible()
            handler?.invoke(1)
        }
    }


    fun setOnAmountChangedListener(listener : (Int)->Unit){
        handler = listener
    }




    fun init(view:View){
        val product = mProduct
        mView = view
        if(mItem==null && product!=null){
            mItem = db.getCartItem(product._id)
        }
        setupVisible(view.layout_full, view.text_amount, view.button_add)
        setupAddButton(view.button_add)
        setupMinusButton(view.button_minus)
        setupPlusButton(view.button_plus)

    }
    fun init(binding:PlusMinusButtonBinding){
        mBinding = binding
        val product = mProduct
        if(mItem==null && product!=null){
            mItem = db.getCartItem(product._id)
        }
        setupVisible()
        setupAddButton(binding.buttonAdd)
        setupMinusButton(binding.buttonMinus)
        setupPlusButton(binding.buttonPlus)
    }


    private fun setupPlusButton(buttonPlus: TextView) {
        buttonPlus.setOnClickListener {
            val item = mItem
            val id = itemId
            Log.d("myApp","Item is $mItem")
            if (item != null) {
                val check = db.getCartItem(id)
                Log.d("myApp", "Doing check $check")
                if(check==null){
                    db.addToCart(item)
                    val check2 = db.getCartItem(id)
                    Log.d("myApp", "Doing check again $check2")
                }
                plusItem(item)
            } else {
                val i = db.getCartItem(id)
                Log.d("myApp", "getting cart item $i")
                if (i != null) {
                    mItem = i
                    plusItem(i)
                } else {
                    val product = mProduct
                    if (product != null) {
                        db.addToCart(product, 1)
                        mItem = db.getCartItem(product._id)
                    }
                }
            }
            setupVisible()
            handler?.invoke(mItem?.quantity ?: 0)
        }
    }

    private fun setupMinusButton(buttonMinus: TextView) {
        buttonMinus.setOnClickListener {
            val item = mItem
            if (item != null) {
                if (item.quantity > 0) {
                    item.quantity -= 1
                    if (item.quantity > 0) {
                        db.updateItemAmount(item)
                    } else {
                        db.removeFromCart(item._id)
                    }
                }
            }
            setupVisible()
            handler?.invoke(mItem?.quantity ?: 0)
        }
    }


    private fun addToCartIfNot(){
        val item = mItem
        val product = mProduct
        if(item==null && product!=null){
            db.addToCart(product,1)
            mItem = db.getCartItem(product._id)
        }
    }
    private fun plusItem(item:CartItem){
        item.quantity+=1
        db.updateItemAmount(item)
    }

    private fun setupVisible(layout:View, amount:TextView, button:View){
        val id = itemId
        val item = db.getCartItem(id)
        if(item!=null){
            mItem = item
        }
        if((mItem?.quantity?:0)>0){
            showFull=true
        }
        if(showFull){
            layout.visibility=View.VISIBLE
            button.visibility=View.GONE
            amount.text=(mItem?.quantity?:0).toString()
        }else{
            button.visibility=View.VISIBLE
            layout.visibility=View.GONE
        }
    }
    private fun setupVisible(){
        Log.d("myApp","Setting up ${mItem?.quantity}")
        val bind = mBinding
        val view=mView
        if(bind !=null){
            Log.d("myApp","Binding fragment")
            setupVisible(bind.layoutFull, bind.textAmount, bind.buttonAdd)
        }else if (view!=null){
            setupVisible(view.layout_full, view.text_amount, view.button_add)
        }
    }
}