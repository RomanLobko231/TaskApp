package com.example.bottombar.data.room

import com.example.bottombar.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Subtask(
    val subtaskText: String,
    val isActive: Boolean = true,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID()
)
