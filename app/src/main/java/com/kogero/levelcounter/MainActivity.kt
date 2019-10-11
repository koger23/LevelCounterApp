package com.kogero.levelcounter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.activites.LoginActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
        val i3 = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(i3)
    }
}