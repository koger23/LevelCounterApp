package com.kogero.levelcounter.activites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.R
import com.kogero.levelcounter.api.ApiClient
import com.kogero.levelcounter.models.requests.UserEditRequest
import com.kogero.levelcounter.models.responses.UserResponse
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val tvFullName = findViewById<TextView>(R.id.tvFullName)
        val tvGender = findViewById<TextView>(R.id.tvGender)
        val tvRegistered = findViewById<TextView>(R.id.tvRegistered)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val btnChangeGender = findViewById<Button>(R.id.btnChangeGender)
        btnChangeGender.setOnClickListener {

        }
        val btnChangeEmail = findViewById<Button>(R.id.btnChangeEmail)
        btnChangeEmail.setOnClickListener {

        }
        val btnChangePassword = findViewById<Button>(R.id.btnChangePassword)
        btnChangePassword.setOnClickListener {

        }

        getUserData()

    }

    private fun updateUserData() {
//        var userEditRequest = UserEditRequest(
//            email = editTextEmail.text.toString(),
//            currentPassword = editTextCurrentPassword.text.toString(),
//            fullName = tvFullName.text.toString(),
//            userName = tvUserName.text.toString(),
//            newPassword = editTextNewPassword.text.toString(),
//            verifyNewPassword = editTextVerifyPassword.text.toString()
//        )
//
//        val call: Call<ResponseBody> =
//            ApiClient.getClient.updateUserData(userEditRequest)
//        call.enqueue(object : Callback<ResponseBody> {
//
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                when {
//                    response.code() == 200 -> {
//                        val responseMessage = response.body()
//                        if (responseMessage != null) {
//                            Toast.makeText(this@ProfileActivity, "responseMessage", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//                    response.code() == 401 -> {
//                        Toast.makeText(this@ProfileActivity, "Login expired.", Toast.LENGTH_SHORT)
//                            .show()
//                        ApiClient.saveToken("")
//                        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(
//                    this@ProfileActivity,
//                    "Could not connect to the server",
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
//        })
    }

    private fun getUserData() {
        val call: Call<UserResponse> =
            ApiClient.getClient.getUserData()
        call.enqueue(object : Callback<UserResponse> {

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                when {
                    response.code() == 200 -> {
                        val userData = response.body()
                        if (userData != null) {
                            tvFullName.text = userData.fullName
                            tvUserName.text = userData.userName
                            tvEmail.text = userData.email
                            tvGender.text = "Gender: ${userData.sex}"
                            tvRegistered.text = "Registered on: ${userData.registerDate.split("T")[0]}"
                        }
                    }
                    response.code() == 401 -> {
                        Toast.makeText(this@ProfileActivity, "Login expired.", Toast.LENGTH_SHORT)
                            .show()
                        ApiClient.saveToken("")
                        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}