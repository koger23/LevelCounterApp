package com.kogero.levelcounter

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.helpers.TimeConverter
import com.kogero.levelcounter.model.Game
import com.kogero.levelcounter.model.InGameUser
import com.kogero.levelcounter.model.RecyclerViewClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameActivity : AppCompatActivity() {

    internal var startMills = System.currentTimeMillis()
    internal var totalSecs: Long = 0
    internal var additionalSecs: Long = 0
    private var round = 1
    private var isFirstStart = true
    var gameId: Int = 0
    var playerList: List<InGameUser> = ArrayList()
    val adapter = GameAdapter(this, playerList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        gameId = intent.getIntExtra("GAMEID", 1)
        Toast.makeText(this@GameActivity, "game id: $gameId", Toast.LENGTH_SHORT).show()
        val tvBonus = findViewById<TextView>(R.id.tvBonusValue)
        val tvLevel = findViewById<TextView>(R.id.tvLevelValue)
        val tvBonusSetValue = findViewById<TextView>(R.id.tvBonusSetValue)
        val tvLevelSetValue = findViewById<TextView>(R.id.tvLevelSetValue)

        val tvRound = findViewById<TextView>(R.id.tvRound)
        tvRound.text = "Round $round"
        val btnNextRound = findViewById<Button>(R.id.btnNextRound)
        btnNextRound.setOnClickListener {
            round++
            tvRound.text = "Round $round"
        }

        val game = getGame(gameId)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_playerlist)
        recyclerView.layoutManager = LinearLayoutManager(this@GameActivity)
        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        val selectedPlayer = playerList[position]
                        tvBonusSetValue.text = selectedPlayer.Bonus.toString()
                        tvLevelSetValue.text = selectedPlayer.Level.toString()
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )
        if (isFirstStart) {
            startClock()
            isFirstStart = false
        }
    }

    private fun getGame(gameId: Int) {
        val call: Call<Game> = ApiClient.getClient.startGame(gameId)
        call.enqueue(object : Callback<Game> {
            override fun onResponse(
                call: Call<Game>,
                response: Response<Game>
            ) {
                val game = response.body()
                if (response.code() != 200) {
                    Toast.makeText(
                        this@GameActivity,
                        "Error while starting the game: " + response.code(),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    this@GameActivity.finish()
                }
                playerList = game!!.inGameUsers
                for (player in playerList) {
                    Toast.makeText(this@GameActivity, "playername: ${player.UserName}", Toast.LENGTH_SHORT)
                        .show()
                }
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Toast.makeText(
                    this@GameActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun getPlayers(gameId: Int) {
        val call: Call<List<InGameUser>> = ApiClient.getClient.getPlayersByGameId(gameId)
        call.enqueue(object : Callback<List<InGameUser>> {
            override fun onResponse(
                call: Call<List<InGameUser>>,
                response: Response<List<InGameUser>>
            ) {
                val players = response.body()
                if (response.code() != 200) {
                    Toast.makeText(
                        this@GameActivity,
                        "Error while fetching players" + response.code(),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    this@GameActivity.finish()
                }
                playerList = players!!
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<InGameUser>>, t: Throwable) {
                Toast.makeText(
                    this@GameActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
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