package com.test.groceryexercise.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.test.groceryexercise.R
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.app.SessionManager
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.nav_drawer.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlin.properties.Delegates

abstract class ListingActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    protected lateinit var dbHelper: DBHelper
    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navigationView: NavigationView
    protected var cartTextView : TextView? = null
    private var _currentCartCount by Delegates.notNull<Int>()
    protected var currentCartCount : Int
        get() = _currentCartCount
        set(value){
            _currentCartCount=value
            setCartIconText()
        }
    protected fun updateCartCount(){
        currentCartCount = dbHelper.cartSize
    }
    private fun setCartIconText() {
        cartTextView?.text = _currentCartCount.toString()
        cartTextView?.visibility = if(_currentCartCount>0)View.VISIBLE else View.GONE
    }

    abstract val contentResource : Int
    open val showBackButton:Boolean = false
    open val showCartButton:Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val content :View = findViewById(R.id.content)
        setContentView(R.layout.nav_drawer)
        val content = LayoutInflater.from(this).inflate(contentResource,drawer_layout,false)
        val bar = LayoutInflater.from(this).inflate(R.layout.app_bar, drawer_layout, false)
        dbHelper = DBHelper(this)
        _currentCartCount = dbHelper.cartSize
        drawerLayout = drawer_layout
        navigationView = nav_view
        drawerLayout.addView(content,0)
        if(content is LinearLayout){
            content.addView(bar,0)
        }
    }


    override fun onStart() {
        super.onStart()
        val bar = toolbar
        if(bar!=null) {
            setupToolbar(toolbar)
        }
        setupNavDrawer()
    }

    override fun onRestart() {
        super.onRestart()
        updateCartCount()
    }

    private fun setupNavDrawer() {
        navigationView.setNavigationItemSelectedListener(this)
        setHeaderUI()
        val bar = toolbar
        if(bar!=null && !showBackButton) {
            val toggle = ActionBarDrawerToggle(this, drawerLayout, bar, 0,0)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
        }
    }



    private fun setHeaderUI(){
        val header = navigationView.getHeaderView(0)
        val session = SessionManager(this)
        val user = session.user
        val loggedIn =  user!=null
        if(header!=null) {
            val email  = user?.email?:""
            header.text_email.text = email
            header.text_email.visibility= if(email=="")View.GONE else View.VISIBLE
            header.text_name.text = user?.firstName?:"Guest"
        }
        val menu = navigationView.menu
        val signin = menu.findItem(R.id.menu_signin)
        val account = menu.findItem(R.id.menu_account)
        signin.isVisible = !loggedIn
        account.isVisible = loggedIn
    }

    open fun setupToolbar(bar:Toolbar){
        setSupportActionBar(bar)
        if (showBackButton)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val sessionManager = SessionManager(this)
        val user = sessionManager.user
        val cartItem = menu?.findItem(R.id.menu_cart)
        if(showCartButton) {
            MenuItemCompat.setActionView(cartItem, R.layout.menu_cart_layout)
            val cartView = MenuItemCompat.getActionView(cartItem)
            cartTextView = cartView.text_cart_items
            setCartIconText()
            cartView.setOnClickListener {
                startActivity(Intent(this, ShowCartActivity::class.java))
            }
        }else{
            cartItem?.isVisible=false
        }
        val loggedIn =  user!=null
        //val welcome = menu?.findItem(R.id.menu_welcome)
        //welcome?.isVisible = loggedIn
        val logout = menu?.findItem(R.id.menu_logout)
        logout?.isVisible = loggedIn
        //val login = menu?.findItem(R.id.menu_login)
        //login?.isVisible = !loggedIn
        val cart = menu?.findItem(R.id.menu_cart)
        cart?.title = "(${dbHelper.cartSize})"
        /*if(user!=null) {
            welcome?.title = "Welcome ${user.firstName}"
        }*/
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_cart -> startActivity(Intent(this, ShowCartActivity::class.java))
            //R.id.menu_signin -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.menu_logout -> {
                val session = SessionManager(this)
                session.user=null
                invalidateOptionsMenu()
                setHeaderUI()
                //intent.putExtra(User.KEY,null as Serializable?)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_order -> Toast.makeText(this, "Orders",Toast.LENGTH_SHORT).show()
            R.id.menu_account -> Toast.makeText(this, "Account",Toast.LENGTH_SHORT).show()
            R.id.menu_address -> Toast.makeText(this, "Address",Toast.LENGTH_SHORT).show()
            R.id.menu_refer -> Toast.makeText(this, "Refer",Toast.LENGTH_SHORT).show()
            R.id.menu_help -> Toast.makeText(this, "Help",Toast.LENGTH_SHORT).show()
            R.id.menu_rate -> Toast.makeText(this, "Rate",Toast.LENGTH_SHORT).show()
            R.id.menu_signin -> startActivity(Intent(this, LoginActivity::class.java))
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }
}