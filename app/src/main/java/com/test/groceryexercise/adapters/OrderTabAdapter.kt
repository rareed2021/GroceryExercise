package com.test.groceryexercise.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.test.groceryexercise.fragments.DetailOrderFragment
import com.test.groceryexercise.fragments.ListOrderFragment
import com.test.groceryexercise.models.Order

class OrderTabAdapter(fm:FragmentManager, orders:List<Order>) : FragmentStatePagerAdapter(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
  /*  private val mData = mutableMapOf<Int, Fragment>()
    private val mPositions = mutableMapOf<Order,Int>()
    var mCount :Int = 2*/
    var selectedOrder : Order? = null
        get() = field
        set(value) {
            val oldOrder = field
            field = value
            tryReplaceFragment()
            notifyDataSetChanged()
        }

    private fun tryReplaceFragment() {
        val nextFrag = detailFragment
        nextFrag?.order = selectedOrder
    }

    private var listFragment = ListOrderFragment.newInstance(orders.toTypedArray())
    private var selectedFragment :DetailOrderFragment? = null

    override fun getCount(): Int =
        if(selectedOrder!=null) 2 else 1

    private val detailFragment:DetailOrderFragment?
    get() {
        val order = selectedOrder
        return selectedFragment
            ?: if (order != null) {
                DetailOrderFragment.newInstance(order)
            } else {
                null
            }
    }


    override fun getItemPosition(`object`: Any): Int {
        return when(`object`){
            is ListOrderFragment -> 0
            is DetailOrderFragment ->1
            else -> POSITION_NONE
        }
    }


    override fun getItem(position: Int): Fragment {
        Log.d("myApp","Getting fragment")
        return when (position) {
            0 -> listFragment
            1 -> detailFragment as Fragment
            else -> detailFragment as Fragment
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0->"Orders"
            1->"Detail"
            else->"Orders"
        }
    }
}