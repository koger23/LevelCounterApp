package com.kogero.levelcounter.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.R
import com.kogero.levelcounter.adapters.LoadGameAdapter
import com.kogero.levelcounter.api.ApiClient
import com.kogero.levelcounter.listeners.RecyclerViewTouchListener
import com.kogero.levelcounter.models.Game
import com.kogero.levelcounter.models.RecyclerViewClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadGameActivity : AppCompatActivity() {
    var gameList: ArrayList<Game> = ArrayList()
    val adapter = LoadGameAdapter(this, gameList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadgame)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_load_game_list)
        recyclerView.layoutManager = LinearLayoutManager(this@LoadGameActivity)
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        val selectedGame = gameList[position]
                        val gameId = selectedGame.id
                        val intent = Intent(this@LoadGameActivity, GameActivity::class.java)
                        intent.putExtra("GAMEID", gameId)
                        startActivity(intent)
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )
        getGames()
    }

    private fun getGames() {
        val call: Call<List<Game>> = ApiClient.getClient.getSavedGames()
        call.enqueue(object : Callback<List<Game>> {
            override fun onResponse(
                call: Call<List<Game>>,
                response: Response<List<Game>>
            ) {
                when {
                    response.code() == 200 -> {
                        val games = response.body()
                        if (games!!.isNotEmpty()) {
                            gameList.clear()
                            for (player in games) {
                                gameList.add(player)
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                    response.code() == 401 -> {
                        Toast.makeText(this@LoadGameActivity, "Login Expired.", Toast.LENGTH_SHORT)
                            .show()
                        ApiClient.resetToken()
                        val intent = Intent(this@LoadGameActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() / 100 == 5 -> {
                        Toast.makeText(this@LoadGameActivity, "Server Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                Toast.makeText(
                    this@LoadGameActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}