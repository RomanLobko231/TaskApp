package com.example.bottombar.ui.theme.main_task_screen.private_main_task_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bottombar.ui.theme.screen_events.MainScreenEvent
import com.example.bottombar.util.UiEventForUser


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    onNavigate: (UiEventForUser.Navigate) -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
) {

    val tasks = viewModel.tasksList.collectAsState(initial = emptyList())
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEventForUser.Navigate -> {
                    onNavigate(event)
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            androidx.compose.material3.TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                },
                title = {
                    Text(text = " Hi! Check your tasks for today:")
                }
            )

        },
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
                Box(modifier = Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onEvent(MainScreenEvent.OnDoneTasksIconClick) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "done tasks"
                    )
                }

            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(65.dp),
                shape = CircleShape,
                onClick = {
                    viewModel.onEvent(MainScreenEvent.OnAddTaskClick)
                },
                backgroundColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Task",
                    tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = 0) {
                Tab(
                    selected = true,
                    onClick = { },
                    text = { Text(text = "Private") },

                    )
                Tab(
                    selected = false,
                    onClick = { viewModel.onEvent(MainScreenEvent.OnPublicTabClick) },
                    text = { Text(text = "Public") },

                    )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(0.dp, 0.dp, 0.dp, 6.dp)
                    .clip(RoundedCornerShape(0.dp, 0.dp, 44.dp, 44.dp))

            ) {
                items(tasks.value) { task ->
                    TaskItem(
                        task = task,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.onEvent(MainScreenEvent.OnTaskClick(task))
                            })
                }
            }
        }
    }

}