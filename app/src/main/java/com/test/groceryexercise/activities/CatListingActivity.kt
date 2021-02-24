package com.test.groceryexercise.activities

import Category
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.SubcategoryPagerAdapter
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.Product
import com.test.groceryexercise.models.ProductResponse
import com.test.groceryexercise.models.SubCategory
import com.test.groceryexercise.models.SubCategoryResponse
import kotlinx.android.synthetic.main.activity_cat_listing.*
import kotlinx.android.synthetic.main.app_bar.*

class CatListingActivity : ListingActivity() {
    lateinit var mCategory : Category
    var mSubcats : List<SubCategory>? = null
    var mProducts : List<Product>? = null
    override val contentResource: Int
        get() = R.layout.activity_cat_listing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_cat_listing)
        init()
    }

    private fun init() {
        val session = SessionManager(this)
        mCategory = intent.getSerializableExtra(Category.KEY) as Category
        session.setActive(mCategory)
        getData()
    }

    override fun setupToolbar(bar:Toolbar) {
        bar.title = mCategory.catName
        super.setupToolbar(bar)
    }

    private fun getData(){
        val queue = Volley.newRequestQueue(this)
        val subcatRequest = StringRequest(
            Request.Method.GET,
            Endpoints.subcategoriesByCategory(mCategory.catId),
            {
                progress_subcategory.visibility = View.GONE
                val gson = Gson()
                val response = gson.fromJson(it, SubCategoryResponse::class.java)
                mSubcats = response.data
                pager_subcategories.adapter = SubcategoryPagerAdapter(supportFragmentManager,mSubcats!!)
                tabs_subcategories.setupWithViewPager(pager_subcategories)
//                setAdapter()
            },
            {}
        )
        /*val productRequest = StringRequest(
            Request.Method.GET,
            Product.urlFromCatId(mCategory.catId),
            {
                val gson = Gson()
                val response = gson.fromJson(it, ProductResponse::class.java)
                mProducts = response.data
                //Log.d("myApp","Number of products: ${mProducts?.size}")
//                setAdapter()
            },
            {}
        )*/
        queue.add(subcatRequest)
    }

   /* private fun setAdapter() {
        val prods = mProducts
        val subcats = mSubcats
        if(prods !=null && subcats!=null){
            val mutableData :MutableMap<SubCategory,MutableList<Product>> = mutableMapOf()
            val subcatmap = subcats.map { Pair(it.subId,it) }.toMap()
            for(cat in subcatmap){
                mutableData[cat.value] = mutableListOf()
            }
            for(p in prods){
                val s = subcatmap[p.subId]
                //Log.d("myApp","id: ${p.subId}  sub ${s?.subName}")
                mutableData[s]?.add(p)
            }
            val data = mutableData.mapValues {
               it.value.toList()
            }
            Log.d("myApp","Setting up tabs and pager")
            pager_subcategories.adapter = SubcategoryPagerAdapter(supportFragmentManager, data)
            tabs_subcategories.setupWithViewPager(pager_subcategories)
//            val adapter = ProductListAllAdapter(this,data)
//            recycler_products.adapter = adapter
//            recycler_products.layoutManager = LinearLayoutManager(this)
        }
    }*/
}