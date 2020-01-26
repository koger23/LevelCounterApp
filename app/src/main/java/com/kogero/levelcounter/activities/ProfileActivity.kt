package com.kogero.levelcounter.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.R
import com.kogero.levelcounter.api.ApiClient
import com.kogero.levelcounter.models.requests.PasswordChangeRequest
import com.kogero.levelcounter.models.responses.UserResponse
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var currentPassword: EditText
    private lateinit var newPassword: EditText
    private lateinit var verifyPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        val changePasswordDialog = Dialog(this)

        val btnChangeGender = findViewById<Button>(R.id.btnChangeGender)
        btnChangeGender.setOnClickListener {
        }
        val btnChangeEmail = findViewById<Button>(R.id.btnChangeEmail)
        btnChangeEmail.setOnClickListener {
        }
        val btnChangePassword = findViewById<Button>(R.id.btnChangePassword)
        btnChangePassword.setOnClickListener {
            changePasswordPopup(changePasswordDialog)
        }

        getUserData()
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
                            tvRegistered.text =
                                "Registered on: ${userData.registerDate.split("T")[0]}"
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

    private fun changePasswordPopup(changePasswordDialog: Dialog) {
        val popupView = layoutInflater.inflate(R.layout.popup_change_password, null)

        currentPassword =  findViewById(R.id.editTextCurrentPassword)
        newPassword = findViewById(R.id.editTextNewPassword)
        verifyPassword = findViewById(R.id.editTextVerifyPassword)

        changePasswordDialog.setContentView(popupView)
        changePasswordDialog.show()

        val btnCancel = popupView.findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            changePasswordDialog.dismiss()
        }
        val btnChange = popupView.findViewById<Button>(R.id.btnChange)
        btnChange.setOnClickListener {
            changePassword(currentPassword.text.toString(), newPassword.text.toString(), verifyPassword.text.toString())
            changePasswordDialog.dismiss()
        }
    }

    private fun checkPasswordFields(
        currentPassword: String,
        newPassword: String,
        verifyPassword: String
    ): Boolean {
        if (currentPassword.isNotEmpty() && newPassword.isNotEmpty() && verifyPassword.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun changePassword(currentPassword: String, newPassword: String, verifyPassword: String) {
        if (!checkPasswordFields(currentPassword, newPassword, verifyPassword)) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val passwordChangeRequest = PasswordChangeRequest(
            currentPassword,
            newPassword,
            verifyPassword
        )
        val call: Call<ResponseBody> =
            ApiClient.getClient.changePassword(passwordChangeRequest)
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when {
                    response.code() == 200 -> {
                        val message = response.body()
                        Toast.makeText(this@ProfileActivity, message.toString(), Toast.LENGTH_LONG)
                            .show()
                        ApiClient.saveToken("")
                        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() == 400 -> {
                        val errors = response.body()
                        Toast.makeText(this@ProfileActivity, errors.toString(), Toast.LENGTH_LONG)
                            .show()
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

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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