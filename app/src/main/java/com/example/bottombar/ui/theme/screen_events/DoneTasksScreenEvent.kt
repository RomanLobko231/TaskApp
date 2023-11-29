package com.example.bottombar.ui.theme.screen_events

import com.example.bottombar.data.room.Task

sealed class DoneTasksScreenEvent{
    data class OnTaskClick(val task: Task): DoneTasksScreenEvent()
    data class OnDeleteTaskClick(val task: Task): DoneTasksScreenEvent()
    data class OnRestoreTaskClick(val task: Task): DoneTasksScreenEvent()
    object OnMainTaskScreenClick: DoneTasksScreenEvent()
    object OnStatisticsClick: DoneTasksScreenEvent()
}
