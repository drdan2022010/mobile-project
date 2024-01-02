package com.example.logintest

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var usernameInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var progressDialog: ProgressDialog

    private val users = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        usernameInputLayout = findViewById(R.id.usernameInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait...")

        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", "")
        val savedPassword = sharedPreferences.getString("password", "")

        if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            users[savedUsername] = savedPassword
            usernameEditText.setText(savedUsername)
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                usernameInputLayout.error = null
                passwordInputLayout.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                if (username.isEmpty()) {
                    usernameInputLayout.error = "Please enter your username"
                }
                if (password.isEmpty()) {
                    passwordInputLayout.error = "Please enter your password"
                }
                return@setOnClickListener
            }

            progressDialog.show()
            loginButton.isEnabled = false
            registerButton.isEnabled = false

            if (users[username] == password) {
                progressDialog.dismiss()
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, math::class.java)
                startActivity(intent)
                finish()
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }

            passwordEditText.text.clear()
            loginButton.isEnabled = true
            registerButton.isEnabled = true
        }

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                if (username.isEmpty()) {
                    usernameInputLayout.error = "Please enter your username"
                }
                if (password.isEmpty()) {
                    passwordInputLayout.error = "Please enter your password"
                }
                return@setOnClickListener
            }

            progressDialog.show()
            loginButton.isEnabled = false
            registerButton.isEnabled = false

            if (users.containsKey(username)) {
                progressDialog.dismiss()
                Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show()
            } else {
                users[username] = password
                sharedPreferences.edit().putString("username", username).putString("password", password).apply()
                progressDialog.dismiss()
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                usernameEditText.text.clear()
                passwordEditText.text.clear()
                usernameEditText.clearFocus()
                passwordEditText.clearFocus()
            }

            passwordEditText.text.clear()
            loginButton.isEnabled = true
            registerButton.isEnabled = true
        }
    }
}