package com.kogero.levelcounter

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.model.requests.LoginRequest
import com.kogero.levelcounter.model.requests.SignUpRequest
import com.kogero.levelcounter.model.responses.LoginResponse
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

            if (checkInputs(username, fullName, email, password, verifyPassword)) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Signing up...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkInputs(username: String, fullName: String, email: String, password: String, verifyPassword: String): Boolean {
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