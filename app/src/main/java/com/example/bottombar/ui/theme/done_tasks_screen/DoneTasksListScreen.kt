package com.example.bottombar.ui.theme.done_tasks_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bottombar.ui.theme.main_task_screen.private_main_task_screen.TaskItem
import com.example.bottombar.ui.theme.screen_events.DoneTasksScreenEvent
import com.example.bottombar.util.UiEventForUser

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DoneTasksScreen(
    onNavigate: (UiEventForUser.Navigate) -> Unit,
    viewModel: DoneTasksScreenViewModel = hiltViewModel()
) {

    val doneTasks = viewModel.doneTasks.collectAsState(emptyList())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEventForUser.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(65.dp)
                    .clip(RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp)),
                cutoutShape = CircleShape,
                elevation = 22.dp,
                backgroundColor = MaterialTheme.colorScheme.secondary
            ) {
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "done tasks"
                    )
                }

                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onEvent(DoneTasksScreenEvent.OnMainTaskScreenClick) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "done tasks"
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onEvent(DoneTasksScreenEvent.OnMainTaskScreenClick) }
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "done tasks",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(doneTasks.value) { doneTask ->
                    TaskItem(
                        task = doneTask,
                        onDoneEvent = viewModel::onEvent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.onEvent(DoneTasksScreenEvent.OnTaskClick(doneTask))
                            }
                    )

                }

            }
        }

    }
}