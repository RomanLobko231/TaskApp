package com.example.bottombar.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title: String,
    val description: String?,
    val subtasks: List<Subtask>?,
    val deadline: String?,
    val isDone: Boolean,
    val priority: Int? = 0,
    @PrimaryKey val id: Int? = null
)
