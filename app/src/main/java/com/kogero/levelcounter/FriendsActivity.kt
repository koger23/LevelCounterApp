package com.kogero.levelcounter

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.model.Relationship
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FriendsActivity : AppCompatActivity() {

    val friendList: ArrayList<Relationship> = ArrayList()
    var adapter = FriendsAdapter(this, friendList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.activity_friends)

        getFriends()

        for (relationship in friendList) {
            println("id: ${relationship.relatingUserId}")
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv_friend_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }


    private fun getFriends() {
        val call: Call<List<Relationship>> = ApiClient.getClient.getFriends()
        call.enqueue(object : Callback<List<Relationship>> {
            override fun onResponse(
                call: Call<List<Relationship>>,
                response: Response<List<Relationship>>
            ) {
                Toast.makeText(this@FriendsActivity, "Code: " + response!!.code(), Toast.LENGTH_SHORT)
                    .show()
                val relationships: List<Relationship>? = response.body()
                if (response.code() == 200) {
                    println(response.body().toString())
                    if (relationships != null) {
                        for (relationship in relationships) {
                            friendList.add(relationship)
                            println("user id: " + relationship.relatingUserId)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<Relationship>>, t: Throwable) {

            }
        })
    }

    companion object {
        fun newInstance(): FriendsActivity = FriendsActivity()
    }

}