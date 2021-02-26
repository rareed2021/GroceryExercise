package com.test.groceryexercise.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.groceryexercise.R
import com.test.groceryexercise.models.Order
import kotlinx.android.synthetic.main.row_order.view.*
import java.time.LocalDate
import java.time.LocalDateTime

class OrderListAdapter(private val context: Context, orders:List<Order>) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>(){

    private val mData = orders.toMutableList()
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(order: Order) {
            if(order.orderSummary!=null) {
                itemView.text_cost.text = "\$${order.orderSummary.totalAmount}"
            }else{
                itemView.text_cost.text = "\$${order.calcTotal().totalAmount}"
            }
            itemView.text_email.text =  order.user?.email
            itemView.text_items.text = order.products.size.toString()
            if(order.date!=null) {
                val date = LocalDate.parse(order.date.split("T")[0])
                //itemView.text_date.text =
                //    "${date.dayOfWeek.toString().titleCase()} ${date.month.toString().titleCase()} ${date.dayOfMonth}"
                itemView.text_date.text = order.date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        Log.d("myApp","Number of orders: ${mData.size}",)
        return mData.size
    }
}


fun String.titleCase():String = "${this[0].toUpperCase()}${this.drop(1).toLowerCase()}"