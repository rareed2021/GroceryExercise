package com.test.groceryexercise.activities

import CategoryResponse
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.app.Endpoints
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        getData()
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
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(Category.KEY, cats.toTypedArray())
                startActivity(intent)
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