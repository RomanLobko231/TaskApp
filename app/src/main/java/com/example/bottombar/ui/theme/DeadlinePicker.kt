package com.example.bottombar.ui.theme

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.bottombar.ui.theme.screen_events.FieldChangeEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlinePicker(
    openDialog: MutableState<Boolean>,
    deadlineMillis: Long,
    onConfirm: (FieldChangeEvent) -> Unit
){
    if (openDialog.value){
        val datePickerState = rememberDatePickerState(
            deadlineMillis
        )
        val confirmEnabled by remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onConfirm(FieldChangeEvent.OnDeadlineChange(datePickerState.selectedDateMillis!!))
                    },
                    enabled = confirmEnabled
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }) {
            DatePicker(state = datePickerState)
        }
    }

}