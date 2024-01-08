package com.example.mymemorygame01


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainMenu : AppCompatActivity() {

    // Button click handlers
    val startGame1 = View.OnClickListener {
        val intent = Intent(this, Game01::class.java)
        startActivity(intent)
    }

    val startGame2 = View.OnClickListener {
        val intent = Intent(this, Game02::class.java)
        startActivity(intent)
    }

    val startGame3 = View.OnClickListener {
        val intent = Intent(this, Game03::class.java)
        startActivity(intent)
    }

    val startGame4 = View.OnClickListener {
        val intent = Intent(this, Game04::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)

        findViewById<View>(R.id.game1Button).setOnClickListener(startGame1)
        findViewById<View>(R.id.game2Button).setOnClickListener(startGame2)
        findViewById<View>(R.id.game3Button).setOnClickListener(startGame3)
        findViewById<View>(R.id.game4Button).setOnClickListener(startGame4)

    }
}