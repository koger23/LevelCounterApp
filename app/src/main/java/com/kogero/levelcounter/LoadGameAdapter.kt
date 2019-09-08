package com.kogero.levelcounter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.helpers.TimeConverter
import com.kogero.levelcounter.model.Game
import kotlinx.android.synthetic.main.loadgame_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class LoadGameAdapter(
    private val context: Context,
    private val gameList: List<Game>
) :
    RecyclerView.Adapter<LoadGameAdapter.LoadGameViewHolder>() {
    override fun onBindViewHolder(holder: LoadGameViewHolder, position: Int) {
        holder.tvNo.text = "${position + 1}."
        holder.tvDate.text = SimpleDateFormat(
            "yyyy-MM-dd HH:mm",
            Locale.ENGLISH
        ).format(
            TimeConverter.fromStringToDate(
                gameList[position].dateTime,
                "yyyy-MM-dd'T'HH:mm:ss"
            )
        ).toString()
        holder.tvPlayer.text = "Number of players: ${gameList[position].inGameUsers.size}"
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
        val tvPlayer: TextView = view.tvPlayers

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
        }
    }
}