package com.code2college.moneygame

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.code2college.moneygame.model.Upgrade
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

object GameManager {
    var currentUsername: String? = null
    var currentAccountId: String? = null
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

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val handler = Handler(Looper.getMainLooper())
    private var isTimerRunning = false

    private val passiveRunnable = object : Runnable {
        override fun run() {
            addPassiveMoney()
            handler.postDelayed(this, 1000L)
        }
    }

    private val autoSaveRunnable = object : Runnable {
        override fun run() {
            saveUserDataToFirestore()
            handler.postDelayed(this, 10000L)
        }
    }

    fun startPassiveTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true
            handler.postDelayed(passiveRunnable, 1000L)
            handler.postDelayed(autoSaveRunnable, 10000L)
        }
    }

    fun loadUserData(username: String, accountId: String?, savedMoney: Int, upgradeLevels: Map<String, Int>) {
        currentUsername = username
        currentAccountId = accountId ?: username
        money = savedMoney

        for (i in upgrades.indices) {
            val upgrade = upgrades[i]
            val level = upgradeLevels[upgrade.id] ?: 0
            upgrades[i] = upgrade.copy(level = level)
        }
    }

    fun resetUserData(username: String, accountId: String?) {
        currentUsername = username
        currentAccountId = accountId ?: username
        money = 0
        for (i in upgrades.indices) {
            upgrades[i] = upgrades[i].copy(level = 0)
        }
    }

    fun saveUserDataToFirestore(onComplete: (() -> Unit)? = null) {
        val username = currentUsername ?: run {
            onComplete?.invoke()
            return
        }

        val upgradesMap = upgrades.associate { it.id to it.level }

        val dataToSave = mapOf<String, Any>(
            "money" to money,
            "upgrades" to upgradesMap
        )

        db.collection("Accounts").document(username)
            .set(dataToSave, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("GameManager", "Game data saved successfully for $username")
                onComplete?.invoke()
            }
            .addOnFailureListener { e ->
                Log.e("GameManager", "Error saving data: ${e.message}")
                onComplete?.invoke()
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
                saveUserDataToFirestore()
                return true
            }
        }
        return false
    }
}
