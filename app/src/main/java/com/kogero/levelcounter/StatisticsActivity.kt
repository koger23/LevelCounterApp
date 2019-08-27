package com.kogero.levelcounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.model.Statistics
import retrofit2.Call
import retrofit2.Callback
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException


class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.actitvity_playerstat)

        val userName = intent.extras.getString("USERNAME")
        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        tvUserName.text = userName

        val statistics = intent.extras.getParcelable<Statistics>("STATISTICS")

        val tvGamesPlayed = findViewById<TextView>(R.id.tvGamesPlayed)
        tvGamesPlayed.text = statistics.gamesPlayed.toString()

        val tvRoundsPlayed = findViewById<TextView>(R.id.tvRoundsPlayed)
        tvRoundsPlayed.text = statistics.roundsPlayed.toString()

        val tvWins = findViewById<TextView>(R.id.tvWins)
        tvWins.text = statistics.wins.toString()

        val tvTimePlayed = findViewById<TextView>(R.id.tvTimePlayed)
        tvTimePlayed.text = statistics.playTime.toString()

        val relationshipId = intent.extras.getInt("RELATIONSHIPID")
        val isFriend = intent.extras.getBoolean("ISFRIEND")
        val isBlocked = intent.extras.getBoolean("ISBLOCKED")

        val btnImage = findViewById<ImageView>(R.id.btnImgState)
        when {
            isFriend -> btnImage.setImageResource(R.mipmap.friend)
            isBlocked -> btnImage.setImageResource(R.mipmap.block)
            else -> {
                btnImage.setImageResource(R.mipmap.add_friend)
                btnImage.setOnClickListener {
                    makeRequest(this@StatisticsActivity, userName!!)
                    Toast.makeText(this@StatisticsActivity, "clicked", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    companion object {
        fun newInstance(): FriendsActivity = FriendsActivity()
    }

    fun makeRequest(
        context: Context,
        userName: String
    ) {
        val call: Call<ResponseBody> =
            ApiClient.getClient.makeRequest(userName)
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(
                    context,
                    "Request sent.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}