package com.kogero.levelcounter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.R
import com.kogero.levelcounter.model.UserListViewModel
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
        when {
            selectedUser.isFriend -> holder.relationshipStateImgBtn.setImageResource(R.mipmap.friend)
            selectedUser.isBlocked -> holder.relationshipStateImgBtn.setImageResource(R.mipmap.block)
            selectedUser.isPending -> holder.relationshipStateImgBtn.setImageResource(R.mipmap.mailsent)
            else -> holder.relationshipStateImgBtn.setImageResource(R.mipmap.add_friend)
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
        val query = query.toLowerCase()
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
        val relationshipStateImgBtn: ImageButton = view.btnImg

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
        }

    }
}
