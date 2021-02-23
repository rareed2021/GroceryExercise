package com.test.groceryexercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.RegisterRequest
import com.test.groceryexercise.models.RegisterResponse
import com.test.groceryexercise.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edit_email
import kotlinx.android.synthetic.main.activity_register.edit_password
import kotlinx.android.synthetic.main.activity_register.text_error
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun init() {
        button_register.setOnClickListener {
            text_error.visibility=View.GONE
            val firstName = edit_name.text.toString()
            val email = edit_email.text.toString()
            val mobile = edit_mobile.text.toString()
            val password = edit_password.text.toString()
//            if(firstName!="" && email !="" && mobile!="" && password != ""){
                sendData(RegisterRequest(firstName,email,password,mobile))
//            }else{
//                text_error.text = getString(R.string.missing_fields)
//                text_error.visibility= View.VISIBLE
//            }
        }
    }

    private fun sendData(registerRequest: RegisterRequest) {
        val queue = Volley.newRequestQueue(this)
        val param = JSONObject(Gson().toJson(registerRequest, RegisterRequest::class.java))
        val request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.register,
            param,
            {
                val response = Gson().fromJson(it.toString(0), RegisterResponse::class.java)
                if(!response.error){
                    val session = SessionManager(this)
                    session.user = response.data
                    //val intent = Intent(this, MainActivity::class.java)
                    //intent.putExtra(User.KEY, response.data)
                    //this.startActivity(intent)
                    finish()
                }
                //Toast.makeText(this,"Register succeeded",Toast.LENGTH_SHORT).show()
            },
            {
                val s  = String(it.networkResponse.data, StandardCharsets.UTF_8)
                val response = Gson().fromJson(s, RegisterResponse::class.java)
                //Log.d("myApp","${response.error} ${response?.message}")
                if(response.error && response.message!=null){
                    text_error.visibility=View.VISIBLE
                    text_error.text = response.message.replace(",","\n")
                    //Toast.makeText(this, response?.message,Toast.LENGTH_SHORT).show()
                }
            }
        )
        queue.add(request)
    }
}