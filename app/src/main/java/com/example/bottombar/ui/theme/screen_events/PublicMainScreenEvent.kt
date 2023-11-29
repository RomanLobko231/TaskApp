package com.example.bottombar.ui.theme.screen_events

import com.example.bottombar.data.firebase.Project

sealed class PublicMainScreenEvent {
    object OnPrivateTabClick : PublicMainScreenEvent()
    object OnProfileClick : PublicMainScreenEvent()
    object OnNewProjectClick: PublicMainScreenEvent()
    data class OnProjectClick(val project: Project): PublicMainScreenEvent()
    data class OnDropDownClick(val project: Project): PublicMainScreenEvent()
    data class OnDeleteProjectClick(val project: Project): PublicMainScreenEvent()
    object OnEditProjectClick: PublicMainScreenEvent()
}
