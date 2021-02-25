package com.test.groceryexercise.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.activities.SelectPaymentActivity
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.Address
import com.test.groceryexercise.models.AddressResponse
import kotlinx.android.synthetic.main.row_address.view.*

class AddressAdapter(private val context: Context, private val checkoutOnClick:Boolean)  :RecyclerView.Adapter<AddressAdapter.ViewHolder>(){
    private val session = SessionManager(context)
    val mData = mutableListOf<Address>()
    init {
        updateData()
    }
    fun updateData(){
        val user = session.user
        if(user!=null) {
            val queue = Volley.newRequestQueue(context)
            val request = StringRequest(
                Request.Method.GET,
                Endpoints.addressesById(user._id),
                {
                    val response = Gson().fromJson(it, AddressResponse::class.java)
                    Log.d("myApp","Got Address List (size ${response.data.size}")
                    mData.clear()
                    mData.addAll(response.data)
                    notifyDataSetChanged()
                },
                {
                    Log.d("myApp","Error fetching addresses")
                    Log.d("myApp",String(it.networkResponse.data))
                }
            )
            queue.add(request)
        }else{
            mData.clear()
        }

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(address: Address) {
            itemView.text_streetname.text=address.streetName
            itemView.text_city.text = address.city
            itemView.text_pin.text =address.pincode.toString()
            itemView.text_house_number.text = address.houseNo
            if(checkoutOnClick){
                itemView.setOnClickListener {
                    val intent = Intent(context, SelectPaymentActivity::class.java)
                    intent.putExtra(Address.KEY, address)
                    context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_address, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}