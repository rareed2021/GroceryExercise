package com.test.groceryexercise.adapters

import Category
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.squareup.picasso.Picasso
import com.test.groceryexercise.R
import com.test.groceryexercise.activities.CatListingActivity
import com.test.groceryexercise.app.Config
import kotlinx.android.synthetic.main.row_category.view.*

class CategoryAdapter(private val context: Activity,
                      private val mData:List<Category>,
) :RecyclerView.Adapter<CategoryAdapter.CatViewHolder>() {

    inner class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(category: Category) {
            itemView.text_category_name.text = category.catName
            val url = "${Config.IMAGE_BASE}${category.catImage}"
            Log.d("myApp",url)
            val pic = Picasso.get()
            pic.isLoggingEnabled=true
            pic
                .load(url)
                .into(itemView.image_category)
            itemView.setOnClickListener {
                val intent = Intent(context, CatListingActivity::class.java)
                intent.putExtra(Category.KEY,category)
                context.startActivity(intent)
            }
            //itemView.image_category.setImageUrl(category.catImage, mLoader)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        Log.d("myApp", "Loading adapter ${mData.size}")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_category,parent,false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}