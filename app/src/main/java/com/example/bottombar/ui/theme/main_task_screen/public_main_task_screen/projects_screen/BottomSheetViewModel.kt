package com.example.bottombar.ui.theme.main_task_screen.public_main_task_screen.projects_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottombar.data.firebase.FirestoreProjectRepository
import com.example.bottombar.data.firebase.FirestoreUserRepository
import com.example.bottombar.data.firebase.Project
import com.example.bottombar.ui.theme.screen_events.BottomSheetEvent
import com.example.bottombar.ui.theme.screen_events.FieldChangeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val firestoreProjectRepository: FirestoreProjectRepository,
    private val firestoreUserRepository: FirestoreUserRepository
) : ViewModel() {

    var title by mutableStateOf("")
        private set

    var deadline by mutableStateOf("")
        private set

    var priority by mutableIntStateOf(0)
        private set

    var id by mutableStateOf("")
        private set

    var deadlineMillis by mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
        private set



    fun onEvent(event: FieldChangeEvent) {
        when (event) {
            is FieldChangeEvent.OnTitleChange -> {
                title = event.newTitle
            }
            is FieldChangeEvent.OnDeadlineChange -> {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val deadlineDate = Date(event.deadline)
                deadline = dateFormat.format(deadlineDate)
            }

            is FieldChangeEvent.OnPriorityChange -> {
                priority = event.priority
            }

            is BottomSheetEvent.OnConfirmProjectClick -> {
                viewModelScope.launch {
                    if (id.isNotBlank()) {
                        firestoreProjectRepository.updateProject(Project(title, deadline, priority, id = id))
                    } else{
                        val id = firestoreProjectRepository.getIdForNewProject()
                        firestoreProjectRepository.saveProject(Project(title, deadline, priority, Clock.System.now().toEpochMilliseconds(), id))
                        firestoreUserRepository.addProjectId(id)
                    }
                }
                title = ""
                deadline = ""
                priority = 0
                id = ""
            }
            is BottomSheetEvent.OnEditProjectClick -> {
                title = event.project.projectName
                deadline = event.project.deadline
                priority = event.project.priority
                id = event.project.id
            }

            else -> {}
        }
    }
}