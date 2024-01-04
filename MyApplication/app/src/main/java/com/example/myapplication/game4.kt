package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Game4 : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var scrambledWordTextView: TextView
    private lateinit var guessWordEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var messageTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var viewHistoryButton: Button
    private lateinit var resetGameButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var btnExit: Button

    private val words = ListOfWords.game4Words
    private lateinit var currentWord: String
    private var score = 0
    private var guessCount = 0

    private var timer: CountDownTimer? = null
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var buttonClickSound: MediaPlayer
    private lateinit var pointSound: MediaPlayer
    private lateinit var loseSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game4)

        scoreTextView = findViewById(R.id.score)
        scrambledWordTextView = findViewById(R.id.scrambled_word)
        guessWordEditText = findViewById(R.id.guess_word)
        submitButton = findViewById(R.id.submit_button)
        messageTextView = findViewById(R.id.message)
        timerTextView = findViewById(R.id.timer)
        viewHistoryButton = findViewById(R.id.view_history_button)
        resetGameButton = findViewById(R.id.reset_game_button)
        clearHistoryButton = findViewById(R.id.clear_history_button)
        btnExit = findViewById(R.id.exitButton)

        sharedPreferences = getSharedPreferences("game4_history", Context.MODE_PRIVATE)

        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        buttonClickSound = MediaPlayer.create(this, R.raw.button_sound)
        pointSound = MediaPlayer.create(this, R.raw.correct_sound)
        loseSound = MediaPlayer.create(this, R.raw.lose_sound)

        mediaPlayer.isLooping = true
        mediaPlayer.start()

        submitButton.setOnClickListener {
            buttonClickSound.start()
            checkGuess()
        }

        viewHistoryButton.setOnClickListener {
            buttonClickSound.start()
            viewHistory()
        }

        resetGameButton.setOnClickListener {
            buttonClickSound.start()
            resetGame()
        }

        clearHistoryButton.setOnClickListener {
            buttonClickSound.start()
            clearHistory()
        }

        btnExit.setOnClickListener {
            timer?.cancel()
            mediaPlayer.stop()
            finish()
        }

        pickRandomWord()
    }

    private fun pickRandomWord() {
        currentWord = words.random()
        scrambledWordTextView.text = scrambleWord(currentWord)
        guessCount = 0
        startTimer()
    }

    private fun scrambleWord(word: String): String {
        val letters = word.toCharArray()
        letters.shuffle()
        return String(letters)
    }

    private fun checkGuess() {
        val guess = guessWordEditText.text.toString()
        if (guess == currentWord) {
            score++
            scoreTextView.text = "Score: $score"
            messageTextView.text = "Correct! New word:"
            pointSound.start()
            pickRandomWord()
            guessWordEditText.text.clear()
        } else {
            guessCount++
            if (guessCount >= 3) {
                gameOver()
            } else {
                messageTextView.text = "Wrong guess. Try again."
            }
        }
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = "Time left: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                gameOver()
            }
        }.start()
    }

    private fun gameOver() {
        timer?.cancel()
        messageTextView.text = "Game Over! Your score: $score"
        loseSound.start()
        saveScore(score)
        score = 0
        scoreTextView.text = "Score: $score"
    }

    private fun saveScore(score: Int) {
        val editor = sharedPreferences.edit()
        val history = sharedPreferences.getString("history", "") ?: ""
        editor.putString("history", "$history\nScore: $score")
        editor.apply()
    }

    private fun viewHistory() {
        val history = sharedPreferences.getString("history", "")
        messageTextView.text = "Score History:\n$history"
    }

    private fun resetGame() {
        score = 0
        scoreTextView.text = "Score: $score"
        pickRandomWord()
    }

    private fun clearHistory() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        messageTextView.text = "Score history cleared."
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        buttonClickSound.release()
        pointSound.release()
        loseSound.release()
    }
}