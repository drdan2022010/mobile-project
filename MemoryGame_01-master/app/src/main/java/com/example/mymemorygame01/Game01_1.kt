package com.example.mymemorygame01

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.gridlayout.widget.GridLayout
import kotlin.random.Random

class Game01_1 : AppCompatActivity() {
    private lateinit var gridLayout: GridLayout
    private val gridSize = 6 // 6x6 grid
    private val buttons = Array(gridSize) { arrayOfNulls<Button>(gridSize) }
    private val correctButtons = mutableListOf<Button>()
    private val handler = Handler()
    private var mistakesMade = 0
    private var allowedMistakes = 0
    private val buttonStates = mutableMapOf<Button, Boolean>()
    private var isGameActive = true
    private var correctPresses = 0
    private val totalColorsToShow = 5
    private var isDisplayingColors = false
    private var score = 0

    private lateinit var scoreTextView: TextView
    private lateinit var highScoreTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var highScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("MyMemoryGamePrefs", Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("highScore", 0)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.game01_1)
        gridLayout = findViewById(R.id.gridLayout)

        initializeGrid()
        setupLayoutChangeListener()

        val exitButton = findViewById<Button>(R.id.exitButton)
        val restartButton = findViewById<Button>(R.id.restartButton)
        scoreTextView = findViewById(R.id.scoreTextView)
        highScoreTextView = findViewById(R.id.highScoreTextView)

        highScoreTextView.text = "High Score: $highScore"

        exitButton.setOnClickListener {
            exitGame() // Gọi hàm exitGame() khi nút "Exit Game" được bấm
        }
        restartButton.setOnClickListener {
            restartGame() // Gọi hàm restartGame() khi nút "Restart Game" được bấm
        }

    }

    private fun initializeGrid() {
        gridLayout.post {
            val gridWidth = gridLayout.width - (gridLayout.paddingLeft + gridLayout.paddingRight)
            val gridHeight = gridLayout.height - (gridLayout.paddingTop + gridLayout.paddingBottom)

            val buttonSize = Math.min(gridWidth, gridHeight) / gridSize

            for (i in 0 until gridSize) {
                for (j in 0 until gridSize) {
                    val button = Button(this).apply {
                        // Cấu hình kích thước và khoảng cách của nút
                        layoutParams = GridLayout.LayoutParams().apply {
                            width = buttonSize - 2 // Trừ đi kích thước viền
                            height = buttonSize - 2 // Trừ đi kích thước viền
                            setMargins(1, 1, 1, 1) // Đặt khoảng cách là 1px
                        }

                        setBackgroundColor(Color.WHITE)
                        setOnClickListener { checkButton(this)
                        }
                    }
                    gridLayout.addView(button)
                    buttons[i][j] = button
                    buttonStates[button] = false
                }
            }
            displayRandomColors()
        }

    }

    private fun setupLayoutChangeListener() {
        val observer = gridLayout.viewTreeObserver
        observer.addOnGlobalLayoutListener {
            val gridWidth = gridLayout.width - (gridLayout.paddingLeft + gridLayout.paddingRight)
            val gridHeight = gridLayout.height - (gridLayout.paddingTop + gridLayout.paddingBottom)
            val buttonSize = Math.min(gridWidth, gridHeight) / gridSize

            for (i in 0 until gridSize) {
                for (j in 0 until gridSize) {
                    val button = buttons[i][j]
                    button?.layoutParams?.width = buttonSize - 2 // Adjust for border
                    button?.layoutParams?.height = buttonSize - 2 // Adjust for border
                }
            }
        }
    }


    // Trong phương thức displayRandomColors()
    private fun displayRandomColors() {
        isDisplayingColors = true
        val random = Random(System.currentTimeMillis())
        val numberOfColors = totalColorsToShow // Đảm bảo luôn là 5
        allowedMistakes = 2
        mistakesMade = 0
        correctButtons.clear()

        // Tắt tất cả các nút trong grid
        for (button in gridLayout.children) {
            (button as Button).isEnabled = false
            (button as Button).setBackgroundColor(Color.WHITE)
        }

        var colorsDisplayed = 0
        while (colorsDisplayed < numberOfColors) {
            val i = random.nextInt(gridSize)
            val j = random.nextInt(gridSize)
            val button = buttons[i][j]!!
            if (!correctButtons.contains(button)) {
                button.setBackgroundColor(Color.YELLOW)
                correctButtons.add(button)
                colorsDisplayed++
            }
        }

        handler.postDelayed({
            // Bật lại tất cả các nút và xóa màu
            for (button in gridLayout.children) {
                (button as Button).isEnabled = true
                (button as Button).setBackgroundColor(Color.WHITE)
            }
            correctButtons.forEach { it.setBackgroundColor(Color.WHITE) }
            isDisplayingColors = false
        }, 3000)
    }

    private fun checkButton(button: Button) {
        if (!isGameActive) {
            return
        }
        if (button in correctButtons && !buttonStates[button]!!) { // Kiểm tra nếu nút chưa được bấm đúng trước đó
            button.setBackgroundColor(Color.GREEN)
            buttonStates[button] = true
            correctPresses++
            score += 1

            if (correctPresses == totalColorsToShow) {
                Toast.makeText(this, "Great! Starting next round...", Toast.LENGTH_SHORT).show()
                resetGame()
            }
        } else {
            if (buttonStates[button] == null || !buttonStates[button]!!) { // Kiểm tra nếu nút chưa được bấm đúng trước đó
                button.setBackgroundColor(Color.RED)
                mistakesMade++

                if (mistakesMade > allowedMistakes) {
                    endGameWithLoss()
                }
            }
        }
        updateScore()
    }

    private fun endGameWithLoss() {
        isGameActive = false
        Toast.makeText(this, "You Lost! Too many mistakes.", Toast.LENGTH_LONG).show()
    }

    private fun checkForWin() {
        if (buttonStates.values.all { it }) {
            Toast.makeText(this, "Congratulations! You won!", Toast.LENGTH_LONG).show()
            resetGame()
        }
    }

    private fun resetGame() {
        if (mistakesMade <= allowedMistakes) {
            correctButtons.clear()
            buttonStates.clear()
            correctPresses = 0

            for (button in gridLayout.children) {
                (button as Button).setBackgroundColor(Color.WHITE)
                buttonStates[button] = false
            }
            mistakesMade = 0 // Reset số lỗi
            displayRandomColors() // Hiển thị lại các màu ngẫu nhiên
        } else {
            // Có thể hiển thị thông báo hoặc thực hiện hành động khác nếu cần
            Toast.makeText(this, "Can't reset, too many mistakes made!", Toast.LENGTH_LONG).show()
        }
    }

    private fun exitGame() {
        finish()
    }

    private fun restartGame() {
        finish()
        startActivity(intent)
    }

    private fun updateScore() {
        // Hiển thị điểm lên TextView
        scoreTextView.text = "Score: $score"

        // Khi cập nhật điểm số
        if (score > highScore) {
            // Cập nhật điểm số cao nhất
            highScore = score

            // Lưu điểm số cao nhất vào SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putInt("highScore", highScore)
            editor.apply()

            // Cập nhật hiển thị điểm số cao nhất
            highScoreTextView.text = "High Score: $highScore"
        }
    }
}
