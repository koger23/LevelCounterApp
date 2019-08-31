package com.kogero.levelcounter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.model.InGameUser
import kotlinx.android.synthetic.main.ingame_listitem.view.*

class GameAdapter(
    private val context: Context,
    private val userList: List<InGameUser>
    ) :
    RecyclerView.Adapter<GameAdapter.InGameViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InGameViewHolder {
        return InGameViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.ingame_listitem, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: InGameViewHolder, position: Int) {
        holder.tvPlayerName.text = userList[position].UserName
        holder.tvLevel.text = userList[position].Level.toString()
        holder.tvBonus.text = userList[position].Bonus.toString()
    }

    inner class InGameViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val tvPlayerName: TextView = view.tvPlayerName
        val tvLevel: TextView = view.tvLevel
        val tvBonus: TextView = view.tvBonus

        init {
            view.setOnClickListener(this)
            this.setIsRecyclable(false)
        }

        override fun onClick(v: View) {
        }
    }
}