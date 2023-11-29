package com.example.bottombar.ui.theme.main_task_screen.private_main_task_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bottombar.data.room.Task
import com.example.bottombar.ui.theme.screen_events.DoneTasksScreenEvent
import com.example.bottombar.ui.theme.screen_events.MainScreenEvent

//reusing one composable for both done and main screens depending on isDone value to avoid repeating the same code twice
@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onEvent: (MainScreenEvent) -> Unit = {},
    onDoneEvent: (DoneTasksScreenEvent) -> Unit = {}
) {

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
            .aspectRatio(3f)
            .padding(8.dp)
            .shadow(
                shape = RoundedCornerShape(30.dp),
                ambientColor = MaterialTheme.colorScheme.primary,
                elevation = 10.dp,
                clip = true
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.title,
                        style = when {
                            task.isDone -> {
                                TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            }

                            else -> {
                                TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    )
                    if (task.isDone) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete task",
                            modifier = Modifier.clickable {
                                onDoneEvent(DoneTasksScreenEvent.OnDeleteTaskClick(task))
                            }
                        )

                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    task.deadline?.let {
                        Text(text = it)
                    }
                    task.priority.takeIf { it != 0 }?.let {
                        repeat(it) {
                            Spacer(modifier = Modifier.width(20.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "priority count"
                            )
                        }
                    }

                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            when {
                task.isDone -> {
                    IconButton(
                        onClick = {
                            onDoneEvent(DoneTasksScreenEvent.OnRestoreTaskClick(task))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh, "Restore task"
                        )
                    }
                }

                else -> {
                    Checkbox(
                        checked = false,
                        onCheckedChange = { isChecked ->
                            onEvent(MainScreenEvent.OnDoneClick(task, isChecked))
                        })
                }
            }

        }
    }
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = modifier
//            .padding(8.dp)
//            .clip(RoundedCornerShape(32.dp))
//            .background(MaterialTheme.colorScheme.secondaryContainer)
//    ) {


}