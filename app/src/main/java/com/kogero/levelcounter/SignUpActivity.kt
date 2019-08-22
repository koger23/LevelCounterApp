package com.kogero.levelcounter

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnSignUp = findViewById<Button>(R.id.buttonSignUp)
        btnSignUp.setOnClickListener {
            Toast.makeText(this@SignUpActivity, "Signing up...", Toast.LENGTH_SHORT).show()
        }
    }
}