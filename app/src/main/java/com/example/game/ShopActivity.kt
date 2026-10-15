package com.example.game

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class ShopActivity : ComponentActivity() {

    private lateinit var tvShopMoney: TextView
    private lateinit var llUpgradesContainer: LinearLayout

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateUi()
            handler.postDelayed(this, 1000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        tvShopMoney = findViewById(R.id.tvShopMoney)
        llUpgradesContainer = findViewById(R.id.llUpgradesContainer)

        val btnBackToGame = findViewById<Button>(R.id.btnBackToGame)
        btnBackToGame.setOnClickListener {
            finish()
        }

        populateUpgradesList()
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

    private fun populateUpgradesList() {
        llUpgradesContainer.removeAllViews()
        val inflater = LayoutInflater.from(this)

        for (upgrade in GameManager.upgrades) {
            val itemView = inflater.inflate(R.layout.item_upgrade, llUpgradesContainer, false)

            val tvUpgradeName = itemView.findViewById<TextView>(R.id.tvUpgradeName)
            val tvUpgradeLevel = itemView.findViewById<TextView>(R.id.tvUpgradeLevel)
            val tvUpgradeDesc = itemView.findViewById<TextView>(R.id.tvUpgradeDesc)
            val tvUpgradeCost = itemView.findViewById<TextView>(R.id.tvUpgradeCost)
            val btnBuy = itemView.findViewById<Button>(R.id.btnBuy)

            tvUpgradeName.text = upgrade.name
            tvUpgradeLevel.text = getString(R.string.upgrade_level_format, upgrade.level)
            tvUpgradeDesc.text = upgrade.description
            val cost = upgrade.currentCost
            tvUpgradeCost.text = getString(R.string.upgrade_cost_format, cost)

            val canAfford = GameManager.money >= cost
            btnBuy.isEnabled = canAfford

            btnBuy.setOnClickListener {
                if (GameManager.buyUpgrade(upgrade.id)) {
                    updateUi()
                    populateUpgradesList()
                }
            }

            llUpgradesContainer.addView(itemView)
        }
    }

    private fun updateUi() {
        tvShopMoney.text = getString(R.string.money_format, GameManager.money)

        for (i in 0 until llUpgradesContainer.childCount) {
            val itemView = llUpgradesContainer.getChildAt(i)
            if (i in GameManager.upgrades.indices) {
                val upgrade = GameManager.upgrades[i]
                val tvUpgradeLevel = itemView.findViewById<TextView>(R.id.tvUpgradeLevel)
                val tvUpgradeCost = itemView.findViewById<TextView>(R.id.tvUpgradeCost)
                val btnBuy = itemView.findViewById<Button>(R.id.btnBuy)

                tvUpgradeLevel.text = getString(R.string.upgrade_level_format, upgrade.level)
                val cost = upgrade.currentCost
                tvUpgradeCost.text = getString(R.string.upgrade_cost_format, cost)
                btnBuy.isEnabled = GameManager.money >= cost
            }
        }
    }
}
