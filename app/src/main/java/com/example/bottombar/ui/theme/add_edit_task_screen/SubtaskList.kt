package com.example.bottombar.ui.theme.add_edit_task_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.bottombar.data.room.Subtask
import com.example.bottombar.ui.theme.screen_events.AddEditScreenEvent

@Composable
fun SubtaskList(
    onEvent: (AddEditScreenEvent) -> Unit,
    subtask: Subtask
) {

    Spacer(modifier = Modifier.height(8.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(0.75f)
    ) {

        if (subtask.isActive) {
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    IconButton(
                        modifier = Modifier.weight(0.5f),
                        onClick = {
                            onEvent(AddEditScreenEvent.OnSubtaskDoneClick(subtask))
                        }) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "end subtask"
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.weight(0.5f),
                        onClick = {
                            onEvent(AddEditScreenEvent.OnSubtaskDeleteClick(subtask))
                        }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete subtask"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                value = subtask.subtaskText,
                onValueChange = {
                    onEvent(AddEditScreenEvent.OnSubtaskChange(it, subtask))
                },
                placeholder = {
                    Text(text = "Enter your subtask")
                },
                singleLine = false
            )
        } else {

            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.LightGray,
                    textColor = Color.Gray
                ),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    IconButton(
                        modifier = Modifier.weight(0.5f),
                        onClick = {
                            onEvent(AddEditScreenEvent.OnSubtaskRestoreClick(subtask))
                        }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "restore subtask"
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.weight(0.5f),
                        onClick = {
                            onEvent(AddEditScreenEvent.OnSubtaskDeleteClick(subtask))
                        }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete subtask"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                value = subtask.subtaskText,
                textStyle = TextStyle(textDecoration = TextDecoration.LineThrough),
                onValueChange = {
                    onEvent(AddEditScreenEvent.OnSubtaskChange(it, subtask))
                },
                placeholder = {
                    Text(text = "Enter your subtask")
                },
                singleLine = false
            )
        }
    }
}