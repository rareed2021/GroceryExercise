package com.test.groceryexercise.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.AmountButtonAdapter
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.databinding.PlusMinusButtonBinding
import com.test.groceryexercise.models.AmountButtonSettings
import com.test.groceryexercise.models.CartItem
import com.test.groceryexercise.models.Product
import kotlinx.android.synthetic.main.fragment_add_button.view.*
import org.json.JSONObject

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ITEM_ID_PARAM = "ITEM_ID"
private const val ITEM_PRODUCT_PARAM = Product.KEY
private const val ALWAYS_SHOW_PARAM = "ALWAYS_SHOW"

/**
 * A simple [Fragment] subclass.
 * Use the [AddButtonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddButtonFragment : Fragment() {
    private var mId:String? = null
    private var mItem: CartItem? = null
    private var mProduct:Product? = null
    private var showFull  = false
    private var db :DBHelper?=null
    private var adapter :AmountButtonAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mProduct = it.getSerializable(ITEM_PRODUCT_PARAM) as? Product?
            mId = it.getString(ITEM_ID_PARAM, mProduct?._id ?:"")
            showFull = it.getBoolean(ALWAYS_SHOW_PARAM,false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_button, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        val context= activity
        if(context!=null){
            val product =mProduct
            if(product!=null) {
                adapter = AmountButtonAdapter(context,product)
            }else{
                val id = mId
                if(id!=null){
                    adapter = AmountButtonAdapter(context, id,  AmountButtonSettings(alwaysShow= showFull))
                }
            }
            Log.d("myApp","View items ${view.button_items}")
            //val binding = PlusMinusButtonBinding.bind(view.button_items)
            adapter?.init(view.button_items)
        }
    }

   /* private fun addToCartIfNot(){
        val item = mItem
        val product = mProduct
        if(item==null && product!=null){
            db?.addToCart(product,1)
            mItem = db?.getCartItem(product._id)
        }
    }
    private fun plusItem(item:CartItem){
        item.quantity+=1
        db?.updateItemAmount(item)
    }
    private fun setupVisible(view:View){
        val id = mId
        if(id!=null) {
            mItem = db?.getCartItem(id)
        }
        if((mItem?.quantity?:0)>0){
            showFull=true
        }
        if(showFull){
            view.layout_full.visibility=View.VISIBLE
            view.button_add.visibility=View.GONE
            view.text_amount.text=(mItem?.quantity?:0).toString()
        }else{
            view.button_add.visibility=View.VISIBLE
            view.layout_full.visibility=View.GONE
        }
    }*/



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param id product id
         * @param alwaysShow Whether cart item should be deleted when value is zero.
         * @return A new instance of fragment AddButtonFragment.
         */
        @JvmStatic
        fun newInstance(id: String, alwaysShow:Boolean=true) =
            AddButtonFragment().apply {
                arguments = Bundle().apply {
                    putString(ITEM_ID_PARAM, id)
                    putBoolean(ALWAYS_SHOW_PARAM,alwaysShow)
                }
            }
        @JvmStatic
        fun newInstance(product: Product, alwaysShow:Boolean = false) =
            AddButtonFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ITEM_PRODUCT_PARAM, product)
                    putBoolean(ALWAYS_SHOW_PARAM,alwaysShow)
                }
            }
    }
}