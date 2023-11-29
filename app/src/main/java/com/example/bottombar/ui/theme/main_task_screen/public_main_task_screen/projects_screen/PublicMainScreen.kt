package com.example.bottombar.ui.theme.main_task_screen.public_main_task_screen.projects_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bottombar.presentation.sign_in.UserData
import com.example.bottombar.ui.theme.DeadlinePicker
import com.example.bottombar.ui.theme.screen_events.BottomSheetEvent
import com.example.bottombar.ui.theme.add_edit_task_screen.PriorityRow
import com.example.bottombar.ui.theme.screen_events.FieldChangeEvent
import com.example.bottombar.ui.theme.screen_events.PublicMainScreenEvent
import com.example.bottombar.util.UiEventForUser
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicMainScreen(
    onNavigate: (UiEventForUser.Navigate) -> Unit,
    mainScreenViewModel: PublicMainScreenViewModel = hiltViewModel(),
    sheetViewModel: BottomSheetViewModel = hiltViewModel(),
    userData: UserData
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    //val projects by mainScreenViewModel.projectsList.collectAsState(initial = emptyList())
    val userProjects by mainScreenViewModel.userProjects.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = true) {
        mainScreenViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEventForUser.Navigate -> {
                    onNavigate(event)
                }
                is UiEventForUser.OpenBottomSheet -> {
                    openBottomSheet = true
                }
                else -> Unit
            }
        }
    }

    val openDialog = remember { mutableStateOf(false) }
    DeadlinePicker(
        openDialog = openDialog,
        deadlineMillis = sheetViewModel.deadlineMillis,
        onConfirm = sheetViewModel::onEvent
    )

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
            modifier = Modifier.fillMaxHeight(0.5f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.SpaceAround
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(0.75f),
                    value = sheetViewModel.title,
                    onValueChange = {
                        sheetViewModel.onEvent(FieldChangeEvent.OnTitleChange(it))
                    },
                    placeholder = {
                        Text(text = "Title")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                PriorityRow(priority = sheetViewModel.priority, onEvent = sheetViewModel::onEvent)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(0.75f),
                    onClick = {
                        openDialog.value = true
                    }
                ){
                    Text(text = sheetViewModel.deadline.ifBlank { "Deadline" })
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    sheetViewModel.onEvent(BottomSheetEvent.OnConfirmProjectClick)
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            openBottomSheet = false
                        }
                    }
                }) {
                    Text(text = "Save project")
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }


    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                },
                title = {
                    Text(text = " Hi, ${userData.username}!")
                },
                actions = {
                    IconButton(onClick = { mainScreenViewModel.onEvent(PublicMainScreenEvent.OnProfileClick) }) {
                        AsyncImage(
                            model = userData.profilePictureUrl,
                            contentDescription = "profile picture",
                            modifier = Modifier.clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
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
                    onClick = { }
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
                    openBottomSheet = !openBottomSheet
                },
                backgroundColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Task",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 8.dp, 0.dp, 0.dp)
        ) {
            TabRow(selectedTabIndex = 1) {
                Tab(
                    selected = false,
                    onClick = { mainScreenViewModel.onEvent(PublicMainScreenEvent.OnPrivateTabClick) },
                    text = { Text(text = "Private") },
                    )
                Tab(
                    selected = true,
                    onClick = { },
                    text = { Text(text = "Public") },
                    )
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(0.dp, 0.dp, 0.dp, 6.dp)
                    .clip(RoundedCornerShape(0.dp, 0.dp, 32.dp, 32.dp))
            ){
                items(userProjects){ project ->
                    ProjectItem(
                        modifier = Modifier
                            .clickable {
                            mainScreenViewModel.onEvent(PublicMainScreenEvent.OnProjectClick(project))
                        },
                        project,
                        mainScreenViewModel::onEvent,
                        sheetViewModel::onEvent
                    )
                }
            }
        }
    }
}


