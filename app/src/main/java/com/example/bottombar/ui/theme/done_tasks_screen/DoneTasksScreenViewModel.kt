package com.example.bottombar.ui.theme.done_tasks_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottombar.data.room.Task
import com.example.bottombar.data.room.TaskRepository
import com.example.bottombar.ui.theme.screen_events.DoneTasksScreenEvent
import com.example.bottombar.util.Screen
import com.example.bottombar.util.UiEventForUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoneTasksScreenViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel(){

    val doneTasks = repository.getDoneTasks()

    private val _uiEvent = Channel<UiEventForUser>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: DoneTasksScreenEvent){
        when(event){
            is DoneTasksScreenEvent.OnRestoreTaskClick -> {
                viewModelScope.launch {
                    repository.insertTask(
                        Task(
                            event.task.title,
                            event.task.description,
                            event.task.subtasks,
                            event.task.deadline,
                            false,
                            event.task.priority,
                            id = event.task.id
                        )
                    )
                }
            }
            is DoneTasksScreenEvent.OnDeleteTaskClick -> {
                viewModelScope.launch {
                    repository.deleteTask(event.task)
                }
            }
            is DoneTasksScreenEvent.OnTaskClick -> {
                sendUiEvent(UiEventForUser.Navigate(Screen.AddEditScreen.route + "?taskId=${event.task.id}"))
            }
            is DoneTasksScreenEvent.OnMainTaskScreenClick -> {
                sendUiEvent(UiEventForUser.Navigate(Screen.PrivateMainScreen.route))
            }
            is DoneTasksScreenEvent.OnStatisticsClick -> {}
        }
    }

    private fun sendUiEvent(event: UiEventForUser){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}