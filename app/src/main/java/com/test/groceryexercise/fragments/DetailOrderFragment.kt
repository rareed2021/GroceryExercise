package com.test.groceryexercise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.groceryexercise.R
import com.test.groceryexercise.models.Order

private const val ARG_ORDER = Order.KEY

/**
 * A simple [Fragment] subclass.
 * Use the [DetailOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailOrderFragment : Fragment() {
    private var mOrder: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mOrder = it.getSerializable(ARG_ORDER) as? Order
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_order, container, false)
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