package com.example.game4math

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Game1Activity : AppCompatActivity() {
    private var score = 0
    private val images = arrayOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4,
        R.drawable.image5
    )
    private val delayTime = 3000L
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1)

        val product1Price = findViewById<TextView>(R.id.product1_price)
        val product2Price = findViewById<TextView>(R.id.product2_price)
        val product1Image = findViewById<ImageView>(R.id.product1_image)
        val product2Image = findViewById<ImageView>(R.id.product2_image)

        // Play music when the game starts
        mediaPlayer = MediaPlayer.create(this, R.raw.game_music)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        updateLevel(product1Price, product2Price, product1Image, product2Image)
    }

    fun onProduct1ButtonPressed(view: View) {
        // Play sound when a button is pressed
        val buttonSound = MediaPlayer.create(this, R.raw.button_sound)
        buttonSound.start()

        val product1Price = findViewById<TextView>(R.id.product1_price)
        val product2Price = findViewById<TextView>(R.id.product2_price)
        val product1Result = findViewById<TextView>(R.id.product1_result)
        val product2Result = findViewById<TextView>(R.id.product2_result)
        val product1Button = findViewById<Button>(R.id.product1_button)
        val product2Button = findViewById<Button>(R.id.product2_button)
        val scoreTextView = findViewById<TextView>(R.id.score)

        handleButtonClick(
            product1Price,
            product2Price,
            product1Result,
            product2Result,
            product1Button,
            product2Button,
            scoreTextView
        )
    }

    fun onProduct2ButtonPressed(view: View) {
        // Play sound when a button is pressed
        val buttonSound = MediaPlayer.create(this, R.raw.button_sound)
        buttonSound.start()

        val product1Price = findViewById<TextView>(R.id.product1_price)
        val product2Price = findViewById<TextView>(R.id.product2_price)
        val product1Result = findViewById<TextView>(R.id.product1_result)
        val product2Result = findViewById<TextView>(R.id.product2_result)
        val product1Button = findViewById<Button>(R.id.product1_button)
        val product2Button = findViewById<Button>(R.id.product2_button)
        val scoreTextView = findViewById<TextView>(R.id.score)

        handleButtonClick(
            product2Price,
            product1Price,
            product2Result,
            product1Result,
            product2Button,
            product1Button,
            scoreTextView
        )
    }

    fun onExitButtonPressed(view: View) {
        finish()
    }

    private fun handleButtonClick(
        product1Price: TextView,
        product2Price: TextView,
        product1Result: TextView,
        product2Result: TextView,
        product1Button: Button,
        product2Button: Button,
        scoreTextView: TextView
    ) {
        product2Result.visibility = View.VISIBLE
        product1Result.visibility = View.VISIBLE

        if (calculateExpression(product1Price.text.toString()) < calculateExpression(product2Price.text.toString())) {
            score += 50
            scoreTextView.text = "Score: $score"
            Toast.makeText(this, "Correct! Your score is $score", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Incorrect! Your score is $score", Toast.LENGTH_SHORT).show()
        }

        product1Result.text = "Result: ${calculateExpression(product1Price.text.toString())}"
        product2Result.text = "Result: ${calculateExpression(product2Price.text.toString())}"
        product1Button.isEnabled = false
        product2Button.isEnabled = false

        Handler(Looper.getMainLooper()).postDelayed({
            // Stop the game music
            mediaPlayer.stop()

            // Play sound when announcing score
            val scoreSound = MediaPlayer.create(this, R.raw.score_sound)
            scoreSound.start()

            val product1Image = findViewById<ImageView>(R.id.product1_image)
            val product2Image = findViewById<ImageView>(R.id.product2_image)

            updateLevel(product1Price, product2Price, product1Image, product2Image)
            product1Button.isEnabled = true
            product2Button.isEnabled = true
            product1Result.visibility = View.GONE
            product2Result.visibility = View.GONE
        }, delayTime)
    }

    private fun updateLevel(
        product1Price: TextView,
        product2Price: TextView,
        product1Image: ImageView,
        product2Image: ImageView
    ) {
        product1Price.text = generateExpression()
        product2Price.text = generateExpression()
        product1Image.setImageResource(getRandomImage())
        product2Image.setImageResource(getRandomImage())
    }

    private fun generateExpression(): String {
        val operations = arrayOf("+", "-", "*", "/")
        val random = Random()
        val operator = operations[random.nextInt(operations.size)]
        val operand2 = if (operator == "/") random.nextInt(9) + 1 else random.nextInt(10)
        return "${random.nextInt(10)} $operator $operand2"
    }

    private fun calculateExpression(expression: String): Double {
        val parts = expression.split(" ")
        val operand1 = parts[0].toDouble()
        val operator = parts[1]
        val operand2 = parts[2].toDouble()

        return when (operator) {
            "+" -> operand1 + operand2
            "-" -> operand1 - operand2
            "*" -> operand1 * operand2
            "/" -> if (operand2 != 0.0) operand1 / operand2 else Double.MAX_VALUE
            else -> throw IllegalArgumentException("Unknown operator")
        }
    }

    private fun getRandomImage(): Int {
        val random = Random()
        return images[random.nextInt(images.size)]
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the media player resources
        mediaPlayer.release()
    }
}
