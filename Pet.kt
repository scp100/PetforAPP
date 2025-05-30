package com.example.core.model

import java.time.LocalDateTime

enum class PetState { WALK, EAT, STARVE, DEAD }

data class Pet(
    var name: String,
    var type: String,
    var level: Int = 1,
    var exp: Int = 0,
    var hunger: Int = 20,
    var happiness: Int = 80,
    var health: Int = 100,
    var lastFed: LocalDateTime = LocalDateTime.now()
) {
    fun gainExp(amount: Int) {
        exp += amount
        happiness = (happiness + amount / 5).coerceAtMost(100)
        checkLevelUp()
    }

    private fun checkLevelUp() {
        val needed = level * 100
        if (exp >= needed) {
            level += 1
            exp -= needed
            happiness = (happiness + 20).coerceAtMost(100)
        }
    }

    fun feed() {
        hunger = (hunger - 30).coerceAtLeast(0)
        happiness = (happiness + 15).coerceAtMost(100)
        health = (health + 10).coerceAtMost(100)
        lastFed = LocalDateTime.now()
    }

    fun timePass() {
        hunger = (hunger + 5).coerceAtMost(100)
        if (hunger > 80) {
            happiness = (happiness - 10).coerceAtLeast(0)
            health = (health - 5).coerceAtLeast(0)
        }
    }

    val state: PetState
      get() = when {
        health <= 0 || happiness <= 0 -> PetState.DEAD
        hunger < 20                  -> PetState.EAT
        hunger > 80                  -> PetState.STARVE
        else                         -> PetState.WALK
      }
}
