package com.example.bottombar.ui.theme.add_edit_task_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bottombar.ui.theme.screen_events.FieldChangeEvent

@Composable
fun PriorityRow(
     priority: Int,
     onEvent: (FieldChangeEvent) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for(i in 1..3){
            IconButton(
                onClick = {
                    if (priority == 1 && i == 1){
                        onEvent(FieldChangeEvent.OnPriorityChange(0))

                    } else {
                        onEvent(FieldChangeEvent.OnPriorityChange(i))
                    }

                }
            ) {
                Icon(
                    imageVector = if (priority >= i ) Icons.Default.Star else Icons.Default.Close,
                    contentDescription = "choose priority"
                )
            }
    }

    }


}
