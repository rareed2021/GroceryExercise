package com.test.groceryexercise.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.groceryexercise.R
import com.test.groceryexercise.app.Config
import com.test.groceryexercise.models.CartItem
import com.test.groceryexercise.models.Order
import kotlinx.android.synthetic.main.row_order_product.view.*

class OrderProductAdapter(private val context: Context, private val mOrder: Order) : RecyclerView.Adapter<OrderProductAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: CartItem) {
            itemView.text_product_name.text = product.productName
            itemView.text_price.text = "\$${product.price}"
            if(product.mrp>product.price){
                itemView.text_mrp.text = "\$${product.mrp}"
                itemView.text_mrp.paintFlags = itemView.text_mrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemView.text_savings.text = "You saved \$${product.quantity * (product.mrp-product.price)}"
            }
            itemView.text_amount.text = product.quantity.toString()
            Picasso
                .get()
                .load(Config.IMAGE_BASE+product.image)
                .placeholder(R.drawable.ic_baseline_fastfood_24)
                .into(itemView.image_product)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_order_product,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mOrder.products[position])
    }

    override fun getItemCount(): Int {
        return mOrder.products.size
    }
}