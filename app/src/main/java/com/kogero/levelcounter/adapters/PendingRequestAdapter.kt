package com.kogero.levelcounter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.R
import com.kogero.levelcounter.models.UserListViewModel
import kotlinx.android.synthetic.main.pending_request_item.view.*
import kotlinx.android.synthetic.main.user_list_item.view.friendName

class PendingRequestAdapter(
    private val context: Context,
    private val userList: ArrayList<UserListViewModel>,
    private val userFullList: ArrayList<UserListViewModel> = ArrayList()
) :
    RecyclerView.Adapter<PendingRequestAdapter.UserViewHolder>() {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.friendName.text = userList[position].userName
        if (userFullList.size == 0) {
            userFullList.addAll(userList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.pending_request_item, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val friendName: TextView = view.friendName
        val okBtn: ImageButton = view.btnImgOk
        val dismissBtn: ImageButton = view.btnImgDismiss

        init {
            view.setOnClickListener(this)
            okBtn.setOnClickListener(this)
            dismissBtn.setOnClickListener(this)
        }

        override fun onClick(v: View) {
        }
    }
}