package com.test.groceryexercise.util

import android.graphics.Bitmap
import android.os.Bundle
import androidx.collection.LruCache
import com.android.volley.toolbox.ImageLoader

class LruBitmapCache(size : Int = defaultSize) : LruCache<String, Bitmap>(size), ImageLoader.ImageCache {
    override fun getBitmap(url: String): Bitmap? {
        return get(url)
    }

    override fun putBitmap(url: String, bitmap: Bitmap) {
        put(url,bitmap)
    }
    fun toBundle(): Bundle {
        val b = Bundle()
        val vals = snapshot()
        val keys = vals.keys
        for(k in keys){
            b.putParcelable(k,vals[k])
        }
        return b
    }
    companion object{
        const val KEY="CACHE"
        const val defaultSize = 20
        fun fromBundle(bundle:Bundle?):LruBitmapCache{
            val b = bundle ?: Bundle()
            return LruBitmapCache().apply {
                val keys = b.keySet()
                for(k in keys){
                    val bitmap = b.getParcelable(k) as? Bitmap
                    if(bitmap !=null) {
                        put(k,bitmap)
                    }
                }
            }
        }
    }
}