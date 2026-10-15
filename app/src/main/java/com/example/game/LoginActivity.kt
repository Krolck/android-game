package com.example.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvGoToSignUp = findViewById<TextView>(R.id.tvGoToSignUp)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                tvErrorMessage.text = getString(R.string.error_empty_login_fields)
                tvErrorMessage.visibility = View.VISIBLE
            } else {
                tvErrorMessage.visibility = View.GONE
                val intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("EXTRA_USERNAME", username)
                }
                startActivity(intent)
            }
        }

        tvGoToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }
}
