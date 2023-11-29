package com.example.bottombar.util

sealed class UiEventForUser{
    data class Navigate(val route: String): UiEventForUser()
    object PopBackStack: UiEventForUser()
    object OpenBottomSheet: UiEventForUser()
}
