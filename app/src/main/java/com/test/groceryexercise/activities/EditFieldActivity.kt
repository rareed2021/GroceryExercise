package com.test.groceryexercise.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.test.groceryexercise.R
import com.test.groceryexercise.app.ApiHelper
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.User
import kotlinx.android.synthetic.main.activity_edit_field.*
import java.io.Serializable

class EditFieldActivity : AppCompatActivity() {
    lateinit var mObj:Serializable
    lateinit var mTitle:String
    lateinit var mField:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_field)
        mTitle = intent.getStringExtra("TITLE") ?: ""
        mObj = intent.getSerializableExtra("OBJECT") ?: "" as Serializable
        mField = intent.getStringExtra("FIELD") ?:""
        init()
    }

    private fun init() {
        val obj = mObj
        if(obj is User) {
            edit_confirm.text.clear()
            edit_confirm.text.insert(0,obj.fieldString(mField))
            edit_field.text.clear()
            edit_field.text.insert(0, obj.fieldString(mField))
        }
        text_title.text = mTitle
        text_confirm.text = "Confirm $mTitle"
        button_save.setOnClickListener {
            text_error.visibility= View.GONE
            val field_value = edit_field.text.toString()
            val confirm = edit_confirm.text.toString()
            if(field_value!=confirm){
                text_error.text = "Values must match"
                text_error.visibility=View.VISIBLE
            }else{
                if(obj is User) {
                    //SessionManager(this).user = obj.setField(mField, field_value)
                    ApiHelper(this).updateUser(obj.setField(mField,field_value))
                }
                finish()
            }
        }
    }
}