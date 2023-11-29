package com.example.bottombar.util

sealed class Screen(val route: String){
    object PublicProjectsMainScreen: Screen("public_main_projects")
    object PrivateMainScreen: Screen("private_main")
    object AddEditScreen: Screen("add_edit")
    object DoneTasksScreen: Screen("done_tasks")
    object AuthScreen: Screen("auth")
    object ProfileScreen: Screen("profile")
    object PublicTasksMainScreen: Screen("public_main_tasks")
}
