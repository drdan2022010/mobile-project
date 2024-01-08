package com.example.mymemorygame01

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat

class Game02 : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game02)

        progressBar = findViewById(R.id.progress_bar)
    }

    fun startGame1(view: View) {

        val intent = Intent(this, Game02_1::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "gameLayout")
        startActivity(intent, options.toBundle())
    }

    fun startGame2(view: View) {

        val intent = Intent(this, Game02_2::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "gameLayout")
        startActivity(intent, options.toBundle())
    }

}