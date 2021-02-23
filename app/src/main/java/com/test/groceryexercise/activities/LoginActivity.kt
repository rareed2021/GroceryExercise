package com.test.groceryexercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.groceryexercise.R
import com.test.groceryexercise.app.Endpoints
import com.test.groceryexercise.app.SessionManager
import com.test.groceryexercise.models.LoginRequest
import com.test.groceryexercise.models.LoginResponse
import com.test.groceryexercise.models.RegisterResponse
import com.test.groceryexercise.models.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edit_email
import kotlinx.android.synthetic.main.activity_login.edit_password
import kotlinx.android.synthetic.main.activity_login.text_error
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        text_register.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        button_login.setOnClickListener {
            text_error.visibility = View.GONE
            val email = edit_email.text.toString()
            val password = edit_password.text.toString()
            val login = LoginRequest(email, password)
            handleRequest(login)
        }
    }

    private fun handleRequest(login: LoginRequest) {
        val params = JSONObject(Gson().toJson(login, LoginRequest::class.java))
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.login,
            params,
            {
                val response = Gson().fromJson(it.toString(0),LoginResponse::class.java)
                val user = response?.user
                if(user!=null){
                    val session = SessionManager(this)
                    session.user = user
                    //val intent = Intent(this, MainActivity::class.java)
//                    intent.putExtra(User.KEY, user)
                    //startActivity(intent)
                    finish()
                }
            },
            {
                val s = String(it.networkResponse.data, StandardCharsets.UTF_8)
                val response = Gson().fromJson(s, LoginResponse::class.java)
                //Log.d("myApp", "${response.error} ${response?.message}")
                if (response.error != null && response.message != null) {
                    text_error.visibility = View.VISIBLE
                    text_error.text = response.message.replace(",", "\n")
                }
            }
        )
        queue.add(request)
    }
}
