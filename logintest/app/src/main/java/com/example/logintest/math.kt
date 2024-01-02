package com.example.logintest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat

class math : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_math)

        progressBar = findViewById(R.id.progress_bar)
    }

    fun startGame1(view: View) {

        val intent = Intent(this, Game1Activity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "gameLayout")
        startActivity(intent, options.toBundle())
    }

    fun startGame2(view: View) {

        val intent = Intent(this, Game2Activity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "gameLayout")
        startActivity(intent, options.toBundle())
    }

}