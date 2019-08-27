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
import com.kogero.levelcounter.model.UserListViewModel
import kotlinx.android.synthetic.main.actitvity_playerstat.*
import kotlinx.android.synthetic.main.pending_request_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingRequestActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private var userList: ArrayList<UserListViewModel> = ArrayList()
    private var adapter = PendingRequestAdapter(this, userList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.activity_pending_requests)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_user_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        btnImgOk.setOnClickListener {
                            Toast.makeText(this@PendingRequestActivity, "OK!", Toast.LENGTH_SHORT).show()
                        }
                        btnImgDismiss.setOnClickListener {
                            Toast.makeText(this@PendingRequestActivity, "Not OK!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )

        getPendingRequests(this@PendingRequestActivity)
    }

    private fun getPendingRequests(
        context: Context
    ) {
        val call: Call<List<UserListViewModel>> =
            ApiClient.getClient.getPendingRequests()
        call.enqueue(object : Callback<List<UserListViewModel>> {
            override fun onResponse(
                call: Call<List<UserListViewModel>>,
                response: Response<List<UserListViewModel>>
            ) {
                Toast.makeText(
                    context,
                    "Code: " + response.code(),
                    Toast.LENGTH_SHORT
                )
                    .show()
                val pendingRequests: List<UserListViewModel>? = response.body()
                if (response.code() == 200) {
                    if (pendingRequests != null) {
                        userList.clear()
                        userList.addAll(pendingRequests)
                        adapter.notifyDataSetChanged()
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Login expired.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<UserListViewModel>>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}