package com.example.bottombar.ui.theme.authentication_screen

import androidx.lifecycle.ViewModel
import com.example.bottombar.presentation.sign_in.SignInResult
import com.example.bottombar.presentation.sign_in.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInErrorMessage = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

}