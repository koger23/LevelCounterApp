package com.kogero.levelcounter.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.api.ApiClient
import com.kogero.levelcounter.MainActivity
import com.kogero.levelcounter.R
import com.kogero.levelcounter.listeners.RecyclerViewTouchListener
import com.kogero.levelcounter.adapters.PendingRequestAdapter
import com.kogero.levelcounter.models.RecyclerViewClickListener
import com.kogero.levelcounter.models.UserListViewModel
import kotlinx.android.synthetic.main.pending_request_item.*
import okhttp3.ResponseBody
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
                            confirmRequests(
                                this@PendingRequestActivity,
                                userList[position].relationShipId,
                                position
                            )
                        }
                        btnImgDismiss.setOnClickListener {
                            dismissRequest(
                                this@PendingRequestActivity,
                                userList[position].relationShipId,
                                position
                            )
                        }
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )
        getPendingRequests(this@PendingRequestActivity)
    }

    private fun dismissRequest(context: Context, relationshipId: Int, position: Int) {
        val call: Call<ResponseBody> =
            ApiClient.getClient.dismissRequest(relationshipId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                when {
                    response.code() == 200 -> {
                        userList.remove(userList[position])
                        adapter.notifyItemRemoved(position)

                    }
                    response.code() == 401 -> {
                        Toast.makeText(context, "Login expired.", Toast.LENGTH_SHORT).show()
                        ApiClient.resetToken()
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() / 100 == 5 -> {
                        Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun confirmRequests(
        context: Context,
        relationshipId: Int,
        position: Int
    ) {
        val call: Call<ResponseBody> =
            ApiClient.getClient.confirmRequest(relationshipId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    userList.remove(userList[position])
                    adapter.notifyItemRemoved(position)

                } else if (response.code() == 401) {
                    Toast.makeText(context, "Login expired.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
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