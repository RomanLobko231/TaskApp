package com.example.bottombar.ui.theme.main_task_screen.public_main_task_screen.task_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PublicTasksMainScreen(
    viewModel: PublicTasksViewModel = hiltViewModel()
){
Box(contentAlignment = Alignment.Center) {
    Text(text = viewModel.title)
}
}
