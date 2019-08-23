package com.kogero.levelcounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kogero.levelcounter.model.Relationship
import kotlinx.android.synthetic.main.activity_friends.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsActivity : Fragment() {

    val friendList: ArrayList<Relationship> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        getFriends()

        val rootView = inflater.inflate(R.layout.activity_friends, container, false)

        rv_friend_list.layoutManager = LinearLayoutManager(activity)
        rv_friend_list.adapter = FriendsAdapter(friendList, rootView.context)
        return rootView
    }

    private fun getFriends() {
        val call: Call<List<Relationship>> = ApiClient.getClient.getFriends()
        call.enqueue(object : Callback<List<Relationship>> {
            override fun onResponse(
                call: Call<List<Relationship>>,
                response: Response<List<Relationship>>
            ) {
                var relationships: List<Relationship>? = response.body()
                if (relationships != null) {
                    friendList.addAll(relationships)
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