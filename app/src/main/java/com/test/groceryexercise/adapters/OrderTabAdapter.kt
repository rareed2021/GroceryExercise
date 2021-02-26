package com.test.groceryexercise.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.test.groceryexercise.fragments.DetailOrderFragment
import com.test.groceryexercise.fragments.ListOrderFragment
import com.test.groceryexercise.models.Order

class OrderTabAdapter(fm:FragmentManager, orders:List<Order>) : FragmentPagerAdapter(fm) {
    private val mData = mutableMapOf<Order, Fragment>()
    var selectedOrder : Order? = null
        get() = field
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var listFragment = ListOrderFragment.newInstance(orders.toTypedArray())

    override fun getCount(): Int =
        if(selectedOrder!=null) 2 else 1

    private val detailFragment:Fragment
    get() {
        val order = selectedOrder
        return if(order!=null){
            val frag = mData[order]
            if(frag!=null){
                frag
            }else{
                val newFrag = DetailOrderFragment.newInstance(order)
                mData[order] = newFrag
                newFrag
            }
        }else{
            listFragment
        }
    }
    override fun getItem(position: Int): Fragment =
        when(position){
            0 -> listFragment
            1 -> detailFragment
            else -> detailFragment
        }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0->"Orders"
            1->"Detail"
            else->"Orders"
        }
    }
}