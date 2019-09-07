package com.kogero.levelcounter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.model.Game
import kotlinx.android.synthetic.main.loadgame_list_item.view.*

class LoadGameAdapter(
    private val context: Context,
    private val gameList: List<Game>
) :
    RecyclerView.Adapter<LoadGameAdapter.LoadGameViewHolder>() {
    override fun onBindViewHolder(holder: LoadGameViewHolder, position: Int) {
        holder.tvNo.text = "${position + 1}."
//        holder.tvDate.text = gameList[position].dateTime.toString()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LoadGameViewHolder {
        return LoadGameViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.loadgame_list_item, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    inner class LoadGameViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        val tvNo: TextView = view.tvNo
        val tvDate: TextView = view.tvDate

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
        }
    }
}