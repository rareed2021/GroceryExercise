package com.test.groceryexercise.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.groceryexercise.R
import com.test.groceryexercise.activities.ProductDetailActivity
import com.test.groceryexercise.app.Config
import com.test.groceryexercise.models.Product
import kotlinx.android.synthetic.main.row_search_result.view.*

class SearchResultAdapter(private val context: Context) :RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
    private val mData = mutableListOf<Product>()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: Product) {
            itemView.text_name.text =product.productName
            Picasso.get()
                .load(Config.IMAGE_BASE+product.image)
                .placeholder(R.drawable.ic_baseline_fastfood_24)
                .into(itemView.image_product)
            itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra(Product.KEY, product)
                if(context is ManageSearchWindow){
                    Log.d("myApp","Closing search window")
                    context.onCloseSearchWindow(this@SearchResultAdapter)
                }else{
                    Log.d("myApp","Cannot close search window")
                }
                context.startActivity(intent)
            }
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

    /**
     * Interface for objects that have a search window to open or close.
     * Should be implemented on a [Context]
     */
    interface ManageSearchWindow{
        fun onCloseSearchWindow(adapter : SearchResultAdapter?=null)
        fun onOpenSearchWindow(adapter : SearchResultAdapter?=null)
    }
}