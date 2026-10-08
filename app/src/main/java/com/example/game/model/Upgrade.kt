package com.example.game.model

import kotlin.math.pow

data class Upgrade(
    val id: String,
    val name: String,
    val description: String,
    val baseCost: Int,
    val clickBonus: Int = 0,
    val passiveBonus: Int = 0,
    val level: Int = 0,
) {
    val currentCost: Int
        get() = (baseCost * 1.5.pow(level)).toInt()
}
