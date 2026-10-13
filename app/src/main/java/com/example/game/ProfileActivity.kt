package com.example.game

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class ProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val username = intent.getStringExtra("EXTRA_USERNAME") ?: "John Doe"

        val tvProfileName = findViewById<TextView>(R.id.tvProfileName)
        val tvProfileEmail = findViewById<TextView>(R.id.tvProfileEmail)
        val btnBackToHome = findViewById<Button>(R.id.btnBackToHome)

        tvProfileName.text = username
        tvProfileEmail.text = "${username.lowercase().replace(" ", "")}@example.com"

        btnBackToHome.setOnClickListener {
            finish()
        }
    }
}
