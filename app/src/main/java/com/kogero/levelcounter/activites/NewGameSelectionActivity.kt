package com.kogero.levelcounter.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.api.ApiClient
import com.kogero.levelcounter.R
import com.kogero.levelcounter.listeners.RecyclerViewTouchListener
import com.kogero.levelcounter.adapters.NewGameSelectionAdapter
import com.kogero.levelcounter.models.Game
import com.kogero.levelcounter.models.RecyclerViewClickListener
import com.kogero.levelcounter.models.UserListViewModel
import com.kogero.levelcounter.models.UserSelectionModel
import com.kogero.levelcounter.models.requests.InGameUserCreationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewGameSelectionActivity : AppCompatActivity() {

    private var viewModels = ArrayList<UserSelectionModel>()
    private val friendList: ArrayList<UserListViewModel> = ArrayList()
    var adapter = NewGameSelectionAdapter(this, viewModels)
    var progressBar: ProgressBar? = null

    override fun onRestart() {
        super.onRestart()
        friendList.clear()
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitvity_userselection)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_selectfriends)
        recyclerView.layoutManager = LinearLayoutManager(this@NewGameSelectionActivity)
        recyclerView.adapter = adapter
        friendList.clear()
        getFriends()

        progressBar = findViewById<ProgressBar>(R.id.pbGameStart)
        progressBar!!.visibility = View.INVISIBLE

        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        val userModel = viewModels[position]
                        userModel.isSelected = !userModel.isSelected
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )
        val btnStartGame = findViewById<Button>(R.id.btnStartGame)
        btnStartGame.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            createGame()
        }
    }

    private fun createGame() {
        val call: Call<Game> = ApiClient.getClient.createGame()
        call.enqueue(object : Callback<Game> {
            override fun onResponse(
                call: Call<Game>,
                response: Response<Game>
            ) {
                val game: Game? = response.body()
                if (response.code() == 200) {
                    if (game != null) {
                        Toast.makeText(this@NewGameSelectionActivity, "Time:" + game.dateTime, Toast.LENGTH_LONG)
                            .show()
                        addInGameUsers(game.id)
                    } else {
                        Toast.makeText(this@NewGameSelectionActivity, "Game with not exists" + response.code(), Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(this@NewGameSelectionActivity, "Error when creating game: ${response.code()}", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Toast.makeText(
                    this@NewGameSelectionActivity,
                    "Could not connect to the server when try create game",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun addInGameUsers(gameId: Int) {
        val call: Call<Game> = ApiClient.getClient.addInGameUsers(createInGameRequest(gameId))
        call.enqueue(object : Callback<Game> {
            override fun onResponse(
                call: Call<Game>,
                response: Response<Game>
            ) {
                val game: Game? = response.body()
                if (response.code() == 200) {
                    if (game != null) {
                        val intent = Intent(this@NewGameSelectionActivity, GameActivity::class.java)
                        intent.putExtra("GAMEID", game.id)
                        intent.putExtra("JOIN", 0)
                        progressBar!!.visibility = View.INVISIBLE
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@NewGameSelectionActivity, "Game with not exists" + response.code(), Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(this@NewGameSelectionActivity, "Error while adding players: " + response.code(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Toast.makeText(
                    this@NewGameSelectionActivity,
                    "Could not connect to the server when try to add players",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun createInGameRequest(gameId: Int): InGameUserCreationRequest {
        return InGameUserCreationRequest(gameId, getSelectedPlayerNames())
    }

    private fun getSelectedPlayerNames(): ArrayList<String> {
        val selectedPlayerNames = ArrayList<String>()
        for (playerName in viewModels) {
            if (playerName.isSelected) {
                selectedPlayerNames.add(playerName.user.userName)
            }
        }
        return selectedPlayerNames
    }

    private fun getFriends() {
        val call: Call<List<UserListViewModel>> = ApiClient.getClient.getFriends()
        call.enqueue(object : Callback<List<UserListViewModel>> {
            override fun onResponse(
                call: Call<List<UserListViewModel>>,
                response: Response<List<UserListViewModel>>
            ) {
                val userData: List<UserListViewModel>? = response.body()
                if (response.code() == 200) {
                    if (userData != null) {
                        friendList.clear()
                        for (udata in userData) {
                            friendList.add(udata)
                        }
                        for (player in friendList) {
                            viewModels.add(UserSelectionModel(user = player))
                        }
                        adapter.notifyDataSetChanged()
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(this@NewGameSelectionActivity, "Login expired.", Toast.LENGTH_SHORT)
                        .show()
                    ApiClient.resetToken()
                    val intent = Intent(this@NewGameSelectionActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@NewGameSelectionActivity, "Error while getting friends: " + response.code(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<UserListViewModel>>, t: Throwable) {
                Toast.makeText(
                    this@NewGameSelectionActivity,
                    "Could not connect to the server while getting friend list",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}