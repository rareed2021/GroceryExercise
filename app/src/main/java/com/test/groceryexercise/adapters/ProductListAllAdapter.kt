package com.test.groceryexercise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.groceryexercise.R
import com.test.groceryexercise.app.Config
import com.test.groceryexercise.models.Product
import com.test.groceryexercise.models.SubCategory
import kotlinx.android.synthetic.main.row_product.view.*
import kotlinx.android.synthetic.main.row_product.view.text_name
import kotlinx.android.synthetic.main.row_subcategory.view.*

class ProductListAllAdapter(private val context: Context, data:Map<SubCategory,List<Product>>): RecyclerView.Adapter<ProductListAllAdapter.ViewHolder>(){

    var mData:List<Entry>
    init{
        val ret : MutableList<Entry> = mutableListOf()
        for(k in data.keys){
            ret.add(Entry.SubCat(k))
            val subcat = data[k]
            if(subcat!=null){
                for(p in subcat){
                    ret.add(Entry.Prod(p))
                }
            }
        }
        mData = ret.toList()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(entry: Entry){
            when(entry){
                is Entry.Prod -> bind(entry)
                is Entry.SubCat -> bind(entry)
            }
        }
        fun bind(product : Entry.Prod){
            val (prod) = product
            itemView.text_name.text  =prod.productName
            itemView.text_description.text = prod.description
            itemView.text_price.text = "\$${prod.price}"
            Picasso.get()
                .load(Config.IMAGE_BASE + prod.image)
                .resize(80,80)
                .into(itemView.image_product)
        }
        fun bind(subcat : Entry.SubCat){
            val (sc) = subcat
            itemView.text_subcat_name.text = sc.subName
        }
    }
    sealed class Entry{
        data class Prod(val product: Product):Entry()
        data class SubCat(val cat: SubCategory):Entry()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(viewType, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }


    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(mData[position]){
            is Entry.Prod -> R.layout.row_product
            is Entry.SubCat -> R.layout.row_subcategory
        }
    }
}