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
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

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
                val instant = Instant.parse(order.date)
//                val date = LocalDate.parse(order.date.split("T")[0])
//                val time  = LocalTime.parse(order.date.split("T")[1].dropLast(2))
                val datetime= instant.atZone(ZoneId.of("America/Chicago"))
                itemView.text_date.text = datetime.format(DateTimeFormatter.ofPattern("LLL d"))//"${date.mon.name.titleCase()} ${date.dayOfMonth}, ${date.year}"
                itemView.text_time.text = datetime.format(DateTimeFormatter.ofPattern("hh:mm a"))
            }
            //itemView.icon_cart.visibility = if(isSelected)View.GONE else View.VISIBLE
            itemView.icon_selected.visibility = if(isSelected)View.VISIBLE else View.GONE
            val clickListener = {_:View->
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
            itemView.setOnClickListener(clickListener)
            itemView.recycler_order_items.adapter = ProductImageAdapter(
                context,
                order.products.map{it.image},
                Pair(200,200)
            )
            itemView.recycler_order_items.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemView.recycler_order_items.setOnClickListener(clickListener)
            itemView.recycler_order_items.setOnLongClickListener { clickListener(it);false }
            itemView.recycler_order_items.isLayoutFrozen=true
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