package com.example.game

import android.os.Handler
import android.os.Looper
import com.example.game.model.Upgrade

object GameManager {
    var money: Int = 0

    val upgrades = mutableListOf(
        Upgrade("click_1", "Better Mouse", "+1 Money per click", 10, clickBonus = 1),
        Upgrade("passive_1", "Piggy Bank", "+1 Money per second", 25, passiveBonus = 1),
        Upgrade("click_2", "Golden Touch", "+5 Money per click", 100, clickBonus = 5),
        Upgrade("passive_2", "Money Machine", "+10 Money per second", 250, passiveBonus = 10),
        Upgrade("click_3", "Diamond Cursor", "+25 Money per click", 500, clickBonus = 25),
        Upgrade("passive_3", "Crypto Rig", "+50 Money per second", 1000, passiveBonus = 50)
    )

    val moneyPerClick: Int
        get() = 1 + upgrades.sumOf { it.level * it.clickBonus }

    val moneyPerSecond: Int
        get() = upgrades.sumOf { it.level * it.passiveBonus }

    private val handler = Handler(Looper.getMainLooper())
    private var isTimerRunning = false

    private val passiveRunnable = object : Runnable {
        override fun run() {
            addPassiveMoney()
            handler.postDelayed(this, 1000L)
        }
    }

    fun startPassiveTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true
            handler.postDelayed(passiveRunnable, 1000L)
        }
    }

    fun addClickMoney() {
        money += moneyPerClick
    }

    fun addPassiveMoney() {
        if (moneyPerSecond > 0) {
            money += moneyPerSecond
        }
    }

    fun buyUpgrade(upgradeId: String): Boolean {
        val index = upgrades.indexOfFirst { it.id == upgradeId }
        if (index != -1) {
            val upgrade = upgrades[index]
            val cost = upgrade.currentCost
            if (money >= cost) {
                money -= cost
                upgrades[index] = upgrade.copy(level = upgrade.level + 1)
                return true
            }
        }
        return false
    }
}
