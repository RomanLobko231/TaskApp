package com.example.bottombar.ui.theme.screen_events

sealed class FieldChangeEvent {
    data class OnPriorityChange(val priority: Int) : FieldChangeEvent()
    data class OnDeadlineChange(val deadline: Long): FieldChangeEvent()
    data class OnTitleChange(val newTitle: String): FieldChangeEvent()
}