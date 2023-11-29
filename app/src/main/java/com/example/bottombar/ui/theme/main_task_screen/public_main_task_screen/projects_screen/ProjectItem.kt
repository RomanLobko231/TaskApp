package com.example.bottombar.ui.theme.main_task_screen.public_main_task_screen.projects_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bottombar.data.firebase.Project
import com.example.bottombar.ui.theme.screen_events.BottomSheetEvent
import com.example.bottombar.ui.theme.screen_events.PublicMainScreenEvent

@Composable
fun ProjectItem(
    modifier: Modifier = Modifier,
    project: Project,
    onMainScreenEvent: (PublicMainScreenEvent) -> Unit,
    onEdit: (BottomSheetEvent) -> Unit
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var itemHeight by remember {
        mutableStateOf(0.dp)
    }


    val density = LocalDensity.current

    ElevatedCard(
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.elevatedCardElevation(12.dp),
        modifier = Modifier
            .onSizeChanged {
                with(density) { itemHeight = it.height.toDp() }
            }
            .aspectRatio(1f)
            .padding(8.dp)
            .shadow(
                shape = RoundedCornerShape(30.dp),
                ambientColor = MaterialTheme.colorScheme.primary,
                elevation = 10.dp,
                clip = true
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row {
                Text(
                    text = project.projectName,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.weight(5f),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "update project",
                    modifier = Modifier.clickable {
                        isContextMenuVisible = true
                        // onEvent(PublicMainScreenEvent.OnDropDownClick(project))
                    }.weight(1f)
                )

                DropdownMenu(
                    expanded = isContextMenuVisible,
                    onDismissRequest = { isContextMenuVisible = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Delete project") },
                        onClick = {
                            onMainScreenEvent(PublicMainScreenEvent.OnDeleteProjectClick(project))
                            isContextMenuVisible = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Edit project") },
                        onClick = {
                            onEdit(BottomSheetEvent.OnEditProjectClick(project))
                            onMainScreenEvent(PublicMainScreenEvent.OnEditProjectClick)
                            isContextMenuVisible = false
                        })
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            project.deadline.let {
                Text(text = it, color = MaterialTheme.colorScheme.onSecondaryContainer)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row {
                project.priority.takeIf { it != 0 }?.let {
                    repeat(it) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "priority count"
                        )
                    }
                }

            }
        }

    }

//    Box(
//        modifier = Modifier
//            .onSizeChanged {
//                with(density) { itemHeight = it.height.toDp() }
//            }
//            .aspectRatio(1f)
//            .padding(8.dp)
//            .clip(RoundedCornerShape(32.dp))
//            .background(MaterialTheme.colorScheme.secondaryContainer),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = modifier
//                .fillMaxSize()
//                .padding(8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceAround
//        ) {
//            Row {
//                Text(
//                    text = project.projectName,
//                    color = MaterialTheme.colorScheme.onSecondaryContainer,
//                    modifier = Modifier.weight(4f),
//                    textAlign = TextAlign.Center
//                )
//                    Icon(
//                        imageVector = Icons.Default.Settings,
//                        contentDescription = "update project",
//                        modifier = Modifier.clickable {
//                            isContextMenuVisible = true
//                           // onEvent(PublicMainScreenEvent.OnDropDownClick(project))
//                        }
//                    )
//
//                DropdownMenu(
//                    expanded = isContextMenuVisible,
//                    onDismissRequest = { isContextMenuVisible = false }
//                ) {
//                    DropdownMenuItem(
//                        text = { Text(text = "Delete project") },
//                        onClick = {
//                            onMainScreenEvent(PublicMainScreenEvent.OnDeleteProjectClick(project))
//                            isContextMenuVisible = false
//                        })
//                    DropdownMenuItem(
//                        text = { Text(text = "Edit project") },
//                        onClick = {
//                            onEdit(BottomSheetEvent.OnEditProjectClick(project))
//                            onMainScreenEvent(PublicMainScreenEvent.OnEditProjectClick)
//                            isContextMenuVisible = false
//                        })
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//            project.deadline.let {
//                Text(text = it, color = MaterialTheme.colorScheme.onSecondaryContainer)
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//            Row {
//                project.priority.takeIf { it != 0 }?.let {
//                    repeat(it) {
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Icon(
//                            imageVector = Icons.Default.Star,
//                            contentDescription = "priority count"
//                        )
//                    }
//                }
//
//            }
//        }
//
//    }


}