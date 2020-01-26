package com.kogero.levelcounter.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.R
import com.kogero.levelcounter.models.Gender
import com.kogero.levelcounter.models.InGameUser
import kotlinx.android.synthetic.main.ingame_listitem.view.*

class GameAdapter(
    private val context: Context,
    private val userList: List<InGameUser>
) :
    RecyclerView.Adapter<GameAdapter.InGameViewHolder>() {

    var selectedPosition: Int = 0

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
        if (position != -1 && position == selectedPosition) {
            holder.layoutPlayer.setBackgroundResource(R.color.munchkinBrown)
            holder.tvPlayerName.setTextColor(Color.WHITE)
            holder.tvLevel.setTextColor(Color.WHITE)
            holder.tvLevelValue.setTextColor(Color.WHITE)
            holder.tvBonus.setTextColor(Color.WHITE)
            holder.tvBonusValue.setTextColor(Color.WHITE)
            holder.tvStrength.setTextColor(Color.WHITE)
            holder.tvStrengthValue.setTextColor(Color.WHITE)
            holder.tvGender.setTextColor(Color.WHITE)
        }
        holder.tvPlayerName.text = userList[position].UserName
        if (userList[position].IsOnline) {
            holder.tvPlayerStatusIndicator.visibility = View.VISIBLE
        } else {
            holder.tvPlayerStatusIndicator.visibility = View.INVISIBLE
        }
        holder.tvLevelValue.text = userList[position].Level.toString()
        holder.tvBonusValue.text = userList[position].Bonus.toString()
        holder.tvStrengthValue.text = (userList[position].Bonus + userList[position].Level).toString()
        holder.tvGender.text = setGender(userList[position].Gender)

        holder.layoutPlayer.setOnClickListener {
            View.OnClickListener { println("Positon in adapter when clicked: $selectedPosition") }
        }

        holder.layoutPlayer.setOnClickListener(View.OnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        })
    }


    private fun setGender(gender: Gender): String {
        if (gender == Gender.FEMALE) {
            return "female"
        }
        return "male"
    }

    inner class InGameViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        val layoutPlayer: ConstraintLayout = view.player_item_layout
        val tvPlayerName: TextView = view.tvPlayerName
        val tvPlayerStatusIndicator: TextView = view.tvPlayerStatusIndicator
        val tvLevel: TextView = view.tvLevel
        val tvLevelValue: TextView = view.tvLevelValue
        val tvBonus: TextView = view.tvBonus
        val tvBonusValue: TextView = view.tvBonusValue
        val tvStrength: TextView = view.tvStrength
        val tvStrengthValue: TextView = view.tvStrengthValue
        val tvGender: TextView = view.tvGender

        init {
            view.setOnClickListener(this)
            this.setIsRecyclable(false)
        }

        override fun onClick(view: View) {
        }
    }
}