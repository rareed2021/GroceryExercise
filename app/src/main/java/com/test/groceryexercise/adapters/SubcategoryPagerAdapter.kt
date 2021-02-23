package com.test.groceryexercise.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.test.groceryexercise.fragments.ProductListFragment
import com.test.groceryexercise.models.SubCategory

class SubcategoryPagerAdapter(fm:FragmentManager, private val mData : List<SubCategory> = listOf()) : FragmentPagerAdapter(fm){
    private val fragments :List<Fragment> = mData.map{
        ProductListFragment.newInstance(it.subId)
    }
    override fun getCount(): Int {
        return mData.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        val k = mData[position]
        return k.subName
    }

    override fun getItem(position: Int): Fragment {
        val k  = mData.toList()[position]
        return fragments[position]
    }
}