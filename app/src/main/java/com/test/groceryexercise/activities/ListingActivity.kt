package com.test.groceryexercise.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.test.groceryexercise.R
import com.test.groceryexercise.app.DBHelper
import com.test.groceryexercise.app.SessionManager
import kotlinx.android.synthetic.main.app_bar.*

abstract class ListingActivity : AppCompatActivity() {
    protected lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(this)
    }


    override fun onStart() {
        super.onStart()
        val bar = toolbar
        Log.d("myApp","Setting toolbar")
        if(bar!=null) {
            setupToolbar(toolbar)
        }
    }
    open fun setupToolbar(bar:Toolbar){
        Log.d("myApp","Toolbar found")
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val sessionManager = SessionManager(this)
        val user = sessionManager.user
        val loggedIn =  user!=null
        val welcome = menu?.findItem(R.id.menu_welcome)
        welcome?.isVisible = loggedIn
        val logout = menu?.findItem(R.id.menu_logout)
        logout?.isVisible = loggedIn
        val login = menu?.findItem(R.id.menu_login)
        login?.isVisible = !loggedIn
        val cart = menu?.findItem(R.id.menu_cart)
        cart?.title = "(${dbHelper.cartSize})"
        if(user!=null) {
            welcome?.title = "Welcome ${user.firstName}"
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_cart -> startActivity(Intent(this, ShowCartActivity::class.java))
            R.id.menu_login -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.menu_logout -> {
                val session = SessionManager(this)
                session.user=null
                invalidateOptionsMenu()
                //intent.putExtra(User.KEY,null as Serializable?)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}