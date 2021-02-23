package com.test.groceryexercise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.ProductListAdapter
import com.test.groceryexercise.models.Product
import com.test.groceryexercise.models.ProductResponse
import com.test.groceryexercise.models.SubCategory
import kotlinx.android.synthetic.main.fragment_product_list.view.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = SubCategory.KEY

/**
 * A simple [Fragment] subclass.
 * Use the [ProductListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductListFragment : Fragment() {
    private var mData: Array<Product>? = null
    private var subcatId : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subcatId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_product_list, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        val subId = subcatId
        if(subId!=null) {
            view.recycler_subcategory.adapter = ProductListAdapter(activity!!, subId)
            view.recycler_subcategory.layoutManager = LinearLayoutManager(context)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param subcatId ID of Subcategory.
         * @return A new instance of fragment ProductListFragment.
         */
        @JvmStatic
        fun newInstance(subcatId:Int) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, subcatId)
                }
            }
    }
}