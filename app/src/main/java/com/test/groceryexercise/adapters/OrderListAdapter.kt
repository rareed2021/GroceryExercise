package com.test.groceryexercise.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.groceryexercise.R
import com.test.groceryexercise.activities.ListOrdersActivity
import com.test.groceryexercise.models.Order
import kotlinx.android.synthetic.main.row_order.view.*
import java.time.LocalDate

class OrderListAdapter(private val context: Context, orders:List<Order>) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>(){

    private val mData = orders.toMutableList()
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(order: Order) {
            val selected = (context as?ListOrdersActivity)?.selectedOrder
            val isSelected = selected == order
            if(order.orderSummary!=null) {
                itemView.text_cost.text = "\$${order.orderSummary.totalAmount}"
            }else{
                itemView.text_cost.text = "\$${order.calcTotal().totalAmount}"
            }
            itemView.text_items.text = order.totalItems.toString()
            if(order.date!=null) {
                val date = LocalDate.parse(order.date.split("T")[0])
                itemView.text_date.text = "${date.month.name.titleCase()} ${date.dayOfMonth}, ${date.year}"
            }
            itemView.icon_cart.visibility = if(isSelected)View.GONE else View.VISIBLE
            itemView.icon_selected.visibility = if(isSelected)View.VISIBLE else View.GONE
            itemView.recycler_order_items.adapter = ProductImageAdapter(
                context,
                order.products.map{it.image},
                Pair(180,180)
            )
            itemView.recycler_order_items.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemView.setOnClickListener {
                if(context is ListOrdersActivity){
                    val pos = mData.indexOf(context.selectedOrder)
                    if(pos>0){

                        notifyItemChanged(pos)
                    }
                    notifyItemChanged(adapterPosition)
                }
                if(context is OnChangeOrder){
                    context.onChangeOrder(order)
                }
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
        return mData.size
    }

    interface OnChangeOrder{
        fun onChangeOrder(order:Order)
    }
}


fun String.titleCase():String = "${this[0].toUpperCase()}${this.drop(1).toLowerCase()}"