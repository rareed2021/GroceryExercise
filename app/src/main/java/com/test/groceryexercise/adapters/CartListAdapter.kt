package com.test.groceryexercise.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.groceryexercise.app.Config
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.databinding.PlusMinusButtonBinding
import com.test.groceryexercise.databinding.RowCartProductConstraintBinding
import com.test.groceryexercise.models.CartItem
import com.test.groceryexercise.models.CheckoutTotal
import com.test.groceryexercise.models.Product
import com.test.groceryexercise.util.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_show_cart.*
import kotlinx.android.synthetic.main.row_cart_product.view.*
import kotlinx.android.synthetic.main.row_cart_product.view.text_price

class CartListAdapter(val context : Context, data:List<CartItem> = listOf()) : RecyclerView.Adapter<CartListAdapter.ViewHolder>(){
    private val mData  = data.toMutableList()
    lateinit var binding :RowCartProductConstraintBinding
    lateinit var mHelper :ItemTouchHelper
    init{
        if(context is OnUpdateTotals)
            context.updateTotals(totalCost)
    }
    inner class DeleteCallback(view:RecyclerView) : SwipeToDeleteCallback(context,view){
        override val title = "Remove From Cart?"
        override val message = "Are you sure you want to remove this from your cart?"
        override fun doDelete(viewHolder: RecyclerView.ViewHolder) {
            if(viewHolder is ViewHolder){
                viewHolder.deleteItem()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun deleteItem(){
            val db = DBHelper(context)
            val product = mData[adapterPosition]
            db.removeFromCart(product._id)
            mData.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
            update()
        }
        fun bind(product: CartItem) {
            itemView.text_price.text = "\$${product.price}"
            itemView.text_product_name.text = product.productName
            //itemView.text_amount.text = product.quantity.toString()
            val binding = PlusMinusButtonBinding.bind(itemView.change_amount)
            val amountButtonAdapter = AmountButtonAdapter(context, product, true)
            amountButtonAdapter.init(itemView.change_amount)
            amountButtonAdapter.setOnAmountChangedListener {
                mData[adapterPosition].quantity=it
                update()
            }
            if(product.mrp != product.price){
                itemView.text_mrp.visibility=View.VISIBLE
                itemView.text_savings.text = "You saved \$${product.mrp - product.price}!"
                itemView.text_savings.visibility=View.VISIBLE
                itemView.text_mrp.text = "\$${product.mrp}"
                itemView.text_mrp.paintFlags = itemView.text_mrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                itemView.text_mrp.visibility =View.GONE
                itemView.text_savings.visibility = View.GONE
            }
            Picasso.get()
                .load(Config.IMAGE_BASE + product.image)
                .resize(80,80)
                .into(itemView.image_product)
            /*itemView.button_delete.setOnClickListener {
                deleteItem()
            }*/
        }
    }

    private fun update(){
        if(context is OnUpdateTotals)
            context.updateTotals(totalCost)
    }

    val totalCost:CheckoutTotal
        get(){
            val total = DBHelper(context).cartCost
            /*val total = CheckoutTotal()
            for(item in mData){
                total.subtotal+= item.mrp * item.quantity
                total.total += item.price * item.quantity
            }
            total.discount = total.subtotal - total.total*/
            return total
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        binding  = RowCartProductConstraintBinding.inflate(inflater,parent, false )
        //val view = LayoutInflater.from(context).inflate(R.layout.row_cart_product,parent,false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int =mData.size


    fun init(view: RecyclerView) {
        view.adapter = this
        view.layoutManager = LinearLayoutManager(context)
        val cb = DeleteCallback(view)
        mHelper = ItemTouchHelper(cb)
        cb.mHelper=mHelper
        mHelper.attachToRecyclerView(view)
    }

    interface OnUpdateTotals{
        fun updateTotals(totals: CheckoutTotal)
    }
}