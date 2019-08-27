package com.kogero.levelcounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
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


class UsersActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private var userList: ArrayList<UserShortResponse> = ArrayList()
    private var adapter = UsersAdapter(this, userList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.acitvity_users)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_user_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val searchBar = findViewById<SearchView>(R.id.search_bar)
        searchBar.setOnSearchClickListener {adapter.filterUsers(searchBar.query.toString())}

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filterUsers(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filterUsers(query)
                return false
            }
        })

        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        val user = userList[position]
                        getStatisticsById(this@UsersActivity, user)
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )
        getAllUsers(this@UsersActivity)
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

    private fun getAllUsers(
        context: Context
    ) {
        val call: Call<List<UserShortResponse>> =
            ApiClient.getClient.getAllUsers()
        call.enqueue(object : Callback<List<UserShortResponse>> {
            override fun onResponse(
                call: Call<List<UserShortResponse>>,
                response: Response<List<UserShortResponse>>
            ) {
                Toast.makeText(
                    this@UsersActivity,
                    "Code: " + response.code(),
                    Toast.LENGTH_SHORT
                )
                    .show()
                val userResponseList: List<UserShortResponse>? = response.body()
                if (response.code() == 200) {
                    if (userResponseList != null) {
                        for (user in userResponseList) {
                            userList.add(user)
                        }
                        adapter.notifyDataSetChanged()
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Login expired.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@UsersActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<UserShortResponse>>, t: Throwable) {
                Toast.makeText(
                    this@UsersActivity,
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