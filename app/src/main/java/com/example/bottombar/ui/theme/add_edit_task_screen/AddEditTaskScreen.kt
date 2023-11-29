package com.example.bottombar.ui.theme.add_edit_task_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bottombar.ui.theme.DeadlinePicker
import com.example.bottombar.ui.theme.screen_events.AddEditScreenEvent
import com.example.bottombar.ui.theme.screen_events.FieldChangeEvent
import com.example.bottombar.util.UiEventForUser

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    onPopStackBack: () -> Unit,
    viewModel: AddEditTaskViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()
    val openDialog = remember { mutableStateOf(false) }
    DeadlinePicker(
        openDialog = openDialog,
        deadlineMillis = viewModel.deadlineMillis,
        onConfirm = viewModel::onEvent
    )

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEventForUser.PopBackStack -> onPopStackBack()
                else -> Unit
            }
        }

    }

    Scaffold(
        modifier = Modifier.scrollable(scrollState, Orientation.Vertical),
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(65.dp)
                    .clip(RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp)),
                cutoutShape = CircleShape,
                elevation = 22.dp,
                backgroundColor = MaterialTheme.colorScheme.secondary
            ) {

            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(65.dp),
                backgroundColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                onClick = {
                    viewModel.onEvent(AddEditScreenEvent.OnConfirmTaskClick)
                }
            ) {
                if (viewModel.task?.isDone == true) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "restore a task")
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "confirm add a task",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                }
            }
        }
    ) { padding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(vertical = 64.dp)
        ) {
            item {
                TextField(
                    modifier = Modifier.fillMaxWidth(0.75f),
                    value = viewModel.title,
                    onValueChange = {
                        viewModel.onEvent(FieldChangeEvent.OnTitleChange(it))
                    },
                    placeholder = {
                        Text(text = "Title")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                TextField(
                    modifier = Modifier.fillMaxWidth(0.75f),
                    value = viewModel.description,
                    onValueChange = {
                        viewModel.onEvent(AddEditScreenEvent.OnDescriptionChange(it))
                    },
                    placeholder = {
                        Text(text = "Description")
                    },
                    singleLine = false
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                androidx.compose.material3.Button(
                    onClick = {
                        viewModel.onEvent(AddEditScreenEvent.OnNewSubtaskClick)
                    }
                ) {
                    Text(text = "Add subtask:")
                }
            }

            items(viewModel.subtasks) {
                SubtaskList(onEvent = viewModel::onEvent, subtask = it)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .size(36.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(8.dp)
                        .clickable {
                            openDialog.value = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = viewModel.deadline.ifBlank { "Deadline" },
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                PriorityRow(
                    priority = viewModel.priority,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }

}