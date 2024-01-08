package com.example.mymemorygame01

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class Game01_3 : AppCompatActivity() {
    private lateinit var imageViews: Array<ImageView>
    private var cardResourceIds = mutableListOf<Int>()
    private var hiddenCardIndex = 0

    private lateinit var answerImageViews: Array<ImageView>
    private var hiddenCardResourceId = 0
    private var isRoundOver = false

    private lateinit var scoreDisplayTextView: TextView
    private var score = 0
    private lateinit var highScoreTextView: TextView
    private var highScore = 0 // Điểm cao nhất đạt được
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game01_3)
        setupImageViews()

        answerImageViews = arrayOf(
            findViewById(R.id.answerImageView1),
            findViewById(R.id.answerImageView2),
            findViewById(R.id.answerImageView3),
            findViewById(R.id.answerImageView4)
        )

        // Set up listeners for answer choices
        setupAnswerListeners()
        scoreDisplayTextView = findViewById(R.id.scoreDisplayTextView)
        highScoreTextView = findViewById(R.id.highScoreTextView) // Gán highScoreTextView bằng findViewById

        sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)
        highScore = sharedPreferences.getInt("high_score", 0)
        highScoreTextView.text = "High Score: $highScore"

        // Start the game
        startGame()

    }

    private fun setupImageViews() {
        imageViews = arrayOf(
            findViewById(R.id.imageView1),
            findViewById(R.id.imageView2),
            findViewById(R.id.imageView3),
            findViewById(R.id.imageView4)
        )
    }

    private fun startGame() {
        // Display random cards
        displayRandomCards()
    }

    private fun displayRandomCards() {
        cardResourceIds = listOf(
            R.drawable.card01, R.drawable.card02, R.drawable.card03,
            R.drawable.card04, R.drawable.card05, R.drawable.card06,
            R.drawable.card07, R.drawable.card08, R.drawable.card09,
            R.drawable.card10, R.drawable.card11, R.drawable.card12,
            R.drawable.card13, R.drawable.card14, R.drawable.card15,
            R.drawable.card16, R.drawable.card17, R.drawable.card18,
            R.drawable.card19, R.drawable.card20, R.drawable.card21
        ).shuffled().take(4).toMutableList()

        // Set images for the imageViews
        cardResourceIds.forEachIndexed { index, resourceId ->
            imageViews[index].setImageResource(resourceId)
        }

        // Hide a random card after 3 seconds
        Handler().postDelayed({
            hideRandomCard()
        }, 3000) // Delay for 3 seconds
    }

    private fun hideRandomCard() {
        // Choose a random card to hide
        hiddenCardIndex = Random.nextInt(4)

        // Store the hidden card's resource ID
        hiddenCardResourceId = cardResourceIds[hiddenCardIndex]

        // Replace the image with cardquestion.png
        imageViews[hiddenCardIndex].setImageResource(R.drawable.cardquestion)

        // Delay before revealing answers
        Handler().postDelayed({
            revealAnswers()
        }, 3000)
    }

    private fun setupAnswerListeners() {
        answerImageViews.forEach { imageView ->
            imageView.setOnClickListener {
                if (!isRoundOver) {
                    checkAnswer(imageView)
                }
            }
        }
    }

    private fun checkAnswer(selectedImageView: ImageView) {
        val selectedAnswerId = selectedImageView.drawable.constantState

        if (selectedAnswerId == resources.getDrawable(hiddenCardResourceId).constantState) {
            // User selected the correct answer
            score++
            scoreDisplayTextView.text = "Score: $score"
            isRoundOver = true

            if (score > highScore) {
                highScore = score
                val editor = sharedPreferences.edit()
                editor.putInt("high_score", highScore)
                editor.apply()
                highScoreTextView.text = "High Score: $highScore"
            }

            // Delay before starting a new round
            Handler().postDelayed({
                // Clear the answers and start a new round
                resetGame()
            }, 2000) // Delay for 2 seconds
        } else {
            // User selected the wrong answer
            showGameOverDialog()
            // End the game or handle as needed
        }
    }


    private fun resetGame() {
        answerImageViews.forEach {
            imageView -> imageView.setImageResource(R.drawable.cardquestion)
        }

        // Clear the card resource IDs and images
        cardResourceIds.clear()
        imageViews.forEach { imageView ->
            imageView.setImageResource(0) // Clear the images
        }
        isRoundOver = false
        // Start a new round by displaying random cards and hiding a random card
        startGame()
    }

    private fun revealAnswers() {
        val correctAnswer = cardResourceIds[hiddenCardIndex]

        val availableCards = listOf(
            R.drawable.card01, R.drawable.card02, R.drawable.card03,
            R.drawable.card04, R.drawable.card05, R.drawable.card06,
            R.drawable.card07, R.drawable.card08, R.drawable.card09,
            R.drawable.card10, R.drawable.card11, R.drawable.card12,
            R.drawable.card13, R.drawable.card14, R.drawable.card15,
            R.drawable.card16, R.drawable.card17, R.drawable.card18,
            R.drawable.card19, R.drawable.card20, R.drawable.card21
        )

        // Loại bỏ thẻ câu hỏi và đáp án đúng khỏi danh sách thẻ
        val availableAnswers = availableCards.filter { it != correctAnswer && !cardResourceIds.contains(it) }

        // Lấy ngẫu nhiên 3 đáp án sai từ danh sách đã loại bỏ
        val wrongAnswers = availableAnswers.shuffled().take(3)

        // Tạo danh sách hiển thị chứa đáp án đúng và các đáp án sai
        val shownAnswers = mutableListOf(correctAnswer).apply {
            addAll(wrongAnswers)
        }.shuffled()

        // Hiển thị đáp án
        answerImageViews.forEachIndexed { index, imageView ->
            imageView.setImageResource(shownAnswers[index])
        }
    }

    private fun showGameOverDialog() {
        // Tạm dừng game và hiển thị dialog
        isRoundOver = true

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.apply {
            setTitle("Game Over")
            setMessage("Your final score is: $score")
            setPositiveButton("Exit Game") { dialog, _ ->
                // Khi người dùng chọn "Exit Game"
                finish() // Đóng activity
            }

            setCancelable(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }



}