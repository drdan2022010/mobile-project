package com.example.mymemorygame01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Game04 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game04)

        val buttonChoice1 = findViewById<Button>(R.id.buttonChoice1)
        buttonChoice1.setOnClickListener {
            val intent1 = Intent(this, Game04_1::class.java)
            startActivity(intent1)
        }

        val buttonChoice2 = findViewById<Button>(R.id.buttonChoice2)
        buttonChoice2.setOnClickListener {
            val intent2 = Intent(this, Game04_2::class.java)
            startActivity(intent2)
        }

        val buttonChoice3 = findViewById<Button>(R.id.buttonChoice3)
        buttonChoice3.setOnClickListener {
            val intent3 = Intent(this, Game04_3::class.java)
            startActivity(intent3)
        }
    }
}

