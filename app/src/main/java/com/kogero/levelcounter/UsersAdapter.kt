package com.kogero.levelcounter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.model.UserListViewModel
import kotlinx.android.synthetic.main.friend_list_item.view.*
import kotlinx.android.synthetic.main.friend_list_item.view.friendName
import kotlinx.android.synthetic.main.user_list_item.view.*


class UsersAdapter(
    private val context: Context,
    private val userList: ArrayList<UserListViewModel>,
    private val userFullList: ArrayList<UserListViewModel> = ArrayList()
) :
    RecyclerView.Adapter<UsersAdapter.FriendViewHolder>() {

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val selectedUser = userList[position]
        holder.friendName.text = selectedUser.userName
        if (selectedUser.isFriend) {
            holder.relationshipState.setImageResource(R.mipmap.friend)
        } else if (selectedUser.isBlocked) {
            holder.relationshipState.setImageResource(R.mipmap.block)
        } else {
            holder.relationshipState.setImageResource(R.mipmap.add_friend)
        }
        if (userFullList.size == 0) {
            userFullList.addAll(userList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.user_list_item, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun filterUsers(query: String): List<UserListViewModel> {
        var query = query.toLowerCase()
        userList.clear()
        for (user in userFullList) {
            if (user.userName.toLowerCase().contains(query)) {
                userList.add(user)
            }
        }
        if (userList.isEmpty()) {
            Toast.makeText(this.context, "No match", Toast.LENGTH_SHORT).show()
        }
        notifyDataSetChanged()
        return userList
    }

    inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val friendName: TextView = view.friendName
        val relationshipState: ImageButton = view.btnImg_relationship_state

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
        }

    }
}
