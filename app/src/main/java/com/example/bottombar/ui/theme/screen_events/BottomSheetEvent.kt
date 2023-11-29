package com.example.bottombar.ui.theme.screen_events

import com.example.bottombar.data.firebase.Project


sealed class BottomSheetEvent: FieldChangeEvent(){
    object OnConfirmProjectClick: BottomSheetEvent()
    data class OnEditProjectClick(val project: Project): BottomSheetEvent()
}
