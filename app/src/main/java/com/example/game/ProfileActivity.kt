package com.example.game

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class ProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val username = intent.getStringExtra("EXTRA_USERNAME") ?: "Player"

        val tvProfileName = findViewById<TextView>(R.id.tvProfileName)
        val tvProfileHandle = findViewById<TextView>(R.id.tvProfileHandle)
        val btnBackToHome = findViewById<Button>(R.id.btnBackToHome)

        tvProfileName.text = username
        tvProfileHandle.text = getString(R.string.profile_handle_format, username.lowercase().replace(" ", "_"))

        btnBackToHome.setOnClickListener {
            finish()
        }
    }
}
