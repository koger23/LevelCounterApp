package com.kogero.levelcounter.model

import android.view.View

interface RecyclerViewClickListener {
    fun onClick(view: View, position: Int)
    fun onLongClick(view: View, position: Int)
}