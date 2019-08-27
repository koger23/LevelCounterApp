package com.kogero.levelcounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.model.RecyclerViewClickListener
import com.kogero.levelcounter.model.Statistics
import com.kogero.levelcounter.model.UserListViewModel
import com.kogero.levelcounter.model.responses.UserShortResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


open class UsersActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private var userList: ArrayList<UserListViewModel> = ArrayList()
    private var adapter = UsersAdapter(this, userList)
    private var user: UserListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.acitvity_users)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_user_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        user = userList[position]
                        if (user != null) {
                            getStatisticsById(this@UsersActivity, user!!)
                        }
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )
        val searchBar = findViewById<SearchView>(R.id.search_bar)
        searchBar.setOnSearchClickListener { adapter.filterUsers(searchBar.query.toString()) }
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                adapter.filterUsers(query)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filterUsers(query)
                return false
            }
        })
        getAllUsers(this@UsersActivity)
    }

    private fun getStatisticsById(
        context: Context,
        userShortResponse: UserListViewModel
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
                        intent.putExtra("ISFRIEND", userShortResponse.isFriend)
                        intent.putExtra("ISBLOCKED", userShortResponse.isBlocked)
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
        val call: Call<List<UserListViewModel>> =
            ApiClient.getClient.getAllUsers()
        call.enqueue(object : Callback<List<UserListViewModel>> {
            override fun onResponse(
                call: Call<List<UserListViewModel>>,
                response: Response<List<UserListViewModel>>
            ) {
                Toast.makeText(
                    this@UsersActivity,
                    "Code: " + response.code(),
                    Toast.LENGTH_SHORT
                )
                    .show()
                val userResponseList: List<UserListViewModel>? = response.body()
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

            override fun onFailure(call: Call<List<UserListViewModel>>, t: Throwable) {
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