package com.kogero.levelcounter.models

import android.view.View

interface RecyclerViewClickListener {
    fun onClick(view: View, position: Int)
    fun onLongClick(view: View, position: Int)
}