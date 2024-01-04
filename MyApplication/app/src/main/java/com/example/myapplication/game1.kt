package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class game1 : AppCompatActivity() {

    private val listWords = ListOfWords.words
    private lateinit var tvAlphabet: TextView
    private lateinit var etAnswer: EditText
    private lateinit var btnCheck: Button
    private lateinit var tvResult: TextView
    private lateinit var btnExit: Button
    private lateinit var btnViewHistory: Button
    private lateinit var progressBar: ProgressBar
    private var currentAlphabet: Char = ' '
    private var score: Int = 0
    private var timer: CountDownTimer? = null

    private lateinit var gameMusic: MediaPlayer
    private lateinit var buttonClickSound: MediaPlayer
    private lateinit var pointGainSound: MediaPlayer
    private lateinit var gameOverSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1)

        tvAlphabet = findViewById(R.id.tv_alphabet)
        etAnswer = findViewById(R.id.et_answer)
        btnCheck = findViewById(R.id.btn_check)
        tvResult = findViewById(R.id.tv_result)
        btnExit = findViewById(R.id.btn_exit)
        btnViewHistory = findViewById(R.id.btn_view_history)
        progressBar = findViewById(R.id.progress_bar)

        gameMusic = MediaPlayer.create(this, R.raw.game_music)
        buttonClickSound = MediaPlayer.create(this, R.raw.button_sound)
        pointGainSound = MediaPlayer.create(this, R.raw.correct_sound)
        gameOverSound = MediaPlayer.create(this, R.raw.lose_sound)

        gameMusic.isLooping = true
        gameMusic.start()

        generateRandomAlphabet()

        timer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = (millisUntilFinished / 1200).toInt()
            }

            override fun onFinish() {
                saveScore()
                tvResult.text = "Game over! Your final score is: $score"
                btnCheck.isEnabled = false
                gameOverSound.start()
                Toast.makeText(this@game1, "Game Over!", Toast.LENGTH_SHORT).show()
            }
        }.start()

        Toast.makeText(this, "Game Started!", Toast.LENGTH_SHORT).show()

        btnCheck.setOnClickListener {
            buttonClickSound.start()
            checkAnswer()
            etAnswer.text.clear()
        }

        btnExit.setOnClickListener {
            timer?.cancel()
            gameMusic.stop()
            finish()
        }

        btnViewHistory.setOnClickListener {
            buttonClickSound.start()
            showHistory()
        }
    }

    private fun generateRandomAlphabet() {
        currentAlphabet = ('A'..'Z').random()
        tvAlphabet.text = "Chữ cái: $currentAlphabet"
    }

    private fun checkAnswer() {
        val answer = etAnswer.text.toString().trim()
        if (answer.isNotEmpty() && answer.first().toUpperCase() == currentAlphabet && listWords.contains(answer)) {
            score++
            pointGainSound.start()
            tvResult.text = "Correct! Your score is: $score"
        } else {
            tvResult.text = "Incorrect! Try again."
        }
        generateRandomAlphabet()
    }

    private fun saveScore() {
        val sharedPref = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("score", score)
        editor.apply()
    }

    private fun showHistory() {
        val sharedPref = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val score = sharedPref.getInt("score", 0)
        AlertDialog.Builder(this)
            .setTitle("Score History")
            .setMessage("Your last score was: $score")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        gameMusic.release()
        buttonClickSound.release()
        pointGainSound.release()
        gameOverSound.release()
    }
}