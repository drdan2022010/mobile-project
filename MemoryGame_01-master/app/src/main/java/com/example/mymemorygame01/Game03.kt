package com.example.mymemorygame01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.app.ActivityOptionsCompat

class Game03 : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game03)

        progressBar = findViewById(R.id.progress_bar)
    }

    fun startGame1(view: View) {

        val intent = Intent(this, Game03_1::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "gameLayout")
        startActivity(intent, options.toBundle())
    }

    fun startGame2(view: View) {

        val intent = Intent(this, Game03_2::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "gameLayout")
        startActivity(intent, options.toBundle())
    }


    fun startGame3(view: View) {

        val intent = Intent(this, Game03_3::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "gameLayout")
        startActivity(intent, options.toBundle())
    }

    fun startGame4(view: View) {

        val intent = Intent(this, Game03_4::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "gameLayout")
        startActivity(intent, options.toBundle())
    }
}


