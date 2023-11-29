package com.example.bottombar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bottombar.data.firebase.FirestoreUserRepository
import com.example.bottombar.data.firebase.User
import com.example.bottombar.presentation.sign_in.GoogleAuthUiClient
import com.example.bottombar.ui.theme.BottomBarTheme
import com.example.bottombar.ui.theme.add_edit_task_screen.AddEditScreen
import com.example.bottombar.ui.theme.authentication_screen.AuthScreen
import com.example.bottombar.ui.theme.authentication_screen.AuthScreenViewModel
import com.example.bottombar.ui.theme.done_tasks_screen.DoneTasksScreen
import com.example.bottombar.ui.theme.main_task_screen.private_main_task_screen.MainScreen
import com.example.bottombar.ui.theme.main_task_screen.public_main_task_screen.projects_screen.PublicMainScreen
import com.example.bottombar.ui.theme.main_task_screen.public_main_task_screen.task_screen.PublicTasksMainScreen
import com.example.bottombar.ui.theme.profile_screen.ProfileScreen
import com.example.bottombar.util.Screen
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val firestoreUserRepository = FirestoreUserRepository(Firebase.firestore)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BottomBarTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.PrivateMainScreen.route
                ) {
                    composable(Screen.AuthScreen.route) {
                        val viewModel = viewModel<AuthScreenViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if (result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }

                            }
                        )

                        LaunchedEffect(key1 = state.isSignInSuccessful) {
                            if (state.isSignInSuccessful) {
                                if (firestoreUserRepository.getUserById(googleAuthUiClient.getSignedInUser()!!.userId) == null){
                                    firestoreUserRepository.saveUser(
                                        User(
                                            googleAuthUiClient.getSignedInUser()!!.userId,
                                            googleAuthUiClient.getSignedInUser()!!.username ?: ""
                                        )
                                    )
                                    Toast.makeText(applicationContext, "Logged In first time", Toast.LENGTH_SHORT).show()
                                }

                                navController.navigate(Screen.PublicProjectsMainScreen.route)
                            }
                        }
                        AuthScreen(
                            state = state,
                            onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )

                                }
                            }
                        )

                    }
                    composable(Screen.ProfileScreen.route) {
                        ProfileScreen(
                            onSignOut = {
                                lifecycleScope.launch {
                                    googleAuthUiClient.signOut()
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed out!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(Screen.PrivateMainScreen.route)
                                }

                            })
                    }
                    composable(Screen.PrivateMainScreen.route) {
                        MainScreen(
                            onNavigate = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                    composable(Screen.PublicProjectsMainScreen.route) {

                        LaunchedEffect(key1 = Unit) {
                            if (googleAuthUiClient.getSignedInUser() == null) {
                                navController.navigate(Screen.AuthScreen.route)
                            }
                        }
                        googleAuthUiClient.getSignedInUser()?.let { it1 ->
                            PublicMainScreen(
                                onNavigate = {
                                    navController.navigate(it.route)
                                },
                                userData = it1
                            )
                        }
                    }
                    composable(
                        route = Screen.PublicTasksMainScreen.route + "?projectId={projectId}",
                        arguments = listOf(
                            navArgument(name = "projectId") {
                                type = NavType.StringType
                                defaultValue = ""
                            }
                        )
                    ) {
                        PublicTasksMainScreen()
                    }
                    composable(Screen.DoneTasksScreen.route) {
                        DoneTasksScreen(
                            onNavigate = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                    composable(
                        route = Screen.AddEditScreen.route + "?taskId={taskId}",
                        arguments = listOf(
                            navArgument(name = "taskId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        AddEditScreen(onPopStackBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}


