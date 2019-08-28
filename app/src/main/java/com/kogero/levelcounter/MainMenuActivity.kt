package com.kogero.levelcounter

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        val btnNewGame = findViewById<Button>(R.id.btnProfile)

        val btnJoinGame = findViewById<Button>(R.id.btnProfile)

        val btnLoadGame = findViewById<Button>(R.id.btnProfile)

        val btnStat = findViewById<Button>(R.id.btnMyStats)
        btnStat.setOnClickListener {
            val intent = Intent(this, PersonalStatisticsActivity::class.java)
            startActivity(intent)}

        val btnProfile = findViewById<Button>(R.id.btnProfile)

        val btnFriends = findViewById<Button>(R.id.btnFriends)
        btnFriends.setOnClickListener {
            val intent = Intent(this, FriendsActivity::class.java)
            startActivity(intent)}

        val btnAbout = findViewById<Button>(R.id.btnProfile)

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
                exitProcess(1)
            }
            .setNegativeButton("No", null)
            .show()
        actionBar?.hide()
    }
}