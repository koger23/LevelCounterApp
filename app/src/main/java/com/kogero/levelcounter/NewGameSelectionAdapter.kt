package com.kogero.levelcounter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.model.UserSelectionModel
import kotlinx.android.synthetic.main.userselection_item.view.*

class NewGameSelectionAdapter(
    private val context: Context,
    private val userList: ArrayList<UserSelectionModel>,
    private val userFullList: ArrayList<UserSelectionModel> = ArrayList()
) :
    RecyclerView.Adapter<NewGameSelectionAdapter.SelectionViewHolder>() {

    override fun onBindViewHolder(holder: SelectionViewHolder, position: Int) {
        holder.friendName.text = userList[position].user.userName
        holder.checkBox.isChecked = userList[position].isSelected

        if (userFullList.size == 0) {
            userFullList.addAll(userList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionViewHolder {
        return SelectionViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.userselection_item, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun filterUsers(query: String): List<UserSelectionModel> {
        var query = query.toLowerCase()
        userList.clear()
        for (user1 in userFullList) {
            if (user1.user.userName?.toLowerCase()!!.contains(query)) {
                userList.add(user1)
            }
        }
        if (userList.isEmpty()) {
            Toast.makeText(this.context, "No match", Toast.LENGTH_SHORT).show()
        }
        notifyDataSetChanged()
        return userList
    }

    inner class SelectionViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val friendName: TextView = view.tvSelectedUserName
        val checkBox: CheckBox = view.cbSelection

        init {
            view.setOnClickListener(this)
            this.setIsRecyclable(false)
        }
        override fun onClick(v: View) {
            checkBox.isChecked = !checkBox.isChecked
        }

    }
}

