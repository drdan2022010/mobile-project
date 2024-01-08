package com.example.mymemorygame01

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Game01_2 : AppCompatActivity() {
    private lateinit var cardImageViews: Array<ImageView>
    private val cardImages = mutableListOf(
        // Danh sách tên hình ảnh của các thẻ
        "card01", "card02", "card03", "card04", "card05",
        "card06", "card07", "card08", "card09", "card10",
        "card11", "card12", "card13", "card14", "card15",
        "card16", "card17", "card18", "card19", "card20",
        "card21"
    )
    private val selectedCards = mutableListOf<String>()
    private var round = 1
    private var lastSelectedCard: String? = null
//    private var previousRoundCards = mutableListOf<String>()
    private var score = 0 // Điểm hiện tại của người chơi
    private var highScore = 0 // Điểm cao nhất đạt được
    private lateinit var scoreTextView: TextView
    private lateinit var highScoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game01_2)

        cardImageViews = arrayOf(
            findViewById(R.id.cardImageView1),
            findViewById(R.id.cardImageView2),
            findViewById(R.id.cardImageView3),
            findViewById(R.id.cardImageView4),
            findViewById(R.id.cardImageView5)
        )
        scoreTextView = findViewById(R.id.scoreTextView)
        highScoreTextView = findViewById(R.id.highScoreTextView)

        val sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)
        highScore = sharedPreferences.getInt("high_score", 0)
        initGame()

        val restartButton: Button = findViewById(R.id.restartButton)
        restartButton.setOnClickListener {
            restartGame() // Gọi hàm restartGame() khi nút Restart được nhấn
        }
        updateHighScore()
    }

    private fun initGame() {
        score = 0 // Reset điểm số khi bắt đầu game mới
        selectedCards.clear()
        lastSelectedCard = null
        selectedCards.addAll(cardImages.shuffled().take(5))
        updateCardDisplay()
        updateScoreDisplay() // Cập nhật điểm số và điểm cao nhất
    }

    private fun updateCardDisplay() {
        for (i in cardImageViews.indices) {
            val cardImageView = cardImageViews[i]
            val cardName = selectedCards[i]
            cardImageView.tag = cardName
            cardImageView.visibility = View.VISIBLE
            val resourceId = resources.getIdentifier(cardName, "drawable", packageName)
            cardImageView.setImageResource(resourceId)
        }
    }

    fun onCardClick(view: View) {
        if (view is ImageView) {
            val cardName = view.tag as String
            processSelection(cardName)
        }
    }

    private fun processSelection(cardName: String) {
        if (round == 1) {
            prepareNextRound(cardName)
            updateHighScore()
            round++
        } else if (cardName == lastSelectedCard) {
            score++
            updateHighScore()
//            Toast.makeText(this, "Correct! Moving to next round.", Toast.LENGTH_SHORT).show()
            prepareNextRound(cardName)
            round++
        } else {
            showLostDialog()
        }
        updateScoreDisplay()
    }
    private fun updateHighScore() {
        if (score > highScore) {
            highScore = score
            val sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("high_score", highScore)
            editor.apply()
        }
        highScoreTextView.text = "High Score: $highScore"
    }

    private fun updateScoreDisplay() {
        scoreTextView.text = "Score: $score" // Cập nhật hiển thị điểm số
    }

    private fun prepareNextRound(selectedCard: String) {
        // Trong vòng đầu tiên, chỉ đặt lastSelectedCard là lá bài được chọn
        if (round == 1) {
            lastSelectedCard = selectedCard
            return
        }

        // Từ vòng thứ hai trở đi, random một lá bài mới không trùng với các lá bài hiện tại và lá bài được chọn
        val availableCards = (cardImages - selectedCards - selectedCard)
        val newCard = availableCards.shuffled().first()

        // Cập nhật lastSelectedCard và danh sách các lá bài cho vòng tiếp theo
        lastSelectedCard = newCard
        selectedCards.remove(selectedCard) // Loại bỏ lá bài được chọn ở vòng trước
        selectedCards.add(newCard)         // Thêm lá bài mới
        selectedCards.shuffle()            // Trộn lẫn vị trí của các lá bài

        updateCardDisplay()
    }



    private fun showLostDialog() {
        updateHighScore()
        cardImageViews.forEach { imageView ->
            imageView.isClickable = false
        }

        // Tạo và hiển thị AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("You lost the game. Your score: $score. Do you want to exit?")
            .setPositiveButton("Exit") { dialog, which ->
                // Thoát khỏi Activity
                finish()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Nếu người chơi chọn 'Cancel', cho phép chơi lại
                dialog.dismiss()
                initGame() // Reset game sẽ reset điểm
            }
            .setCancelable(false)
            .show()
    }
    private fun restartGame() {
        finish()
        startActivity(intent)
    }
}