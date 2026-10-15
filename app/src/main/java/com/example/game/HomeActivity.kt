package com.example.game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val username = intent.getStringExtra("EXTRA_USERNAME") ?: "User"

        val tvWelcomeUser = findViewById<TextView>(R.id.tvWelcomeUser)
        val btnPlayGame = findViewById<Button>(R.id.btnPlayGame)
        val btnOpenShop = findViewById<Button>(R.id.btnOpenShop)
        val btnGoToProfile = findViewById<Button>(R.id.btnGoToProfile)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        tvWelcomeUser.text = getString(R.string.welcome_user_format, username)

        btnPlayGame.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        btnOpenShop.setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }

        btnGoToProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("EXTRA_USERNAME", username)
            }
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}
