package com.test.groceryexercise.services

import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.models.Product
import com.test.groceryexercise.models.ProductResponse
import com.test.groceryexercise.util.SimpleSuffixTrie

internal const val MAX_DESCRIPTION = 20

class SearchProductService : Service() {
    private val mNames = SimpleSuffixTrie<Product>()
    private val mDescription = SimpleSuffixTrie<Product>()

    private val binder = SearchBinder()

    override fun onCreate() {
        super.onCreate()
        Log.d("myApp","Beginning product request")
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET,
            Endpoints.products,
            {
                val response = Gson().fromJson(it, ProductResponse::class.java)
                Log.d("myApp","Products loaded, building trie")
                for(p in response.data){
                    mNames.insert(p.productName,p)
                    var s = p.description
                    do{
                        //causes memory explosion if we try this with full descriptions.
                            //that's what I get for not using compact trie
                        val i = p.description.take(MAX_DESCRIPTION)
                        //mDescription.insert(i,p)
                        s = s.drop(MAX_DESCRIPTION)
                    }while (s.isNotBlank())
                    //mDescription.insert(p.description,p)
                }
            },
            {
                Log.e("myApp",String(it.networkResponse.data))
            }
        )
        queue.add(request)
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }


    /**
     * Fetches a number of [Product]s where a search term exists in name or description
     * Preferentially searches name
     * @searchString
     * @maxValues The maximum number of products to return.
     */
    fun searchProducts(searchString:String, maxValues :Int= 15):Array<Product>{
        if(searchString.isEmpty())
            return arrayOf()
        var ret = mNames.get(searchString).toMutableList()
        if(ret.size> maxValues){
            return ret.dropLast(ret.size-maxValues).toTypedArray()
        }
        val desc = mDescription.get(searchString)
        for(p in desc){
            if(ret.size==maxValues){
                break
            }
            ret.add(p)
        }
        return ret.toTypedArray()
    }

    inner class SearchBinder : Binder(){
        fun getService():SearchProductService = this@SearchProductService
    }
}