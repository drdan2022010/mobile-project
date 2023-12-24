package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val listWords = ListOfWords.words
    private lateinit var tvAlphabet: TextView
    private lateinit var etAnswer: EditText
    private lateinit var btnCheck: Button
    private lateinit var tvResult: TextView
    private lateinit var btnExit: Button
    private var currentAlphabet: Char = ' '
    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAlphabet = findViewById(R.id.tv_alphabet)
        etAnswer = findViewById(R.id.et_answer)
        btnCheck = findViewById(R.id.btn_check)
        tvResult = findViewById(R.id.tv_result)
        btnExit = findViewById(R.id.btn_exit)

        // Khởi tạo chữ cái ngẫu nhiên
        generateRandomAlphabet()

        btnCheck.setOnClickListener {
            checkAnswer()
        }

        btnExit.setOnClickListener {
            finish()
        }
    }

    private fun generateRandomAlphabet() {
        currentAlphabet = ('a'..'z').random()
        tvAlphabet.text = "Chữ cái: $currentAlphabet"
    }

    private fun checkAnswer() {
        val answer = etAnswer.text.toString().trim()
        if (answer.isEmpty()) {
            tvResult.text = "Bạn chưa nhập đáp án!"
            return
        }

        if (answer[0] != currentAlphabet) {
            tvResult.text = "Sai! Chữ cái đầu tiên phải là $currentAlphabet"
            return
        }

        if (listWords.contains(answer)) {
            score++
            tvResult.text = "Đúng! Điểm của bạn là: $score"
        } else {
            tvResult.text = "Sai! Từ '$answer' không có nghĩa."
        }

        generateRandomAlphabet()
        etAnswer.text.clear()
    }
}