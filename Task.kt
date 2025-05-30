package com.example.core.model

import java.time.LocalDateTime

data class Task(
    val id: Int = nextId(),
    var title: String,
    var description: String = "",
    var priority: Int,             // 1..5
    var dueDate: LocalDateTime,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var completedAt: LocalDateTime? = null
) {
    val rewardExp: Int = priority * 10
    val isCompleted: Boolean
      get() = completedAt != null

    fun complete() {
        if (completedAt == null) {
            completedAt = LocalDateTime.now()
        }
    }

    companion object {
        private var seq = 0
        private fun nextId() = ++seq
    }
}
