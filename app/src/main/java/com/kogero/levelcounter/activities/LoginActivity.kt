package com.kogero.levelcounter.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.R
import com.kogero.levelcounter.api.ApiClient
import com.kogero.levelcounter.helpers.AppUser
import com.kogero.levelcounter.helpers.JWTUtils
import com.kogero.levelcounter.models.requests.LoginRequest
import com.kogero.levelcounter.models.responses.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    var token: String = ""
    private val appUser = AppUser
    private val PREF_NAME = "preferences"
    private val PREF_UNAME = "Username"
    private val PREF_PASSWORD = "Password"
    private val DefaultUnameValue = ""
    private var UnameValue: String? = null
    private val DEFAULT_PASSWORD = ""
    private var PasswordValue: String? = null
    private val PREF_TOKEN: String = "token"
    private var defaultTokenValue: String = ""
    private val PREF_USER_ID: String = "userId"
    private var defaultUserId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSignUp = findViewById<Button>(R.id.buttonSignIn)
        btnSignUp.setOnClickListener {
            val userName = findViewById<EditText>(R.id.editTextUserName).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            val loginRequest = LoginRequest(userName, password)
            login(loginRequest, this)
        }

        val btnLoadGame = findViewById<Button>(R.id.buttonSignUp)
        btnLoadGame.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        loadTokenAndUserId()
        if (ApiClient.token.isNotEmpty() && AppUser.id.isNotEmpty()) {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onPause() {
        super.onPause()
        savePreferences()

    }

    public override fun onResume() {
        super.onResume()
        loadPreferences()
    }

    private fun saveUserId() {
        val settings = getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        val editor = settings.edit()
        editor.putString(PREF_USER_ID, appUser.id)
        editor.apply()
    }

    private fun savePreferences() {
        val settings = getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        val editor = settings.edit()

        UnameValue = findViewById<EditText>(R.id.editTextUserName).text.toString()
        PasswordValue = findViewById<EditText>(R.id.editTextPassword).text.toString()
        editor.putString(PREF_UNAME, UnameValue)
        editor.putString(PREF_PASSWORD, PasswordValue)
        editor.putString(PREF_TOKEN, ApiClient.token)
        editor.apply()
    }

    fun clearUserPreferences() {
        val settings = getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        val editor = settings.edit()

        UnameValue = findViewById<EditText>(R.id.editTextUserName).text.toString()
        PasswordValue = findViewById<EditText>(R.id.editTextPassword).text.toString()
        editor.putString(PREF_UNAME, "")
        editor.putString(PREF_PASSWORD, "")
        editor.putString(PREF_TOKEN, "")
        editor.apply()
    }

    private fun loadTokenAndUserId() {
        val settings = getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        val savedToken = settings.getString(PREF_TOKEN, defaultTokenValue)
        ApiClient.token = savedToken!!
        val savedUserId = settings.getString(PREF_USER_ID, defaultUserId)
        AppUser.id = savedUserId!!
    }

    private fun loadPreferences() {

        val settings = getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue)
        PasswordValue = settings.getString(PREF_PASSWORD, DEFAULT_PASSWORD)
        val userNameField = findViewById<EditText>(R.id.editTextUserName)
        val passwordField = findViewById<EditText>(R.id.editTextPassword)
        userNameField.setText(UnameValue)
        passwordField.setText(PasswordValue)
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
                Toast.makeText(this@LoginActivity, "Code: " + response!!.code(), Toast.LENGTH_SHORT)
                    .show()
                when {
                    response.code() == 200 -> {
                        token = response.body()!!.token
                        ApiClient.saveToken(token)
                        appUser.id = JWTUtils().decode(token).toString()
                        saveUserId()
                        savePreferences()
                        val intent = Intent(context, MainMenuActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() == 400 -> {
                        Toast.makeText(
                            context,
                            "Invalid Login Attempt:\nWrong username or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    response.code() == 401 -> {
                        Toast.makeText(context, "Login Expired.", Toast.LENGTH_SHORT).show()
                        ApiClient.resetToken()
                        clearUserPreferences()
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() / 100 == 5 -> {
                        Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                Toast.makeText(
                    this@LoginActivity,
                    "Could not connect to the server. Are you connected to the internet?",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}