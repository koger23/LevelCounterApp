package com.kogero.levelcounter

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.helpers.TimeConverter

class GameActivity : AppCompatActivity() {

    internal var startMills = System.currentTimeMillis()
    internal var totalSecs: Long = 0
    internal var additionalSecs: Long = 0
    private var round = 1
    private var isFirstStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val tvRound = findViewById<TextView>(R.id.tvRound)
        tvRound.text = "Round $round"
        val btnNextRound = findViewById<Button>(R.id.btnNextRound)
        btnNextRound.setOnClickListener {
            round++
            tvRound.text = "Round $round"
        }

        if (isFirstStart) startClock()
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
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun startClock() {
        val t = object : Thread() {

            override fun run() {
                try {
                    while (!isInterrupted) {
                        sleep(1000)
                        runOnUiThread {
                            totalSecs =
                                (System.currentTimeMillis() - (startMills - additionalSecs * 1000)) / 1000

                            val clock = findViewById<TextView>(R.id.tvTime)
                            clock.text = TimeConverter.convert(totalSecs)
                            println()
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        t.start()
    }
}