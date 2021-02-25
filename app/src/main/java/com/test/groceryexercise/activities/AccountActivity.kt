package com.test.groceryexercise.activities

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.widget.FrameLayout
import com.test.groceryexercise.R
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.fragments.EditFieldFragment
import com.test.groceryexercise.models.User
import kotlinx.android.synthetic.main.activity_account.*
import java.io.Serializable

class AccountActivity : ListingActivity() {
    override val contentResource: Int
        get() = R.layout.activity_account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val user = SessionManager(this).user
        text_name.text = user?.firstName ?: "Guest"
        addFragments(user)
    }

    private fun addFragments(user: User?) {
        val fields =mutableListOf<Triple<String,Serializable,String?>>()
        fields.add(Triple("Delivery Address","Click to add or remove addresses","address"))
        if(user!=null){
            fields.add(Triple("Phone Number", user,"mobile"))
            fields.add(Triple("Email Address",user,"email"))
        }
        val transaction = supportFragmentManager.beginTransaction()
        var id = 100000
        for( (title, obj, field) in fields){
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

            val view = FrameLayout(this)
            view.layoutParams = params
            view.id=id
            id+=1
            parent_account.addView(view)
            transaction.add(view.id, EditFieldFragment.newInstance(title, obj, field))
        }
        transaction.commit()
    }
}