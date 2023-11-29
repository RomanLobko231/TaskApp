package com.example.bottombar.ui.theme.screen_events

import com.example.bottombar.data.room.Task

sealed class MainScreenEvent{
    object OnAddTaskClick: MainScreenEvent()
    data class OnDoneClick(val task: Task, val isDone: Boolean): MainScreenEvent()
    data class OnTaskClick(val task: Task): MainScreenEvent()
    object OnDoneTasksIconClick: MainScreenEvent()
    object OnPublicTabClick: MainScreenEvent()
}
