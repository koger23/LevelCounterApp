package com.kogero.levelcounter.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.R
import com.kogero.levelcounter.api.ApiClient
import com.kogero.levelcounter.models.requests.SignUpRequest
import com.kogero.levelcounter.models.responses.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.activity_signup)

        val btnSignUp = findViewById<Button>(R.id.buttonSignUp)
        btnSignUp.setOnClickListener {
            val username = findViewById<EditText>(R.id.editTextUserName).text.toString()
            val fullName = findViewById<EditText>(R.id.editTextFullName).text.toString()
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            val verifyPassword = findViewById<EditText>(R.id.editTextVerifyPassword).text.toString()
            val male = findViewById<RadioButton>(R.id.radioBtnMale).isChecked
            var gender = "female"

            if (male) {
                gender = "male"
            }

            if (checkInputs(username, fullName, email, password, verifyPassword)) {
                val signUpRequest = SignUpRequest(
                    fullName = fullName,
                    userName = username,
                    email = email,
                    password = password,
                    verifyPassword = verifyPassword,
                    gender = gender
                )
                signUp(signUpRequest)
            }
        }
    }

    private fun signUp(signUpRequest: SignUpRequest) {
        val call: Call<SignUpResponse> = ApiClient.getClient.signUp(signUpRequest)
        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                when {
                    response.code() == 201 -> {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Registration successful",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        this@SignUpActivity.finish()
                    }
                    response.code() == 400 -> Toast.makeText(
                        this@SignUpActivity,
                        "Error: ${response.body()!!.ErrorMessages}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    response.code() / 100 == 5 -> {
                        Toast.makeText(this@SignUpActivity, "Server Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Could not connect to the server.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun checkInputs(
        username: String,
        fullName: String,
        email: String,
        password: String,
        verifyPassword: String
    ): Boolean {
        if (fullName.isEmpty()) {
            Toast.makeText(
                this@SignUpActivity,
                "Full name cannot be empty.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (username.isEmpty()) {
            Toast.makeText(this@SignUpActivity, "Username cannot be empty.", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (email.isEmpty()) {
            Toast.makeText(
                this@SignUpActivity,
                "Email cannot be empty.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (password.isEmpty() || verifyPassword.isEmpty()) {
            Toast.makeText(
                this@SignUpActivity,
                "Passwords cannot be empty.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (!checkPasswords(password, verifyPassword)) {
            Toast.makeText(
                this@SignUpActivity,
                "The two password does not match.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else {
            return true
        }
    }

    private fun checkPasswords(password: String, verifyPassword: String): Boolean {

        if (password.equals(verifyPassword, ignoreCase = false)) {
            return true
        }
        return false
    }
}