package com.test.groceryexercise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.groceryexercise.R
import com.test.groceryexercise.models.Product
import kotlinx.android.synthetic.main.row_search_result.view.*

class SearchResultAdapter(private val context: Context) :RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
    private val mData = mutableListOf<Product>()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: Product) {
            itemView.text_name.text =product.productName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_search_result,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setData(vals:Array<Product>){
        mData.clear()
        mData.addAll(vals)
        notifyDataSetChanged()
    }
}