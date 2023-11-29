package com.example.bottombar.ui.theme.main_task_screen.private_main_task_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottombar.data.room.TaskRepository
import com.example.bottombar.ui.theme.screen_events.MainScreenEvent
import com.example.bottombar.util.Screen
import com.example.bottombar.util.UiEventForUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel(){

    val tasksList = repository.getActiveTasks()

    private val _uiEvent = Channel<UiEventForUser>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: MainScreenEvent){
        when(event){
            is MainScreenEvent.OnAddTaskClick -> {
                sendUiEvent(UiEventForUser.Navigate(Screen.AddEditScreen.route))
            }
            is MainScreenEvent.OnPublicTabClick -> {
                sendUiEvent(UiEventForUser.Navigate(Screen.PublicProjectsMainScreen.route))
            }
            is MainScreenEvent.OnDoneClick -> {
                viewModelScope.launch {
                    repository.insertTask(
                        event.task.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
            is MainScreenEvent.OnTaskClick -> {
                sendUiEvent(UiEventForUser.Navigate(Screen.AddEditScreen.route + "?taskId=${event.task.id}"))
            }
            is MainScreenEvent.OnDoneTasksIconClick -> {
                sendUiEvent(UiEventForUser.Navigate(Screen.DoneTasksScreen.route))
            }
        }
    }

    private fun sendUiEvent(event: UiEventForUser){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}