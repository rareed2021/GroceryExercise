package com.test.groceryexercise.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.groceryexercise.app.Config
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.databinding.PlusMinusButtonBinding
import com.test.groceryexercise.databinding.RowCartProductBinding
import com.test.groceryexercise.models.CartItem
import com.test.groceryexercise.models.CheckoutTotal
import kotlinx.android.synthetic.main.row_cart_product.view.*
import kotlinx.android.synthetic.main.row_cart_product.view.text_price

class CartListAdapter(val context : Context, data:List<CartItem> = listOf()) : RecyclerView.Adapter<CartListAdapter.ViewHolder>(){
    private val mData  = data.toMutableList()
    lateinit var binding :RowCartProductBinding
    init{
        if(context is OnUpdateTotals)
            context.updateTotals(totalCost)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: CartItem) {
            itemView.text_price.text = "\$${product.price}"
            itemView.text_product_name.text = product.productName
            //itemView.text_amount.text = product.amount.toString()
            val binding = PlusMinusButtonBinding.bind(itemView.change_amount)
            val amountButtonAdapter = AmountButtonAdapter(context, product, true)
            amountButtonAdapter.init(itemView.change_amount)
            amountButtonAdapter.setOnAmountChangedListener {
                mData[adapterPosition].amount=it
                update()
            }
            if(product.mrp != product.price){
                itemView.text_mrp.visibility=View.VISIBLE
                itemView.text_mrp.text = "\$${product.mrp}"
                itemView.text_mrp.paintFlags = itemView.text_mrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                itemView.text_mrp.visibility =View.GONE
            }
            Picasso.get()
                .load(Config.IMAGE_BASE + product.image)
                .resize(80,80)
                .into(itemView.image_product)
            itemView.button_delete.setOnClickListener {
                val db = DBHelper(context)
                db.removeFromCart(product._id)
                mData.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                update()
            }
        }
    }

    private fun update(){
        if(context is OnUpdateTotals)
            context.updateTotals(totalCost)
    }

    val totalCost:CheckoutTotal
        get(){
            val total = CheckoutTotal()
            for(item in mData){
                total.subtotal+= item.mrp * item.amount
                total.total += item.price * item.amount
            }
            total.discount = total.subtotal - total.total
            return total
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        binding  = RowCartProductBinding.inflate(inflater,parent, false )
        //val view = LayoutInflater.from(context).inflate(R.layout.row_cart_product,parent,false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int =mData.size

    interface OnUpdateTotals{
        fun updateTotals(totals: CheckoutTotal)
    }
}