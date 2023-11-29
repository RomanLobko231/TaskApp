package com.example.bottombar.ui.theme.screen_events

import com.example.bottombar.data.room.Subtask

sealed class AddEditScreenEvent: FieldChangeEvent(){
    object OnConfirmTaskClick: AddEditScreenEvent()
    object OnNewSubtaskClick: AddEditScreenEvent()

    data class OnDescriptionChange(val newDescription: String): AddEditScreenEvent()
    data class OnSubtaskDoneClick(val subtask: Subtask): AddEditScreenEvent()
    data class OnSubtaskDeleteClick(val subtaskToDelete: Subtask): AddEditScreenEvent()
    data class OnSubtaskChange(val newSubtask: String, val currentSubtask: Subtask): AddEditScreenEvent()
    data class OnSubtaskRestoreClick(val subtask: Subtask): AddEditScreenEvent()
}
