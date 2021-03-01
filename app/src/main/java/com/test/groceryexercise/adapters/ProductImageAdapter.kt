package com.test.groceryexercise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.groceryexercise.R
import com.test.groceryexercise.app.Config
import kotlinx.android.synthetic.main.row_image.view.*

class ProductImageAdapter(private val context: Context, data:List<String>? = null, private var size:Pair<Int,Int>?=null) : RecyclerView.Adapter<ProductImageAdapter.ViewHolder>() {
    private val mData  = data?.toMutableList()?:mutableListOf()
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(image: String) {
            var request = Picasso
                .get()
                .load(Config.IMAGE_BASE + image)
            size?.apply{
                request.apply{resize(first,second)}
            }
            request.into(itemView.image_main)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_image,parent,false)
        view.layoutParams.width
        /*size?.apply{
            val params = view.layoutParams
            params.width=first
            params.height = second
            view.layoutParams = params
        }*/
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}