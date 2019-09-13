package com.kogero.levelcounter

import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.helpers.AppUser
import com.kogero.levelcounter.helpers.TimeConverter
import com.kogero.levelcounter.model.Game
import com.kogero.levelcounter.model.Gender
import com.kogero.levelcounter.model.InGameUser
import com.kogero.levelcounter.model.RecyclerViewClickListener
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import okhttp3.ResponseBody
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
    var gameIsRunning = true

    var game: Game? = null
    var playerList: ArrayList<InGameUser> = ArrayList()
    val adapter = GameAdapter(this, playerList)
    lateinit var hubConnection: HubConnection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // SignalR
        hubConnection = HubConnectionBuilder.create("http://05b9b6e3.ngrok.io/game").build()

        hubConnection.on("Send", { message ->
            println("Msg: $message")
        }, String::class.java)

        hubConnection.on("broadcastMessage", { message ->
            println("Msg: $message")
        }, String::class.java)

        HubConnectionTask().execute(hubConnection)


        gameId = intent.getIntExtra("GAMEID", 1)

        val tvRound = findViewById<TextView>(R.id.tvRound)
        tvRound.text = "Round $round"

        val btnNextRound = findViewById<Button>(R.id.btnNextRound)
        btnNextRound.setOnClickListener {
            round++
            tvRound.text = "Round $round"
        }

        getGame(gameId)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_playerlist)
        recyclerView.layoutManager = LinearLayoutManager(this@GameActivity)
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        adapter.selectedPosition = position
                    }

                    override fun onLongClick(view: View, position: Int) {
                        var playerGender = playerList[adapter.selectedPosition].Gender
                        playerGender = if (playerGender == Gender.MALE) {
                            Gender.FEMALE
                        } else {
                            Gender.MALE
                        }
                        playerList[adapter.selectedPosition].Gender = playerGender
                        adapter.notifyDataSetChanged()
                        updateGame()
                        getGame(gameId)
                    }
                })
        )

        if (isFirstStart) {
            startClock()
            isFirstStart = false
        }

        val btnBonusPlus = findViewById<ImageButton>(R.id.btnBonusPlus)
        btnBonusPlus.setOnClickListener {
            increaseBonus(playerList[adapter.selectedPosition])
            increaseLevel(playerList[adapter.selectedPosition])
            try {
                hubConnection.send("Send", playerList[adapter.selectedPosition].UserName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val btnBonusMinus = findViewById<ImageButton>(R.id.btnBonusMin)
        btnBonusMinus.setOnClickListener {
            decreaseBonus(playerList[adapter.selectedPosition])
        }
        val btnLevelPlus = findViewById<ImageButton>(R.id.btnLevelPlus)
        btnLevelPlus.setOnClickListener {
            increaseLevel(playerList[adapter.selectedPosition])
            try {
                hubConnection.send("Send", playerList[adapter.selectedPosition].UserName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val btnLevelMinus = findViewById<ImageButton>(R.id.btnLevelMin)
        btnLevelMinus.setOnClickListener {
            decreaseLevel(playerList[adapter.selectedPosition])
        }
    }

    private fun checkUserIsHost(): Boolean {
        if (AppUser.id == game!!.hostingUserId) {
            return true
        }
        return false
    }

    private fun increaseBonus(inGameUser: InGameUser) {
        if (adapter.selectedPosition != -1 && (checkUserIsHost() || inGameUser.UserId == AppUser.id)) {
            inGameUser.Bonus++
            adapter.notifyDataSetChanged()
        }
    }

    private fun decreaseBonus(inGameUser: InGameUser) {
        if (adapter.selectedPosition != -1 && inGameUser.Bonus > 0 && (checkUserIsHost() || inGameUser.UserId == AppUser.id)) {
            inGameUser.Bonus--
            adapter.notifyDataSetChanged()
        }
    }

    private fun increaseLevel(inGameUser: InGameUser) {
        if (adapter.selectedPosition != -1 && (checkUserIsHost() || inGameUser.UserId == AppUser.id)) {
            inGameUser.Level++
            adapter.notifyDataSetChanged()
        }
    }

    private fun decreaseLevel(inGameUser: InGameUser) {
        if (adapter.selectedPosition != -1 && inGameUser.Level > 1 && (checkUserIsHost() || inGameUser.UserId == AppUser.id)) {
            inGameUser.Level--
            adapter.notifyDataSetChanged()
        }
    }

    private fun getGame(gameId: Int) {
        val call: Call<Game> = ApiClient.getClient.startGame(gameId)
        call.enqueue(object : Callback<Game> {
            override fun onResponse(
                call: Call<Game>,
                response: Response<Game>
            ) {
                Toast.makeText(
                    this@GameActivity,
                    "Code: ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
                if (response.code() == 200) {
                    game = response.body()
                    if (game!!.inGameUsers.isNotEmpty()) {
                        playerList.clear()
                        for (player in game!!.inGameUsers) {
                            playerList.add(player)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
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

    override fun onBackPressed() {
        quitMsg()
    }

    private fun quitMsg() {
        if (checkUserIsHost()) {
            hostQuit()
        } else {
            joinedPlayerQuit()
        }
    }

    private fun joinedPlayerQuit() {
        val i = Intent(this, MainMenuActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Quit")
            .setMessage("Are you sure to quit?")
            .setNeutralButton("Yes") { _, _ ->
                startActivity(i)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun hostQuit() {
        val i = Intent(this, MainMenuActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Quit")
            .setMessage("Are you sure to quit?")
            .setPositiveButton("Save & Quit") { _, _ ->
                saveGame()
                startActivity(i)
                finish()
            }
            .setNeutralButton("Quit") { _, _ ->
                quitGame()
                startActivity(i)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun saveGame() {
        val call: Call<ResponseBody> = ApiClient.getClient.saveGame(game)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    gameIsRunning = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@GameActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun updateGame() {
        val call: Call<ResponseBody> = ApiClient.getClient.updateGame(game)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(
                        this@GameActivity,
                        "Game saved.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@GameActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun quitGame() {
        val call: Call<ResponseBody> = ApiClient.getClient.quitGame(game!!.id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    gameIsRunning = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@GameActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun startClock() {
        val t = object : Thread() {

            override fun run() {
                try {
                    while (gameIsRunning) {
                        sleep(1000)
                        runOnUiThread {
                            totalSecs =
                                (System.currentTimeMillis() - (startMills - additionalSecs * 1000)) / 1000

                            val clock = findViewById<TextView>(R.id.tvTime)
                            if (game != null) {
                                game!!.time = totalSecs
                            }
                            clock.text = TimeConverter.convert(totalSecs)
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        t.start()
    }

    inner class HubConnectionTask : AsyncTask<HubConnection, Void, Void>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg hubConnections: HubConnection): Void? {
            val hubConnection = hubConnections[0]
            hubConnection.start().blockingAwait()
            return null
        }
    }
}