package com.example.bottombar.ui.theme.main_task_screen.public_main_task_screen.task_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottombar.data.firebase.FirestoreProjectRepository
import com.example.bottombar.data.firebase.Project
import com.example.bottombar.data.room.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicTasksViewModel @Inject constructor(
    firestoreProjectRepository: FirestoreProjectRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    var title by mutableStateOf("")
        private set

    var deadline by mutableStateOf("")
        private set

    var priority by mutableIntStateOf(0)
        private set

    var project by mutableStateOf<Project?>(null)
        private set

    var taskList by mutableStateOf(emptyList<Task>())
        private set

    init {
        val projectId = savedStateHandle.get<String>("projectId")
        projectId?.let {
            viewModelScope.launch {
                firestoreProjectRepository.getProjectById(projectId)?.let {
                    title = it.projectName
                    deadline = it.deadline
                    priority = it.priority
                    taskList = it.tasksList
                    this@PublicTasksViewModel.project = it
                }
            }
        }
    }
}