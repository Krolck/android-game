package com.example.game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class GameActivity : ComponentActivity() {

    private lateinit var tvMoney: TextView
    private lateinit var tvPerClick: TextView
    private lateinit var tvPerSec: TextView
    private lateinit var btnTap: Button

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateUi()
            handler.postDelayed(this, 500L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        GameManager.startPassiveTimer()

        tvMoney = findViewById(R.id.tvMoney)
        tvPerClick = findViewById(R.id.tvPerClick)
        tvPerSec = findViewById(R.id.tvPerSec)
        btnTap = findViewById(R.id.btnTap)

        val btnOpenShop = findViewById<Button>(R.id.btnOpenShop)
        val btnBackHome = findViewById<Button>(R.id.btnBackHome)

        btnTap.setOnClickListener {
            GameManager.addClickMoney()
            updateUi()
        }

        btnOpenShop.setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }

        btnBackHome.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUi()
        handler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
    }

    private fun updateUi() {
        tvMoney.text = getString(R.string.money_format, GameManager.money)
        tvPerClick.text = getString(R.string.per_click_format, GameManager.moneyPerClick)
        tvPerSec.text = getString(R.string.passive_rate_format, GameManager.moneyPerSecond)
        btnTap.text = getString(R.string.tap_button_format, GameManager.moneyPerClick)
    }
}
