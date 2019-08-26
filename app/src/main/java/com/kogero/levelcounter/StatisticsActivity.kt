package com.kogero.levelcounter

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.model.Statistics


class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        try {
            val statistics = intent.extras.getParcelable<Statistics>("STATISTICS")

            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "${statistics.statisticsId}", Toast.LENGTH_SHORT).show()
        } catch (e: Resources.NotFoundException) {
            Toast.makeText(this, "Error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(): FriendsActivity = FriendsActivity()
    }
}