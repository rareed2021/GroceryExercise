package com.test.groceryexercise.activities

import android.content.*
import android.os.AsyncTask
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.test.groceryexercise.R
import com.test.groceryexercise.adapters.SearchResultAdapter
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.Product
import com.test.groceryexercise.services.SearchProductService
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.nav_drawer.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.template_content_container.view.*
import kotlin.properties.Delegates

/**
 * Abstract [AppCompatActivity] that initializes menu, nav drawer, and search for activities
 */
abstract class ListingActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    SearchResultAdapter.ManageSearchWindow{
    protected lateinit var dbHelper: DBHelper
    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navigationView: NavigationView
    protected var cartTextView: TextView? = null
    protected var mBound = false
    protected lateinit var contentContainer : LinearLayout
    protected var mService: SearchProductService? = null
    protected lateinit var searchBar: EditText
    protected lateinit var searchResults: RecyclerView

    protected lateinit var searchResultAdapter : SearchResultAdapter
    protected val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("myApp","Service bound")
            val binder = service as SearchProductService.SearchBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

    }
    private var _currentCartCount by Delegates.notNull<Int>()
    protected var currentCartCount: Int
        get() = _currentCartCount
        set(value) {
            _currentCartCount = value
            setCartIconText()
        }

    fun updateCartCount() {
        val size = dbHelper.cartSize
        currentCartCount = size
    }

    private fun setCartIconText() {
        cartTextView?.text = _currentCartCount.toString()
        cartTextView?.visibility = if (_currentCartCount > 0) View.VISIBLE else View.GONE
    }


    open val showBackButton: Boolean = true
    open val showCartButton: Boolean = true
    open val showSearchBar: Boolean = true
    open val toolbarTitle: String? = null


    /**
     * Primary layout resource for activity.
     * This should not be manually set in onCreate
     */
    abstract val contentResource: Int
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Actual content needs to be inside nav drawer, so make this our top level element
        setContentView(R.layout.nav_drawer)
        setupViews()
    }

    /**
     * This function inflates and adds the various views needed for search, menu, and content
     */
    fun setupViews() {
        //Linear layout that we can put various UI views into
        //linear because it makes handling adding search and results easier.
        //includes a child frame_layout that makes hiding main activity content easier
        contentContainer = LayoutInflater.from(this)
            .inflate(R.layout.template_content_container, drawer_layout,false) as LinearLayout
        val content = LayoutInflater.from(this).inflate(contentResource, contentContainer.frame_content, false)
        val bar = LayoutInflater.from(this).inflate(R.layout.app_bar, drawer_layout, false)
        dbHelper = DBHelper(this)
        _currentCartCount = dbHelper.cartSize
        drawerLayout = drawer_layout
        navigationView = nav_view
        //the search bar itself
        searchBar = LayoutInflater.from(this)
            .inflate(R.layout.template_searchbar, drawer_layout, false) as EditText
        //a results page that will
        searchResults = LayoutInflater.from(this)
            .inflate(R.layout.template_search_results, drawer_layout, false) as RecyclerView
        //Anything added to drawerLayout MUST be non-last, otherwise the nav view breaks
        drawerLayout.addView(contentContainer, 0)
        contentContainer.frame_content.addView(content)
        if (showSearchBar) {
            //results must be before content so that it will come on top when shown
            contentContainer.addView(searchResults, 0)
            contentContainer.addView(searchBar, 0)
            searchResultAdapter = SearchResultAdapter(this)
            searchResults.adapter = searchResultAdapter
            searchResults.layoutManager = LinearLayoutManager(this)
        }
        contentContainer.addView(bar, 0)
    }


    override fun onStart() {
        super.onStart()
        val bar = toolbar
        if (bar != null) {
            setupToolbar(toolbar)
        }
        setupNavDrawer()
        setupSearchBar()
    }

    private fun setupSearchBar() {
        Intent(this, SearchProductService::class.java).also {
            bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
        searchBar.addTextChangedListener {
            onOpenSearchWindow()
            val searchTask = GetSearchResultsTask()
            searchTask.execute(searchBar.text.toString())
        }
        searchBar.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                onOpenSearchWindow()
            }
        }
    }

    override fun onCloseSearchWindow(adapter: SearchResultAdapter?) {
        searchResults.visibility = View.GONE
        contentContainer.frame_content.visibility=View.VISIBLE
        searchBar.text.clear()
    }


    override fun onOpenSearchWindow(adapter: SearchResultAdapter?) {
        searchResults.visibility = View.VISIBLE
        contentContainer.frame_content.visibility =View.GONE
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    override fun onRestart() {
        super.onRestart()
        //could have been changed whatever we were at
        updateCartCount()
        //if we've left the activity already, we ought to close the bar
        onCloseSearchWindow()
    }

    private fun setupNavDrawer() {
        navigationView.setNavigationItemSelectedListener(this)
        setNavMenu()
        val bar = toolbar
        if (bar != null && !showBackButton) {
            val toggle = ActionBarDrawerToggle(this, drawerLayout, bar, 0, 0)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
        }
    }


    fun updateHeader() {
        setNavMenu()
    }


    /**
     * Sets the menu options on side menu
     */
    private fun setNavMenu() {
        val header = navigationView.getHeaderView(0)
        val session = SessionManager(this)
        val user = session.user
        val loggedIn = user != null
        if (header != null) {
            val email = user?.email ?: ""
            header.text_email.text = email
            header.text_email.visibility = if (email == "") View.GONE else View.VISIBLE
            header.text_name.text = user?.firstName ?: "Guest"
        }
        val menu = navigationView.menu
        val signin = menu.findItem(R.id.menu_signin)
        val account = menu.findItem(R.id.menu_account)
        val logout = menu.findItem(R.id.menu_logout)
        val order = menu.findItem(R.id.menu_order)
        signin.isVisible = !loggedIn
        account.isVisible = loggedIn
        logout.isVisible = loggedIn
        order.isVisible = loggedIn
    }

    open fun setupToolbar(bar: Toolbar) {
        setSupportActionBar(bar)
        if (showBackButton)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (toolbarTitle != null) {
            supportActionBar?.title = toolbarTitle
        }
    }


    //mostly setting up the cart.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val sessionManager = SessionManager(this)
        val user = sessionManager.user
        val cartItem = menu?.findItem(R.id.menu_cart)
        if (showCartButton) {
            MenuItemCompat.setActionView(cartItem, R.layout.menu_cart_layout)
            val cartView = MenuItemCompat.getActionView(cartItem)
            cartTextView = cartView.text_cart_items
            setCartIconText()
            cartView.setOnClickListener {
                startActivity(Intent(this, ShowCartActivity::class.java))
            }
        } else {
            cartItem?.isVisible = false
        }
        val cart = menu?.findItem(R.id.menu_cart)
        cart?.title = "(${dbHelper.cartSize})"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_cart -> startActivity(Intent(this, ShowCartActivity::class.java))
            R.id.menu_logout -> {
                val session = SessionManager(this)
                session.user = null
                invalidateOptionsMenu()
                setNavMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_order -> startActivity(Intent(this, ListOrdersActivity::class.java))
            R.id.menu_account -> startActivity(Intent(this, AccountActivity::class.java))
            R.id.menu_address -> startActivity(Intent(this, AddressListActivity::class.java))
            R.id.menu_signin -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.menu_logout -> {
                val builder = AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Log Out", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            val session = SessionManager(this@ListingActivity)
                            session.user = null
                            this@ListingActivity.invalidateOptionsMenu()
                            this@ListingActivity.setNavMenu()
                        }
                    })
                    .setNegativeButton("Stay"
                    ) { dialog, _ -> dialog?.dismiss() }
                builder.create().show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    //in case of search or open drawer, want to get rid of those first
    override fun onBackPressed() {
        when{
            drawerLayout.isDrawerOpen(GravityCompat.START)->drawerLayout.closeDrawer(GravityCompat.START)
            searchResults.visibility==View.VISIBLE -> {
                onCloseSearchWindow()
            }
            else -> super.onBackPressed()
        }
    }

    /**
     * [AsyncTask] to handle search results
     * Fetches data from service, then updates search UI
     */
    private inner class GetSearchResultsTask() : AsyncTask<String,Unit, Array<Product>>(){
        override fun doInBackground(vararg params: String?): Array<Product> {
            val str = params.firstOrNull()
            if(str!=null) {
                return mService?.searchProducts(str) ?: arrayOf()
            }
            return arrayOf()
        }

        override fun onPostExecute(result: Array<Product>?) {
            Log.d("myApp","Got search results ${result?.size}")
            if(result!=null) {
                searchResultAdapter.setData(result)
            }
        }

    }
}