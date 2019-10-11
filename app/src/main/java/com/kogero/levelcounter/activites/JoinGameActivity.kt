package com.kogero.levelcounter.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.api.ApiClient
import com.kogero.levelcounter.R
import com.kogero.levelcounter.listeners.RecyclerViewTouchListener
import com.kogero.levelcounter.adapters.LoadGameAdapter
import com.kogero.levelcounter.models.Game
import com.kogero.levelcounter.models.RecyclerViewClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinGameActivity : AppCompatActivity() {
    var gameList: ArrayList<Game> = ArrayList()
    val adapter = LoadGameAdapter(this, gameList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadgame)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_load_game_list)
        recyclerView.layoutManager = LinearLayoutManager(this@JoinGameActivity)
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        val selectedGame = gameList[position]
                        val gameId = selectedGame.id
                        joinGame(gameId)
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )
        getGames()
    }

    fun joinGame(gameId: Int) {
        val call: Call<Game> = ApiClient.getClient.joinGame(gameId)
        call.enqueue(object : Callback<Game> {
            override fun onResponse(
                call: Call<Game>,
                response: Response<Game>
            ) {
                Toast.makeText(
                    this@JoinGameActivity,
                    "Code: ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
                val game = response.body()
                if (game != null) {
                    val intent = Intent(this@JoinGameActivity, GameActivity::class.java)
                    intent.putExtra("GAMEID", game.id)
                    intent.putExtra("JOIN", 1)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Toast.makeText(
                    this@JoinGameActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun getGames() {

        val call: Call<List<Game>> = ApiClient.getClient.getJoinableGames()
        call.enqueue(object : Callback<List<Game>> {
            override fun onResponse(
                call: Call<List<Game>>,
                response: Response<List<Game>>
            ) {
                Toast.makeText(
                    this@JoinGameActivity,
                    "Code: ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
                val games = response.body()
                if (games!!.isNotEmpty()) {
                    gameList.clear()
                    for (player in games) {
                        gameList.add(player)
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                Toast.makeText(
                    this@JoinGameActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}