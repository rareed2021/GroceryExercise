package com.test.groceryexercise.app

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.test.groceryexercise.models.CartItem
import com.test.groceryexercise.models.CheckoutTotal
import com.test.groceryexercise.models.Product

class DBHelper(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null,1) {

    companion object{
        const val DB_NAME = "groceryDB"
        const val TABLE_CART = "CART"
        const val cart_product_id = "pid"
        const val cart_product_name = "name"
        const val cart_product_mrp = "mrp"
        const val cart_product_price = "price"
        const val cart_product_amount = "amount"
        const val cart_product_image = "image"
        const val column_total = "total"
        const val column_subtotal = "subtotal"
        val cart_columns = arrayOf(cart_product_id, cart_product_name, cart_product_image, cart_product_price, cart_product_amount, cart_product_mrp)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val statement = "create table $TABLE_CART ($cart_product_id text, $cart_product_image text," +
                "$cart_product_name text, $cart_product_mrp real, $cart_product_price real, $cart_product_amount integer)"
        db?.execSQL(statement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun addToCart(product: Product, amount:Int = 1){
        if(amount>=0){
            writableDatabase.beginTransaction()
            val content = ContentValues()
            content.put(cart_product_id, product._id)
            content.put(cart_product_amount,amount)
            content.put(cart_product_mrp, product.mrp)
            content.put(cart_product_price,product.price)
            content.put(cart_product_name,product.productName)
            content.put(cart_product_image,product.image)
            writableDatabase.insert(TABLE_CART, null, content)
        }
    }

    fun addToCart(product:CartItem){
        if(product.quantity>=0){
            val content = ContentValues()
            content.put(cart_product_id, product._id)
            content.put(cart_product_amount,product.quantity)
            content.put(cart_product_mrp, product.mrp)
            content.put(cart_product_price,product.price)
            content.put(cart_product_name,product.productName)
            content.put(cart_product_image,product.image)
            writableDatabase.insert(TABLE_CART, null, content)
        }
    }
    fun removeFromCart(id:String){
        writableDatabase.delete(TABLE_CART, "$cart_product_id = ?", arrayOf(id))
    }

    fun getCartItem(id: String): CartItem? {
        val whereArgs = arrayOf(id)
        val cursor = readableDatabase.query(TABLE_CART, cart_columns, "$cart_product_id  = ?", whereArgs, null, null, null)
        val ret =  if(cursor.moveToFirst())
            itemFromCursor(cursor)
        else
            null
        cursor.close()
        return ret
    }
    fun updateItemAmount(item:CartItem){
        val  whereArgs= arrayOf(item._id)
        val content = ContentValues()
        content.put(cart_product_amount,item.quantity)
        writableDatabase.update(TABLE_CART,content, "$cart_product_id = ?", whereArgs)
    }

    private fun itemFromCursor(cursor:Cursor):CartItem = CartItem(
        cursor.getString(cursor.getColumnIndex(cart_product_id)),
        cursor.getString(cursor.getColumnIndex(cart_product_image)),
        cursor.getDouble(cursor.getColumnIndex(cart_product_mrp)),
        cursor.getDouble(cursor.getColumnIndex(cart_product_price)),
        cursor.getString(cursor.getColumnIndex(cart_product_name)),
        cursor.getInt(cursor.getColumnIndex(cart_product_amount)),
    )

    fun clearCart() {
        writableDatabase.delete(TABLE_CART,"",arrayOf())
    }

    val cartCost : CheckoutTotal
        get(){
            val columns = arrayOf("sum($cart_product_price * $cart_product_amount) as $column_total",
                "sum($cart_product_mrp * $cart_product_amount) as $column_subtotal"
            )
            val cursor = readableDatabase.query(TABLE_CART, columns, null, null, null, null,null)
            val ret = CheckoutTotal()
            if(cursor.moveToFirst()){
                ret.totalAmount = cursor.getDouble(cursor.getColumnIndex(column_total))
                ret.ourPrice=ret.totalAmount
                ret.orderAmount = cursor.getDouble(cursor.getColumnIndex(column_subtotal))
                ret.discount = ret.orderAmount -ret.totalAmount
            }
            return ret
        }

    val cartSize : Int
        get(){
            val columns = arrayOf("count($cart_product_id)")
            val cursor = readableDatabase.query(TABLE_CART,columns, null, null, null, null, null)
            val ret =  if(cursor.moveToFirst()){
                cursor.getInt(0)
            }else{
                0
            }
            cursor.close()
            return ret
        }
    val cartItems : List<CartItem>
        get(){
            val ret :MutableList<CartItem> = mutableListOf()
            val cursor = readableDatabase.query(TABLE_CART, cart_columns, null, null, null, null, null)
            while(cursor.moveToNext()) {
                val item = itemFromCursor(cursor)
                ret.add(item)
            }
            cursor.close()
            return ret
        }
}