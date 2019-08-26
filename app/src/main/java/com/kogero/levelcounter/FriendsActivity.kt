package com.kogero.levelcounter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.model.responses.UserShortResponse
import kotlinx.android.synthetic.main.activity_friends.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.kogero.levelcounter.model.RecyclerViewClickListener




class FriendsActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(this, "WTF", Toast.LENGTH_SHORT).show()
    }

    val friendList: ArrayList<UserShortResponse> = ArrayList()
    var adapter = FriendsAdapter(this, friendList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.activity_friends)

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
                        Toast.makeText(
                            applicationContext,
                            friendList[position].userName + " is clicked!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onLongClick(view: View, position: Int) {
                        Toast.makeText(
                            applicationContext,
                            friendList[position].userName + " is long pressed!",
                            Toast.LENGTH_SHORT
                        ).show()

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
                val userDatas: List<UserShortResponse>? = response.body()
                if (response.code() == 200) {
                    println(response.body().toString())
                    if (userDatas != null) {
                        for (userData in userDatas) {
                            friendList.add(userData)
                            println("user id: " + userData.userName)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<UserShortResponse>>, t: Throwable) {
                Toast.makeText(this@FriendsActivity, "Could not connect to the server", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    companion object {
        fun newInstance(): FriendsActivity = FriendsActivity()
    }

}