package com.kogero.levelcounter

import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.model.Statistics


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
    }

    companion object {
        fun newInstance(): FriendsActivity = FriendsActivity()
    }
}