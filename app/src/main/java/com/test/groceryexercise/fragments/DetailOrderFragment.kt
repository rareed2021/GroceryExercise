package com.test.groceryexercise.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.OrderProductAdapter
import com.test.groceryexercise.models.Order
import kotlinx.android.synthetic.main.fragment_detail_order.view.*
import java.time.LocalDate

private const val ARG_ORDER = Order.KEY

/**
 * A simple [Fragment] subclass.
 * Use the [DetailOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailOrderFragment : Fragment() {
    private var mOrder: Order? = null

    private var mView : View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("myApp","Creating fragment")
        arguments?.let {
            mOrder = it.getSerializable(ARG_ORDER) as? Order
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_order, container, false)
        Log.d("myApp","Creating order view")
        mView = view
        //init(view)
        return view
    }

    var order :Order?
        get() = mOrder
        set(value) {
            mOrder = value
            Log.d("myApp","changed order $mOrder")
            if(value!=null) {
                arguments?.putSerializable(ARG_ORDER, value)
            }
        }


    override fun onResume() {
        super.onResume()
        Log.d("myApp","I got called after resume")
        arguments?.let {
            mOrder = it.getSerializable(ARG_ORDER) as? Order
        }
        Log.d("myApp","with mOrder: $mOrder")
        val v = mView ?: this.view
        if(v!=null) {
            init(v)
        }
    }

    private fun init(view: View) {
        val order = mOrder
        if(order!=null) {
            Log.d("myApp","Drawing order")
            //shipping address
            view.text_streetname.text = order.shippingAddress.streetName
            view.text_house_number.text = order.shippingAddress.houseNo
            view.text_city.text = order.shippingAddress.city
            view.text_pincode.text = order.shippingAddress.pincode.toString()
            //order info
            view.text_price.text = "\$${order.orderSummary?.totalAmount ?:0}"
            view.text_saved.text = "You Saved \$${order.orderSummary?.discount?:0}"
            val dateString = order.date
            if(dateString!=null) {
                val date = LocalDate.parse(dateString.split("T")[0])
                view.text_date.text = "Ordered on ${date.dayOfMonth}/${date.monthValue}/${date.year}"
            }
            val context  = activity
            if(context !=null) {
                view.recycler_items.adapter = OrderProductAdapter(context, order)
                view.recycler_items.layoutManager=LinearLayoutManager(context)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param order Parameter 1.
         * @return A new instance of fragment DetailOrderFragment.
         */

        @JvmStatic
        fun newInstance(order: Order)=
            DetailOrderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ORDER, order)
                }
            }
    }
}