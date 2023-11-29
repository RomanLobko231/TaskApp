package com.example.bottombar.data.firebase

data class User(
    val id: String = "",
    val username: String = "",
    val projectsIds: List<String> = emptyList()
)
