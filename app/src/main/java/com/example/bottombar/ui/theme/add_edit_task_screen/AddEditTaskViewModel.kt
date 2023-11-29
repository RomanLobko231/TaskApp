package com.example.bottombar.ui.theme.add_edit_task_screen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottombar.data.room.Subtask
import com.example.bottombar.data.room.Task
import com.example.bottombar.data.room.TaskRepository
import com.example.bottombar.ui.theme.screen_events.AddEditScreenEvent
import com.example.bottombar.ui.theme.screen_events.FieldChangeEvent
import com.example.bottombar.util.UiEventForUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var task by mutableStateOf<Task?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var deadline by mutableStateOf("")
        private set

    var priority by mutableIntStateOf(0)
        private set

    var deadlineMillis by mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
        private set

    var subtasks by mutableStateOf(emptyList<Subtask>())

    private val _uiEvent = Channel<UiEventForUser>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val taskId = savedStateHandle.get<Int>("taskId")
        if (taskId != -1) {
            viewModelScope.launch {
                repository.getTaskById(taskId!!)?.let {
                    title = it.title
                    description = it.description ?: ""
                    deadline = it.deadline ?: ""
                    priority = it.priority ?: 0
                    subtasks = it.subtasks ?: emptyList()
                    this@AddEditTaskViewModel.task = it
                }
                deadlineMillis = if(deadline.isNotBlank()){
                    SimpleDateFormat("dd/MM/yyyy", Locale.US)
                        .parse(deadline)!!
                        .time
                        .plus(86_400_000)
                } else { Clock.System.now().toEpochMilliseconds() }
            }
        }
    }

    fun onEvent(event: FieldChangeEvent) {
        when (event) {
            is FieldChangeEvent.OnTitleChange -> {
                title = event.newTitle
            }
            is AddEditScreenEvent.OnDescriptionChange -> {
                description = event.newDescription
            }
            is FieldChangeEvent.OnDeadlineChange -> {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val deadlineDate = Date(event.deadline)
                deadline = dateFormat.format(deadlineDate)
            }
            is FieldChangeEvent.OnPriorityChange -> {
                priority = event.priority
            }
            is AddEditScreenEvent.OnNewSubtaskClick -> {
                subtasks = subtasks + Subtask("")
            }
            is AddEditScreenEvent.OnSubtaskChange -> {
                val updatedSubtasks = subtasks.toMutableList()
                val index = subtasks.indexOf(event.currentSubtask)
                if (index != -1) {
                    updatedSubtasks[index] = Subtask(event.newSubtask)
                    subtasks = updatedSubtasks
                }
            }
            is AddEditScreenEvent.OnSubtaskDeleteClick -> {
                subtasks = subtasks.filterNot { it == event.subtaskToDelete }
            }
            is AddEditScreenEvent.OnSubtaskDoneClick -> {
                val updatedSubtasks = subtasks.toMutableList()
                val index = subtasks.indexOf(event.subtask)
                if (index != -1){
                    updatedSubtasks[index] = Subtask(
                        event.subtask.subtaskText,
                        false,
                        event.subtask.id
                    )
                    subtasks = updatedSubtasks
                }
            }
            is AddEditScreenEvent.OnSubtaskRestoreClick -> {
                val updatedSubtasks = subtasks.toMutableList()
                val index = subtasks.indexOf(event.subtask)
                if (index != -1){
                    updatedSubtasks[index] = Subtask(
                        event.subtask.subtaskText,
                        true,
                        event.subtask.id
                    )
                    subtasks = updatedSubtasks
                }
            }

            is AddEditScreenEvent.OnConfirmTaskClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        // task: show snack-bar saying that title can't be empty
                        return@launch
                    }
                    repository.insertTask(
                        Task(
                            title,
                            description,
                            subtasks,
                            deadline,
                            false,
                            priority,
                            task?.id

                        )
                    )
                    sendUiEvent(UiEventForUser.PopBackStack)
                }
            }

            else -> {}
        }

    }

    private fun sendUiEvent(event: UiEventForUser) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }

    }
}

