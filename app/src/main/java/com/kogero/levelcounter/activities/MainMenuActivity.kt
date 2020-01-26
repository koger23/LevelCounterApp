package com.kogero.levelcounter.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.MainActivity
import com.kogero.levelcounter.R
import kotlin.system.exitProcess

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        val btnNewGame = findViewById<Button>(R.id.btnNewGame)
        btnNewGame.setOnClickListener {
            val intent = Intent(this, NewGameSelectionActivity::class.java)
            startActivity(intent)}

        val btnJoinGame = findViewById<Button>(R.id.btnJoinGame)
        btnJoinGame.setOnClickListener {
            val intent = Intent(this, JoinGameActivity::class.java)
            startActivity(intent)}

        val btnLoadGame = findViewById<Button>(R.id.btnLoadGame)
        btnLoadGame.setOnClickListener {
            val intent = Intent(this, LoadGameActivity::class.java)
            startActivity(intent)}

        val btnStat = findViewById<Button>(R.id.btnMyStats)
        btnStat.setOnClickListener {
            val intent = Intent(this, PersonalStatisticsActivity::class.java)
            startActivity(intent)}

        val btnProfile = findViewById<Button>(R.id.btnProfile)
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)}

        val btnFriends = findViewById<Button>(R.id.btnFriends)
        btnFriends.setOnClickListener {
            val intent = Intent(this, FriendsActivity::class.java)
            startActivity(intent)}

        val btnAbout = findViewById<Button>(R.id.btnAbout)

        val btnQuit = findViewById<Button>(R.id.btnQuit)
        btnQuit.setOnClickListener {quitMsg()}
    }

    override fun onBackPressed() {
        quitMsg()
    }

    private fun quitMsg() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Quit")
            .setMessage("Are you sure to quit?")
            .setPositiveButton("Yes") { _, _ ->
                moveTaskToBack(true)
                android.os.Process.killProcess(android.os.Process.myPid())
                LoginActivity().clearUserPreferences()
                exitProcess(1)
            }
            .setNegativeButton("No", null)
            .show()
        actionBar?.hide()
    }
}