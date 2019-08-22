package com.kogero.levelcounter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.model.requests.LoginRequest
import com.kogero.levelcounter.model.responses.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.activity_main)

        val userName = findViewById<EditText>(R.id.editTextUserName).text.toString()
        val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
        val loginRequest = LoginRequest(email = userName, password = password)

        val btnSignUp = findViewById<Button>(R.id.buttonSignIn)
        btnSignUp.setOnClickListener {
            login(loginRequest)
        }

        val btnLoadGame = findViewById<Button>(R.id.buttonSignUp)
        btnLoadGame.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(loginRequest: LoginRequest) {
        val call: Call<LoginResponse> = ApiClient.getClient.login(loginRequest)
        call.enqueue(object : Callback<LoginResponse> {

            override fun onResponse(
                call: Call<LoginResponse>?,
                response: Response<LoginResponse>?
            ) {
                Toast.makeText(this@MainActivity, "Code: " + response!!.code(), Toast.LENGTH_SHORT)
                    .show()
                token = response.body()!!.token
                Toast.makeText(this@MainActivity, "Token: $token", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                Toast.makeText(
                    this@MainActivity,
                    "Cannot connect to the server.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}