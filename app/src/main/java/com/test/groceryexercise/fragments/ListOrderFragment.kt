package com.test.groceryexercise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.OrderListAdapter
import com.test.groceryexercise.models.Order
import kotlinx.android.synthetic.main.fragment_list_order.view.*

private const val ARG_ORDERS = Order.KEY


/**
 * A simple [Fragment] subclass.
 * Use the [ListOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListOrderFragment : Fragment() {
    private var mOrders: Array<Order>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mOrders = it.getSerializable(ARG_ORDERS) as? Array<Order>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_order, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        val orders =mOrders
        val context = activity
        if(orders!=null && context!=null){
            view.recycler_orders.adapter = OrderListAdapter(context,orders.sortedByDescending {
                it.date
            })
            view.recycler_orders.layoutManager = LinearLayoutManager(context)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param orders Parameter 1.
         * @return A new instance of fragment ListOrderFragment.
         */
        @JvmStatic
        fun newInstance(orders: Array<Order>)=
            ListOrderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ORDERS, orders)
                }
            }
    }
}