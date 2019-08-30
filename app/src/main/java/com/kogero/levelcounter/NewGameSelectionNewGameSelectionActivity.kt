package com.kogero.levelcounter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.model.RecyclerViewClickListener
import com.kogero.levelcounter.model.UserListViewModel
import com.kogero.levelcounter.model.UserSelectionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewGameSelectionActivity : AppCompatActivity() {

    private var viewModels = ArrayList<UserSelectionModel>()
    private val friendList: ArrayList<UserListViewModel> = ArrayList()
    var adapter = NewGameSelectionAdapter(this, viewModels)

    override fun onRestart() {
        super.onRestart()
        getFriends()
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitvity_userselection)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_selectfriends)
        recyclerView.layoutManager = LinearLayoutManager(this@NewGameSelectionActivity)
        recyclerView.adapter = adapter
        getFriends()

        val checkBox = findViewById<CheckBox>(R.id.cbSelection)
        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        val userModel = viewModels[position]
                        userModel.isSelected = !userModel.isSelected
                        Toast.makeText(this@NewGameSelectionActivity, "${userModel.isSelected}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )
    }

    private fun getFriends() {
        val call: Call<List<UserListViewModel>> = ApiClient.getClient.getFriends()
        call.enqueue(object : Callback<List<UserListViewModel>> {
            override fun onResponse(
                call: Call<List<UserListViewModel>>,
                response: Response<List<UserListViewModel>>
            ) {
                Toast.makeText(this@NewGameSelectionActivity, "Code: " + response.code(), Toast.LENGTH_SHORT)
                    .show()
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
                    val intent = Intent(this@NewGameSelectionActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<UserListViewModel>>, t: Throwable) {
                Toast.makeText(
                    this@NewGameSelectionActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}