package com.example.mymemorygame01

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import android.media.MediaPlayer

class Game03_2 : AppCompatActivity() {

    private lateinit var timer: CountDownTimer
    private var score = 0
    private val startWords = listOf("nhan", "nhan", "nhan", "nhan", "nhan")
    private var originalWord = startWords[Random.nextInt(startWords.size)].toLowerCase()
    private val sharedPref: SharedPreferences by lazy { getSharedPreferences("GameScores", Context.MODE_PRIVATE) }

    private lateinit var gameMusic: MediaPlayer
    private lateinit var clickSound: MediaPlayer
    private lateinit var pointSound: MediaPlayer
    private lateinit var loseSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game03_2)

        gameMusic = MediaPlayer.create(this, R.raw.game_music)
        clickSound = MediaPlayer.create(this, R.raw.click_sound)
        pointSound = MediaPlayer.create(this, R.raw.correct_sound)
        loseSound = MediaPlayer.create(this, R.raw.lose_sound)

        gameMusic.start()

        val scoreTextView = findViewById<TextView>(R.id.score)
        val inputField = findViewById<EditText>(R.id.inputField)
        val timerTextView = findViewById<TextView>(R.id.timer)
        val startWordTextView = findViewById<TextView>(R.id.startWord)
        val startButton = findViewById<Button>(R.id.startButton)
        val resetButton = findViewById<Button>(R.id.resetButton)
        val shuffleButton = findViewById<Button>(R.id.shuffleButton)
        val instructionsTextView = findViewById<TextView>(R.id.instructions)
        val viewHistoryButton = findViewById<Button>(R.id.viewHistoryButton)
        val btnExit = findViewById<Button>(R.id.exitButton)

        instructionsTextView.text =
            "Enter a word that starts with the given start word. You get 10 points for each valid word."

        startWordTextView.text = "Start word: $originalWord"

        timer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = "Time remaining: ${millisUntilFinished / 1000} seconds"
            }

            override fun onFinish() {
                // Play lose sound
                loseSound.start()

                timerTextView.text = "Game over! Your final score is $score"
                startButton.isEnabled = true
                resetButton.isEnabled = false
                shuffleButton.isEnabled = false
                inputField.isEnabled = false

                with(sharedPref.edit()) {
                    putInt(System.currentTimeMillis().toString(), score)
                    apply()
                }
            }
        }

        startButton.setOnClickListener {
            clickSound.start()

            timer.start()
            startButton.isEnabled = false
            resetButton.isEnabled = true
            shuffleButton.isEnabled = true
            inputField.isEnabled = true
        }

        resetButton.setOnClickListener {
            clickSound.start()

            timer.cancel()
            score = 0
            scoreTextView.text = "Score: 0"
            originalWord = startWords[Random.nextInt(startWords.size)].toLowerCase()
            startWordTextView.text = "Start word: $originalWord"
            timerTextView.text = "Time remaining: 120 seconds"
            startButton.isEnabled = true
            resetButton.isEnabled = false
            shuffleButton.isEnabled = false
            inputField.isEnabled = false
        }

        shuffleButton.setOnClickListener {
            clickSound.start()

            originalWord = startWords[Random.nextInt(startWords.size)].toLowerCase()
            startWordTextView.text = "Start word: $originalWord"
        }

        inputField.setOnEditorActionListener { _, _, _ ->
            clickSound.start()

            val enteredWord = inputField.text.toString().toLowerCase()
            if (enteredWord.startsWith(originalWord) && ListOfWords.validWords.map { it.toLowerCase() }
                    .contains(enteredWord)) {
                pointSound.start()

                score += 10
                scoreTextView.text = "Score: $score"
            } else {
                inputField.error =
                    "Invalid word. Please enter a word that starts with $originalWord and is in the list of valid words."
            }
            inputField.text.clear()
            true
        }

        viewHistoryButton.setOnClickListener {
            clickSound.start()

            viewHistory()
        }

        btnExit.setOnClickListener {
            timer?.cancel()
            gameMusic.stop()
            finish()
        }
    }

    fun viewHistory() {
        val scores = sharedPref.all.values.map { it as Int }.sortedDescending()
        val listView = ListView(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, scores.mapIndexed { index, score ->
            "Rank ${index + 1}: $score"
        })
        listView.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle("Score History")
            .setView(listView)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onStop() {
        super.onStop()
        gameMusic.release()
        clickSound.release()
        pointSound.release()
        loseSound.release()
    }
}