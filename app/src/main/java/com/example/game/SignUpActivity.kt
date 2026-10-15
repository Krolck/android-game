package com.example.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class SignUpActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvGoToLogin = findViewById<TextView>(R.id.tvGoToLogin)

        btnSignUp.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                tvErrorMessage.text = getString(R.string.error_empty_signup_fields)
                tvErrorMessage.visibility = View.VISIBLE
            } else if (password != confirmPassword) {
                tvErrorMessage.text = getString(R.string.error_passwords_do_not_match)
                tvErrorMessage.visibility = View.VISIBLE
            } else {
                tvErrorMessage.visibility = View.GONE
                val intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("EXTRA_USERNAME", username)
                }
                startActivity(intent)
            }
        }

        tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
