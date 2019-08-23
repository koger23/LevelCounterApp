package com.kogero.levelcounter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.model.Relationship
import kotlinx.android.synthetic.main.friend_list_item.view.*

class FriendsAdapter(private val context: Context, private val items: ArrayList<Relationship>) :
    RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.friendName.text = items[position].userId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.friend_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val friendName: TextView = view.friendName

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("onclick", "onClick " + layoutPosition + " " + friendName.text)
        }

    }
}
