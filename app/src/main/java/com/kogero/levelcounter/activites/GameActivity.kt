package com.kogero.levelcounter.activites

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kogero.levelcounter.R
import com.kogero.levelcounter.adapters.GameAdapter
import com.kogero.levelcounter.api.ApiClient
import com.kogero.levelcounter.helpers.AppUser
import com.kogero.levelcounter.helpers.TimeConverter
import com.kogero.levelcounter.hub.HubConnectionTask
import com.kogero.levelcounter.listeners.RecyclerViewTouchListener
import com.kogero.levelcounter.models.Game
import com.kogero.levelcounter.models.Gender
import com.kogero.levelcounter.models.InGameUser
import com.kogero.levelcounter.models.RecyclerViewClickListener
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import kotlinx.android.synthetic.main.activity_game.*
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
    private var addedToGroup = false
    private var gameId: Int = 0
    var gameIsRunning = true

    var game: Game? = null
    var joinGame = 0
    var playerList: ArrayList<InGameUser> = ArrayList()
    val adapter = GameAdapter(this, playerList)
    private val gson = Gson()
    private lateinit var hubConnection: HubConnection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        hubConnection = HubConnectionBuilder.create("${ApiClient.SITE_URL}game").build()
        hubConnection.on("Send", {}, String::class.java)
        hubConnection.on("broadcastMessage", { message ->
            val gameFromMsg = gson.fromJson(message, Game::class.java)
            this@GameActivity.runOnUiThread {
                if (gameFromMsg.senderId != AppUser.id) {
                    updateGame(gameFromMsg)
                }
            }
        }, String::class.java)

        val connection = HubConnectionTask().execute(hubConnection)

        gameId = intent.getIntExtra("GAMEID", 1)
        joinGame = intent.getIntExtra("JOIN", 0)

        getGame(gameId)

        val tvRound = findViewById<TextView>(R.id.tvRound)
        tvRound.text = "Round $round"

        val btnNextRound = findViewById<Button>(R.id.btnNextRound)
        btnNextRound.setOnClickListener {
            round++
            tvRound.text = "Round $round"
            sendGameStateWithSignalR()
        }

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
                        sendGameStateWithSignalR()
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
        }
        val btnBonusMinus = findViewById<ImageButton>(R.id.btnBonusMin)
        btnBonusMinus.setOnClickListener {
            decreaseBonus(playerList[adapter.selectedPosition])
        }
        val btnLevelPlus = findViewById<ImageButton>(R.id.btnLevelPlus)
        btnLevelPlus.setOnClickListener {
            increaseLevel(playerList[adapter.selectedPosition])
        }
        val btnLevelMinus = findViewById<ImageButton>(R.id.btnLevelMin)
        btnLevelMinus.setOnClickListener {
            decreaseLevel(playerList[adapter.selectedPosition])
        }
    }

    private fun updateRounds(rounds: Int) {
        round = rounds
        tvRound.text = "Round $round"
    }

    private fun sendGameStateWithSignalR() {
        if (!addedToGroup) {
            try {
                hubConnection.send("AddToGroup", gameId, AppUser.id)
                addedToGroup = false
            } catch (e: RuntimeException) {
                Toast.makeText(
                    this@GameActivity,
                    "Socket connection is not active.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        try {
            game!!.senderId = AppUser.id
            game!!.rounds = round
            try {
                hubConnection.send("Send", gson.toJson(game), gameId)
            } catch (e: RuntimeException) {
                Toast.makeText(
                    this@GameActivity,
                    "Socket connection is not active.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateGame(refreshGame: Game) {
        if (!refreshGame.isRunning && refreshGame.id == gameId) {
            hostExitedMsq()
        }
        if (refreshGame.id == gameId) {
            game = refreshGame
            if (game != null && game?.inGameUsers!!.isNotEmpty()) {
                playerList.clear()
                totalSecs = game!!.time
            }
            for (player in refreshGame.inGameUsers) {
                playerList.add(player)
            }
            val gameStartMills = TimeConverter.convertDateToLong(
                TimeConverter.fromStringToDate(
                    game!!.dateTime,
                    "yyyy-MM-dd'T'HH:mm:ss"
                )
            )
            updateRounds(refreshGame.rounds)
            startMills = gameStartMills
            adapter.notifyDataSetChanged()
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
            sendGameStateWithSignalR()
        }
    }

    private fun decreaseBonus(inGameUser: InGameUser) {
        if (adapter.selectedPosition != -1 && inGameUser.Bonus > 0 && (checkUserIsHost() || inGameUser.UserId == AppUser.id)) {
            inGameUser.Bonus--
            adapter.notifyDataSetChanged()
            sendGameStateWithSignalR()
        }
    }

    private fun increaseLevel(inGameUser: InGameUser) {
        if (adapter.selectedPosition != -1 && (checkUserIsHost() || inGameUser.UserId == AppUser.id)) {
            inGameUser.Level++
            adapter.notifyDataSetChanged()
            sendGameStateWithSignalR()
        }
    }

    private fun decreaseLevel(inGameUser: InGameUser) {
        if (adapter.selectedPosition != -1 && inGameUser.Level > 1 && (checkUserIsHost() || inGameUser.UserId == AppUser.id)) {
            inGameUser.Level--
            adapter.notifyDataSetChanged()
            sendGameStateWithSignalR()
        }
    }

    private fun getGame(gameId: Int) {
        val call: Call<Game> = if (joinGame == 1) {
            ApiClient.getClient.joinGame(gameId)
        } else {
            ApiClient.getClient.startGame(gameId)
        }

        call.enqueue(object : Callback<Game> {
            override fun onResponse(
                call: Call<Game>,
                response: Response<Game>
            ) {
                when {
                    response.code() == 200 -> {
                        game = response.body()
                        updateRounds(game!!.rounds)
                        if (game!!.hostingUserId != AppUser.id) {
                            btnNextRound.visibility = View.INVISIBLE
                        }
                        if (game!!.inGameUsers.isNotEmpty()) {
                            playerList.clear()
                            for (player in game!!.inGameUsers) {
                                playerList.add(player)
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                    response.code() == 401 -> {
                        Toast.makeText(this@GameActivity, "Login Expired.", Toast.LENGTH_SHORT)
                            .show()
                        ApiClient.resetToken()
                        val intent = Intent(this@GameActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() / 100 == 5 -> {
                        Toast.makeText(this@GameActivity, "Server Error", Toast.LENGTH_SHORT).show()
                    }
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
                game!!.isRunning = false
                saveGame()
                sendGameStateWithSignalR()
                startActivity(i)
                finish()
            }
            .setNeutralButton("Quit") { _, _ ->
                game!!.isRunning = false
                quitGame()
                sendGameStateWithSignalR()
                startActivity(i)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }


    private fun hostExitedMsq() {
        val i = Intent(this, MainMenuActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Connection lost with the Host.")
            .setMessage("The host closed the game.")
            .setNeutralButton("OK") { _, _ ->
                gameIsRunning = false
                startActivity(i)
                finish()
            }
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

    private fun updateGameOnServer() {
        game!!.rounds = round
        val call: Call<ResponseBody> = ApiClient.getClient.updateGame(game)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
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
        val call: Call<ResponseBody> = ApiClient.getClient.quitGame(game!!)
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
                            if (totalSecs % 60 == 0L && game!!.hostingUserId == AppUser.id) {
                                game!!.senderId = AppUser.id
                                updateGameOnServer()
                            }
                            clock.text = TimeConverter.convertTimeFromLong(totalSecs)
                        }
                    }
                } catch (e: InterruptedException) {
                    println(e.stackTrace)
                }
            }
        }
        t.start()
        if (!gameIsRunning) {
            t.interrupt()
        }
    }
}