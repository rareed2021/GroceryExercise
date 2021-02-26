package com.test.groceryexercise.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.test.groceryexercise.R
import com.test.groceryexercise.activities.ListingActivity
import com.test.groceryexercise.activities.ProductDetailActivity
import com.test.groceryexercise.app.Config
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.models.AmountButtonSettings
import com.test.groceryexercise.models.Product
import com.test.groceryexercise.models.ProductResponse
import kotlinx.android.synthetic.main.row_product.view.*

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

        fun bind(product: Product) {
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
            if(product.mrp>product.price){
                val pct = (100*((product.price-product.mrp)/product.mrp)).toInt()
                itemView.text_mrp.text = product.mrp.toString()
                itemView.text_mrp.paintFlags = itemView.text_mrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemView.text_mrp.visibility=View.VISIBLE
                itemView.text_savings.text = "$pct%"
                itemView.text_savings.visibility=View.VISIBLE
            }
            val adapter = AmountButtonAdapter(context, product, AmountButtonSettings())
            adapter.init(itemView.frame_button)
            adapter.setOnAmountChangedListener {
                if(context is ListingActivity){
                        context.updateCartCount()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}