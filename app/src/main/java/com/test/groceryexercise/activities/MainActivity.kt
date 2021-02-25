package com.test.groceryexercise.activities

import Category
import CategoryResponse
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.TimeoutError
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.CategoryAdapter
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.User
import com.test.groceryexercise.util.LruBitmapCache
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.app_bar.*
import java.io.Serializable

class MainActivity : ListingActivity() {
    lateinit var mCategories: List<Category>
    lateinit var mQueue: RequestQueue
    //lateinit var cache: LruBitmapCache
    var user: User? = null
    override val contentResource: Int = R.layout.activity_main

    override val showBackButton: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        //setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        val session = SessionManager(this)
        user = session.user
        mQueue = Volley.newRequestQueue(this)
        val maybeBundle = savedInstanceState?.getBundle(LruBitmapCache.KEY)
        //cache = LruBitmapCache.fromBundle(maybeBundle)
        val maybeData = intent.getSerializableExtra(Category.KEY) as? Array<Category>
        if(maybeData!=null){
            mCategories = maybeData.toList()
            loadAdapter()
        }else{
            getData()
        }
        setupClickHandlers()
    }

    override fun setupToolbar(bar: Toolbar) {
        Log.d("myApp","Calling custom toolbar")
        bar.title = "Welcome"
        super.setupToolbar(bar)
    }


    private fun setupClickHandlers() {
        /*text_logout.setOnClickListener {
            user=null
            val session = SessionManager(this)
            session.user=null
            //intent.putExtra(User.KEY,null as Serializable?)
            setupUserText()
        }
        text_register.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        text_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }*/
    }
/*
    private fun setupUserText() {
        val hasUser = user !=null
        val showIfHas = if (hasUser) View.VISIBLE else View.GONE
        val showIfNot = if (!hasUser) View.VISIBLE else View.GONE
        if(user !=null){
            text_welcome.text = "Welcome ${user?.firstName}"
        }
        //text_register.visibility=showIfNot
        text_already_have.visibility = showIfNot
        text_login.visibility = showIfNot
        text_logout.visibility = showIfHas
        text_welcome.visibility = showIfHas
    }*/

    private fun loadAdapter() {
        progress_main.visibility = View.GONE
//        val loader = ImageLoader(mQueue,cache)
        recycler_categories.adapter = CategoryAdapter(this, mCategories)
        recycler_categories.layoutManager = GridLayoutManager(this, 2)
//        recycler_categories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onStart() {
        val session = SessionManager(this)
        session.clearActive()
        super.onStart()
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
                mCategories = cats
                loadAdapter()
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



    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putBundle(LruBitmapCache.KEY, cache.toBundle())
        //outState.putSerializable(Category.KEY, mCategories.toTypedArray())
        super.onSaveInstanceState(outState)
    }

}