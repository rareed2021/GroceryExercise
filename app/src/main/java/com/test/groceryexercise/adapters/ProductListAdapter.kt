package com.test.groceryexercise.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.test.groceryexercise.R
import com.test.groceryexercise.activities.ProductDetailActivity
import com.test.groceryexercise.app.Config
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.models.Product
import com.test.groceryexercise.models.ProductResponse
import kotlinx.android.synthetic.main.row_product.view.*
import java.io.Serializable

class ProductListAdapter(private val context: Activity, private val subId: Int) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    private var mData: List<Product> = listOf()

    init {
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            Endpoints.productsBySub(subId),
            {
                val gson = Gson()
                val response = gson.fromJson(it, ProductResponse::class.java)
                mData = response.data
                notifyDataSetChanged()
            },
            {}
        )
        queue.add(request)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun init(product: Product) {
            itemView.text_name.text = product.productName
            val unit = when(product.unit) {
                "1" -> "Each"
                "Each" -> "Each"
                "Ea" -> "Each"
                else -> "per " + product.unit
            }
            itemView.text_price.text = "\$${product.price} $unit"
            itemView.text_description.text = product.description
            Picasso.get()
                .load(Config.IMAGE_BASE + product.image)
                .resize(80, 80)
                .placeholder(R.drawable.ic_baseline_fastfood_24)
                .into(itemView.image_product)
            itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra(Product.KEY, product)
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.init(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}