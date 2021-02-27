package com.test.groceryexercise.models

import android.content.Context
import com.test.groceryexercise.app.SessionManager
import java.io.Serializable

data class Order(
    val orderSummary: CheckoutTotal?,
    val payment: Payment,
    val products: List<CartItem>,
    val shippingAddress: Address,
    val userId: String,
    val user: User?, //not required to send user, but should anyway
    val orderStatus: String?=null,//retrieved from api, but not sent
    val __v: Int?=null,
    val _id: String?=null,
    val date: String?=null,
):Serializable{
    /**
     * Sometimes orders arrive missing data that we are able to fill in locally.
     */
    fun cleanUpOrder(context: Context):Order{
        val session  = SessionManager(context)
        val currentUser = session.user
        var ret = this

        ret = if(user==null && currentUser!=null){//ought not be null, but can be from api
                ret.copy(user=currentUser)
            }else{ret}
        ret = if(orderSummary==null) {//shouldn't be null, but sometimes is when retrieved from api
            ret.copy(orderSummary = ret.calcTotal())
        }else {ret}
        return ret
    }

    /**
     * Number of overall items in order
     */
    val totalItems : Int
        get() =products.map{it.quantity}.sum()

    /**
     * Calculate total cost of order
     */
    fun calcTotal():CheckoutTotal{
        val ret= CheckoutTotal()
        for(product in this.products){
            ret.orderAmount+=product.mrp*product.quantity
            ret.ourPrice+=product.price*product.quantity
        }
        ret.totalAmount=ret.ourPrice
        return ret
    }
    companion object{
        const val KEY = "ORDER"
    }
}

data class OrderSummary(
    val deliveryCharges: Int,
    val discount: Int,
    val orderAmount: Int,
    val ourPrice: Int,
    val totalAmount: Int,
    val _id: String?,
)

data class Payment(
    val paymentMode: String,//cash
    val paymentStatus: String,//completed
    val _id: String?=null,
):Serializable


data class ShippingAddress(
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String
)


data class OrderResponse(
    val error:Boolean,
    val count:Int,
    val data: List<Order>
)