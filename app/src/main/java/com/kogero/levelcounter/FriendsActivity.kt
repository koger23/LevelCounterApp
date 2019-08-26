package com.kogero.levelcounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.model.RecyclerViewClickListener
import com.kogero.levelcounter.model.Statistics
import com.kogero.levelcounter.model.responses.UserShortResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FriendsActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private val friendList: ArrayList<UserShortResponse> = ArrayList()
    var adapter = UserShortResponseAdapter(this, friendList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.activity_friends)

        val btnSearch = findViewById<ImageButton>(R.id.btnSearch)
        btnSearch.setOnClickListener {
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }

        getFriends()

        val recyclerView = findViewById<RecyclerView>(R.id.rv_friend_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        val user = friendList[position]
                        getStatisticsById(this@FriendsActivity, user)
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )
    }

    private fun getFriends() {
        val call: Call<List<UserShortResponse>> = ApiClient.getClient.getFriends()
        call.enqueue(object : Callback<List<UserShortResponse>> {
            override fun onResponse(
                call: Call<List<UserShortResponse>>,
                response: Response<List<UserShortResponse>>
            ) {
                Toast.makeText(this@FriendsActivity, "Code: " + response.code(), Toast.LENGTH_SHORT)
                    .show()
                val userData: List<UserShortResponse>? = response.body()
                if (response.code() == 200) {
                    if (userData != null) {
                        for (userData in userData) {
                            friendList.add(userData)
                        }
                        adapter.notifyDataSetChanged()
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(this@FriendsActivity, "Login expired.", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@FriendsActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<UserShortResponse>>, t: Throwable) {
                Toast.makeText(
                    this@FriendsActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun getStatisticsById(
        context: Context,
        userShortResponse: UserShortResponse
    ) {
        val call: Call<Statistics> =
            ApiClient.getClient.getStatisticsById(userShortResponse.statisticsId)
        call.enqueue(object : Callback<Statistics> {
            override fun onResponse(
                call: Call<Statistics>,
                response: Response<Statistics>
            ) {
                Toast.makeText(
                    context,
                    "Code: " + response.code(),
                    Toast.LENGTH_SHORT
                )
                    .show()
                val statistics: Statistics? = response.body()
                if (response.code() == 200) {
                    if (statistics != null) {
                        val intent = Intent(context, StatisticsActivity::class.java)
                        intent.putExtra("STATISTICS", statistics)
                        intent.putExtra("USERNAME", userShortResponse.userName)
                        startActivity(intent)
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Login expired.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Statistics>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    companion object {
        fun newInstance(): FriendsActivity = FriendsActivity()
    }
}