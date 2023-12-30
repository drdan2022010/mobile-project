package com.example.game4math

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList

class Game2Activity : AppCompatActivity() {
    private lateinit var scoreTextView: TextView
    private lateinit var attemptsProgressBar: ProgressBar
    private var score = 0
    private var attempts = 0
    private var selectedNumbers = ArrayList<Int>()
    private var selectedButtons = ArrayList<Button>()
    private var numbers = ArrayList<Int>()
    private var correctPair = Pair(0, 0)
    private lateinit var clickSound: MediaPlayer
    private lateinit var correctSound: MediaPlayer
    private lateinit var wrongSound: MediaPlayer
    private lateinit var loseSound: MediaPlayer
    private lateinit var backgroundMusic: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2)

        scoreTextView = findViewById(R.id.score)
        attemptsProgressBar = findViewById(R.id.attempts)
        val submitButton: Button = findViewById(R.id.submit)
        val resetButton: Button = findViewById(R.id.reset)
        val playAgainButton: Button = findViewById(R.id.play_again)
        val exitButton: Button = findViewById(R.id.exit)

        for (i in 1..6) {
            val button: Button = findViewById(resources.getIdentifier("button$i", "id", packageName))
            button.setOnClickListener { onNumberClick(it) }
        }

        submitButton.setOnClickListener { onSubmit() }
        resetButton.setOnClickListener { resetSelections() }
        playAgainButton.setOnClickListener { playAgain() }
        exitButton.setOnClickListener { finish() }
        generateNumbers()

        // Initialize the media players
        clickSound = MediaPlayer.create(this, R.raw.click_sound)
        correctSound = MediaPlayer.create(this, R.raw.correct_sound)
        wrongSound = MediaPlayer.create(this, R.raw.wrong_sound)
        loseSound = MediaPlayer.create(this, R.raw.lose_sound)
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music)

        // Start the background music
        backgroundMusic.isLooping = true
        backgroundMusic.start()

        // Initialize the shared preferences
        sharedPreferences = getSharedPreferences("com.example.matth2test", Context.MODE_PRIVATE)
    }

    private fun onNumberClick(view: View) {
        val button = view as Button
        val number = button.text.toString().toInt()

        if (selectedNumbers.size < 2 && number !in selectedNumbers) {
            selectedNumbers.add(number)
            selectedButtons.add(button)
            button.setBackgroundColor(Color.GREEN)
        } else {
            selectedNumbers.remove(number)
            selectedButtons.remove(button)
            button.setBackgroundColor(Color.LTGRAY)
        }

        // Play the click sound
        clickSound.start()
    }

    private fun onSubmit() {
        if (selectedNumbers.size == 2 && selectedNumbers.containsAll(listOf(correctPair.first, correctPair.second))) {
            score += 50
            scoreTextView.text = "Score: $score"
            Toast.makeText(this, "Correct answer! You get 50 points.", Toast.LENGTH_SHORT).show()
            generateNumbers() // Generate new numbers after a correct answer

            // Play the correct sound
            correctSound.start()
        } else {
            attempts++
            attemptsProgressBar.progress = attempts
            if (attempts == 3) {
                gameOver()
            } else {
                Toast.makeText(this, "Wrong answer. Try again.", Toast.LENGTH_SHORT).show()
            }

            // Play the wrong sound
            wrongSound.start()
        }
        resetSelections()
    }

    private fun generateNumbers() {
        numbers.clear()
        val random = Random()

        // Generate four random numbers
        for (i in 1..4) {
            numbers.add(random.nextInt(1000) + 1) // Random number between 1 and 1000
        }

        // Generate two numbers such that their sum is 10, 100, or 1000
        val sum = listOf(10, 100, 1000).random()
        val firstNumber = random.nextInt(sum)
        val secondNumber = sum - firstNumber
        numbers.add(firstNumber)
        numbers.add(secondNumber)

        // Shuffle the numbers
        numbers.shuffle()

        // Update the buttons with the new numbers
        for (i in numbers.indices) {
            val button: Button = findViewById(resources.getIdentifier("button${i + 1}", "id", packageName))
            button.text = numbers[i].toString()
            button.setBackgroundColor(Color.LTGRAY)
        }

        // Store the correct pair
        correctPair = Pair(firstNumber, secondNumber)
    }

    private fun resetSelections() {
        for (button in selectedButtons) {
            button.setBackgroundColor(Color.LTGRAY)
        }
        selectedNumbers.clear()
        selectedButtons.clear()
    }

    private fun gameOver() {
        val editor = sharedPreferences.edit()
        val gameCount = sharedPreferences.getInt("GameCount", 0)
        editor.putInt("Score_$gameCount", score)
        editor.putInt("GameCount", gameCount + 1)
        editor.apply()
        Toast.makeText(this, "Game over. Your score has been saved.", Toast.LENGTH_SHORT).show()

        val scores = sharedPreferences.all.map { it.value as Int }.sortedDescending()
        val rank = scores.indexOf(score) + 1

        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("Your final score is $score. Your rank is $rank out of ${scores.size}.")
            .setPositiveButton("Play Again") { _, _ -> playAgain() }
            .setNegativeButton("Exit") { _, _ -> finish() }
            .setNeutralButton("View History") { _, _ -> showHistory(scores) }
            .show()

        // Play the lose sound
        loseSound.start()
    }

    private fun showHistory(scores: List<Int>) {
        val history = scores.mapIndexed { index, score -> "Rank ${index + 1}: $score" }.joinToString("\n")
        AlertDialog.Builder(this)
            .setTitle("Score History")
            .setMessage(history)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun playAgain() {
        score = 0
        attempts = 0
        scoreTextView.text = "Score: $score"
        attemptsProgressBar.progress = attempts
        generateNumbers()
        resetSelections()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Release the media players
        clickSound.release()
        correctSound.release()
        wrongSound.release()
        loseSound.release()
        backgroundMusic.release()
    }
}