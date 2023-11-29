package com.example.bottombar.ui.theme.main_task_screen.public_main_task_screen.projects_screen

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottombar.data.firebase.FirestoreProjectRepository
import com.example.bottombar.data.firebase.FirestoreUserRepository
import com.example.bottombar.data.firebase.User
import com.example.bottombar.ui.theme.screen_events.PublicMainScreenEvent
import com.example.bottombar.util.Screen
import com.example.bottombar.util.UiEventForUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicMainScreenViewModel @Inject constructor(
    private val firestoreProjectRepository: FirestoreProjectRepository,
    private val firestoreUserRepository: FirestoreUserRepository
) : ViewModel() {

    val userProjects = firestoreProjectRepository.fetchUserProjects()
    //val projectsList = firestoreProjectRepository.getProjects()


    private val _uiEvent = Channel<UiEventForUser>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: PublicMainScreenEvent) {
        when (event) {
            is PublicMainScreenEvent.OnPrivateTabClick -> {
                sendUiEvent(UiEventForUser.Navigate(Screen.PrivateMainScreen.route))
            }
            is PublicMainScreenEvent.OnProfileClick -> {
                sendUiEvent(UiEventForUser.Navigate(Screen.ProfileScreen.route))
            }
            is PublicMainScreenEvent.OnNewProjectClick -> {
            }
            is PublicMainScreenEvent.OnProjectClick -> {
                sendUiEvent(UiEventForUser.Navigate(Screen.PublicTasksMainScreen.route + "?projectId=${event.project.id}"))
            }
            is PublicMainScreenEvent.OnDropDownClick -> {

            }
            is PublicMainScreenEvent.OnDeleteProjectClick -> {
                viewModelScope.launch {
                    firestoreUserRepository.deleteProjectId(event.project.id)
                    firestoreProjectRepository.deleteProjectById(event.project.id)
                }
            }
            is PublicMainScreenEvent.OnEditProjectClick -> {
                sendUiEvent(UiEventForUser.OpenBottomSheet)
            }
        }
    }

    private fun sendUiEvent(event: UiEventForUser) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}