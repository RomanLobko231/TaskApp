package com.example.bottombar.data.firebase

import com.example.bottombar.data.room.Task

data class Project(
    val projectName: String = "Project",
    val deadline: String = "Deadline",
    val priority: Int = 0,
    val timestamp: Long = 0,
    val id: String = "",
    val tasksList: List<Task> = emptyList()
)
