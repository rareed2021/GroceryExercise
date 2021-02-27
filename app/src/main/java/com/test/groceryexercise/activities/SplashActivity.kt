package com.test.groceryexercise.activities

import CategoryResponse
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.services.SearchProductService
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    //this need do nothing. We want to bind service so that other activities can make use of it later
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        getData()
        //we aren't going to stop this manually
        //but it's set in manifest to stopWithTask
        Thread{
            kotlin.run {
                Intent(this, SearchProductService::class.java).also { intent ->
                    startService(intent)
                }
            }
        }.run()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun getData() {
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET,
            Endpoints.categories,
            {
                val gson = Gson()
                val response = gson.fromJson(it, CategoryResponse::class.java)
                if(response.error){
                    Log.d("myApp","Error in response")
                }
                val cats = response.data
                val session = SessionManager(this)
                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.putExtra(Category.KEY, cats.toTypedArray())
                Log.d("myApp","Have data. Reloading")
                if(!session.loadActive(mainIntent)){
                    startActivity(mainIntent)
                }
            },
            {
                if(it is TimeoutError){
                    text_error.text = getString(R.string.error_timeout)
                    text_error.visibility = View.VISIBLE
                }
                Log.d("myApp", "Error $it")
            }
        )
        queue.add(request)
    }
}