package com.kogero.levelcounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.helpers.AppUser
import com.kogero.levelcounter.helpers.JWTUtils
import com.kogero.levelcounter.model.requests.LoginRequest
import com.kogero.levelcounter.model.responses.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {

    var token: String = ""
    private val appUser = AppUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSignUp = findViewById<Button>(R.id.buttonSignIn)
        btnSignUp.setOnClickListener {
            val userName = findViewById<EditText>(R.id.editTextUserName).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            val loginRequest = LoginRequest(userName = userName, password = password)
            login(loginRequest, this)
        }

        val btnLoadGame = findViewById<Button>(R.id.buttonSignUp)
        btnLoadGame.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(
        loginRequest: LoginRequest,
        context: Context
    ) {
        val call: Call<LoginResponse> = ApiClient.getClient.login(loginRequest)
        call.enqueue(object : Callback<LoginResponse> {

            override fun onResponse(
                call: Call<LoginResponse>?,
                response: Response<LoginResponse>?
            ) {
                Toast.makeText(this@MainActivity, "Code: " + response!!.code(), Toast.LENGTH_SHORT)
                    .show()
                when {
                    response.code() == 200 -> {
                        token = response.body()!!.token
                        ApiClient.saveToken(token)
                        appUser.id = JWTUtils().decode(token).toString()
                        val intent = Intent(context, MainMenuActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() == 400 -> {
                        Toast.makeText(context, "Invalid Login Attempt:\nWrong username or password", Toast.LENGTH_SHORT).show()
                    }
                    response.code() == 401 -> {
                        Toast.makeText(context, "Login Expired.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() / 100 == 5 -> {
                        Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                Toast.makeText(
                    this@MainActivity,
                    "Could not connect to the server. Are you connected to the internet?",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}