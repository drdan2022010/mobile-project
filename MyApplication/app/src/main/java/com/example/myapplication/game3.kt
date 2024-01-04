package com.example.myapplication

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.widget.ProgressBar

class Game3 : AppCompatActivity() {
    private var startWord = ""
    private var score = 0
    private var timer: CountDownTimer? = null

    private lateinit var startWordTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var wordEditText: EditText
    private lateinit var checkButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var exitButton: Button
    private lateinit var historyButton: Button
    private lateinit var restartButton: Button
    private lateinit var pauseResumeButton: Button
    private lateinit var timerProgressBar: ProgressBar
    private var isPaused = false
    private var timeLeft = 30

    // Media Players for sound effects and music
    private lateinit var backgroundMusic: MediaPlayer
    private lateinit var buttonClickSound: MediaPlayer
    private lateinit var pointGainSound: MediaPlayer
    private lateinit var gameOverSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game3)

        backgroundMusic = MediaPlayer.create(this, R.raw.background_music)
        buttonClickSound = MediaPlayer.create(this, R.raw.button_sound)
        pointGainSound = MediaPlayer.create(this, R.raw.correct_sound)
        gameOverSound = MediaPlayer.create(this, R.raw.lose_sound)

        backgroundMusic.isLooping = true
        backgroundMusic.start()

        startWordTextView = findViewById(R.id.startWordTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        wordEditText = findViewById(R.id.wordEditText)
        checkButton = findViewById(R.id.checkButton)
        timerTextView = findViewById(R.id.timerTextView)
        exitButton = findViewById(R.id.exitButton)
        historyButton = findViewById(R.id.historyButton)
        restartButton = findViewById(R.id.restartButton)
        pauseResumeButton = findViewById(R.id.pauseResumeButton)
        timerProgressBar = findViewById(R.id.timerProgressBar)

        resetGame()

        checkButton.setOnClickListener {
            buttonClickSound.start() // Play button click sound

            val enteredWord = wordEditText.text.toString().trim()
            if (enteredWord.isEmpty()) {
                Toast.makeText(this, "Please enter a word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val combinedWord = "$startWord $enteredWord"

            if (ListOfWords.game3Words.contains(combinedWord)) {
                pointGainSound.start()

                score += 10
                startWord = enteredWord
                startWordTextView.text = "Start Word: $startWord"
                scoreTextView.text = "Score: $score"

                timer?.cancel()
                timeLeft = 30
                startTimer()
                Toast.makeText(this, "Correct word!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid word combination", Toast.LENGTH_SHORT).show()
            }

            wordEditText.text.clear()
        }

        pauseResumeButton.setOnClickListener {
            if (isPaused) {
                startTimer()
                pauseResumeButton.text = "Pause"
            } else {
                timer?.cancel()
                pauseResumeButton.text = "Resume"
            }
            isPaused = !isPaused
        }

        restartButton.setOnClickListener {
            resetGame()
        }

        exitButton.setOnClickListener {
            finish()
        }

        historyButton.setOnClickListener {
            showHistory()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeft * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
                timerTextView.text = "Time left: $timeLeft"
                timerProgressBar.progress = 30 - timeLeft
            }

            override fun onFinish() {
                gameOverSound.start()

                timerTextView.text = "Game Over"
                checkButton.isEnabled = false
                saveScore()
            }
        }.start()
    }

    private fun resetGame() {
        startWord = ListOfWords.game3Words.random().split(" ")[0]
        startWordTextView.text = "Start Word: $startWord"
        score = 0
        scoreTextView.text = "Score: $score"
        wordEditText.text.clear()
        checkButton.isEnabled = true
        pauseResumeButton.text = "Pause"
        isPaused = false
        timeLeft = 30
        timerProgressBar.progress = 0

        timer?.cancel()
        startTimer()
    }

    private fun saveScore() {
        val sharedPreferences = getSharedPreferences("Game3Scores", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val scores = sharedPreferences.getStringSet("scores", mutableSetOf())
        scores?.add("$score points")
        editor.putStringSet("scores", scores)
        editor.apply()
    }

    private fun showHistory() {
        val sharedPreferences = getSharedPreferences("Game3Scores", MODE_PRIVATE)
        val scores = sharedPreferences.getStringSet("scores", mutableSetOf())
        val scoresArray = scores?.toTypedArray() ?: arrayOf()

        AlertDialog.Builder(this)
            .setTitle("Score History")
            .setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, scoresArray)) { _, _ -> }
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundMusic.release()
        buttonClickSound.release()
        pointGainSound.release()
        gameOverSound.release()
    }
}