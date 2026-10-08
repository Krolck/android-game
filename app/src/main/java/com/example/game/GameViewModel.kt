package com.example.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.game.model.Upgrade

class GameViewModel : ViewModel() {
    var money by mutableIntStateOf(0)
        private set

    val upgrades = mutableStateListOf(
        Upgrade(
            id = "click_1",
            name = "Better Mouse",
            description = "+1 Money per click",
            baseCost = 10,
            clickBonus = 1
        ),
        Upgrade(
            id = "passive_1",
            name = "Piggy Bank",
            description = "+1 Money per second",
            baseCost = 25,
            passiveBonus = 1
        ),
        Upgrade(
            id = "click_2",
            name = "Golden Touch",
            description = "+5 Money per click",
            baseCost = 100,
            clickBonus = 5
        ),
        Upgrade(
            id = "passive_2",
            name = "Money Machine",
            description = "+10 Money per second",
            baseCost = 250,
            passiveBonus = 10
        ),
        Upgrade(
            id = "click_3",
            name = "Diamond Cursor",
            description = "+25 Money per click",
            baseCost = 500,
            clickBonus = 25
        ),
        Upgrade(
            id = "passive_3",
            name = "Crypto Rig",
            description = "+50 Money per second",
            baseCost = 1000,
            passiveBonus = 50
        )
    )

    val moneyPerClick: Int
        get() = 1 + upgrades.sumOf { it.level * it.clickBonus }

    val moneyPerSecond: Int
        get() = upgrades.sumOf { it.level * it.passiveBonus }

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
