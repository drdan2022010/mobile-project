package com.example.mymemorygame01

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mymemorygame01.R

class Game04_1 : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout
    private lateinit var numbers: MutableList<MutableList<Int>>
    private lateinit var modifiedNumber: Pair<Int, Int>
    private lateinit var refreshButton: Button
    private lateinit var returnButton: Button
    private lateinit var pointsTextView: TextView
    private var points: Int = 0
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game04_1)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tableLayout = findViewById(R.id.tableLayout)
        refreshButton = findViewById(R.id.refreshButton)
        pointsTextView = findViewById(R.id.pointsTextView)

        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        refreshButton.setOnClickListener {
            generateNumbers()
            displayNumbers()
        }


        generateNumbers()
        displayNumbers()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun generateNumbers() {
        numbers = mutableListOf()
        val modifiedRow = (0 until TABLE_SIZE).random()
        val modifiedColumn = (0 until TABLE_SIZE).random()
        modifiedNumber = Pair(modifiedRow, modifiedColumn)
        val modifiedValue = (0..9).random()
        for (i in 0 until TABLE_SIZE) {
            val row = mutableListOf<Int>()
            for (j in 0 until TABLE_SIZE) {
                if (i == modifiedRow && j == modifiedColumn) {
                    row.add((0..9).filter { it != modifiedValue }.random())
                } else {
                    row.add(modifiedValue)
                }
            }
            numbers.add(row)
        }
    }

    private fun displayNumbers() {
        tableLayout.removeAllViews()
        for (i in 0 until TABLE_SIZE) {
            val tableRow = TableRow(this)
            for (j in 0 until TABLE_SIZE) {
                val cell = TextView(this)
                cell.text = numbers[i][j].toString()
                cell.gravity = Gravity.CENTER
                cell.setPadding(16, 16, 16, 16)
                cell.setOnClickListener {
                    if (i == modifiedNumber.first && j == modifiedNumber.second) {
                        Toast.makeText(this, "Congratulations! You found the modified number.", Toast.LENGTH_SHORT).show()
                        points += 100
                        pointsTextView.text = "Points: $points"
                        mediaPlayer.start()
                        generateNumbers()
                        displayNumbers()
                    } else {
                        Toast.makeText(this, "Oops! Try again.", Toast.LENGTH_SHORT).show()
                    }
                }
                tableRow.addView(cell)
            }
            tableLayout.addView(tableRow)
        }
    }

    companion object {
        private const val TABLE_SIZE = 8
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}