package com.example.mymemorygame01

import android.animation.ValueAnimator
import android.graphics.Rect
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mymemorygame01.R
import kotlin.math.sqrt

class Game04_3 : AppCompatActivity() {

    private lateinit var boat: ImageView
    private lateinit var sharks: Array<ImageView?>
    private lateinit var scoreTextView: TextView
    private var score: Int = 0
    private val sharkCollided: MutableSet<ImageView> = mutableSetOf()
    private lateinit var toolbar: Toolbar
    private lateinit var returnButton: Button
    private lateinit var scoreIncreaseSound: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game04_3)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        returnButton = findViewById(R.id.returnButton)
        returnButton.setOnClickListener {
            // Handle return button click event
            onBackPressed()
        }
        scoreIncreaseSound = MediaPlayer.create(this, R.raw.sound)


        // Initialize the ImageView objects
        boat = findViewById(R.id.boat)
        sharks = arrayOfNulls(5)
        sharks[0] = findViewById(R.id.shark1)
        sharks[1] = findViewById(R.id.shark2)
        sharks[2] = findViewById(R.id.shark3)
        sharks[3] = findViewById(R.id.shark4)
        sharks[4] = findViewById(R.id.shark5)

        // Initialize the TextView for score display
        scoreTextView = findViewById(R.id.scoreTextView)
        scoreTextView.text = "Score: $score"

        // Set click listener for the boat
        boat.setOnClickListener {
            checkPlayerCollision()
        }

        // Set click listener for the start button
        val startButton: Button = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            startAnimationLoop()
        }
    }

    private fun startAnimationLoop() {
        // Clear the previous state of the game

        val handler = Handler()
        var delay = 0L

        for (i in sharks.indices) {
            val shark = sharks[i]
            // Skip null elements in the array
            if (shark == null) continue

            handler.postDelayed({
                animateShark(shark)
            }, delay)

            delay += 3000 // 3 seconds delay between each shark
        }
    }

    private fun animateShark(shark: ImageView) {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 15000 // Adjust the duration to 15 seconds
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float

            // Get the coordinates of the boat
            val boatX = boat.x
            val boatY = boat.y

            // Calculate the new position of the shark
            val newSharkX = shark.x + (boatX - shark.x) * progress
            val newSharkY = shark.y + (boatY - shark.y) * progress

            // Update the position of the shark
            shark.x = newSharkX
            shark.y = newSharkY

            // Check if the shark touches the boat
            if (!sharkCollided.contains(shark) && shark.visibility == View.VISIBLE) {
                val distance = calculateDistance(boat.x, boat.y, shark.x, shark.y)
                if (distance <= 60) {
                    decreaseScore(1)
                    sharkCollided.add(shark)
                    shark.visibility = View.INVISIBLE
                }
            }
        }
        animator.start()
    }

    private fun checkPlayerCollision() {
        for (i in sharks.indices) {
            val shark = sharks[i]
            // Skip null elements in the array
            if (shark == null) continue

            // Check if the shark is visible and not already collided
            if (!sharkCollided.contains(shark) && shark.visibility == View.VISIBLE) {
                val distance = calculateDistance(boat.x, boat.y, shark.x, shark.y)
                if (distance <= dpToPx(200)) {
                    increaseScore(10)
                    sharkCollided.add(shark)
                    shark.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x2 - x1
        val dy = y2 - y1
        return sqrt(dx * dx + dy * dy)
    }

    private fun dpToPx(dp: Int): Float {
        val scale = resources.displayMetrics.density
        return dp * scale
    }

    private fun decreaseScore(points: Int) {
        score -= points
        scoreTextView.text = "Score: $score"
    }

    private fun increaseScore(points: Int) {
        score += points
        scoreTextView.text = "Score: $score"
        scoreIncreaseSound.start()

    }
}