package com.test.groceryexercise.adapters

import android.content.Context
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.databinding.FragmentAddButtonBinding
import com.test.groceryexercise.databinding.PlusMinusButtonBinding
import com.test.groceryexercise.databinding.RowCartProductBinding
import com.test.groceryexercise.fragments.AddButtonFragment
import com.test.groceryexercise.models.CartItem
import com.test.groceryexercise.models.Product
import kotlinx.android.synthetic.main.fragment_add_button.view.*
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



    fun setupAddButton(button:Button){
        button.setOnClickListener {
            showFull = true
            addToCartIfNot()
            Log.d("myApp", "Adding to cart.  Product:$mProduct")
            setupVisible()
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
                if(check==null){
                    db.addToCart(item)
                }
                plusItem(item)
            } else {
                val i = db.getCartItem(id)
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
            handler?.invoke(mItem?.amount ?: 0)
        }
    }

    private fun setupMinusButton(buttonMinus: TextView) {
        buttonMinus.setOnClickListener {
            val item = mItem
            if (item != null) {
                if (item.amount > 0) {
                    item.amount -= 1
                    if (item.amount > 0) {
                        db.updateItemAmount(item)
                    } else {
                        db.removeFromCart(item._id)
                    }
                }
            }
            setupVisible()
            handler?.invoke(mItem?.amount ?: 0)
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
        item.amount+=1
        db.updateItemAmount(item)
    }

    private fun setupVisible(layout:View, amount:TextView, button:View){
        val id = itemId
        val item = db.getCartItem(id)
        if(item!=null){
            mItem = item
        }
        if((mItem?.amount?:0)>0){
            showFull=true
        }
        if(showFull){
            layout.visibility=View.VISIBLE
            button.visibility=View.GONE
            amount.text=(mItem?.amount?:0).toString()
        }else{
            button.visibility=View.VISIBLE
            layout.visibility=View.GONE
        }
    }
    private fun setupVisible(){
        Log.d("myApp","Setting up ${mItem?.amount}")
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