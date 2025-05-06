package com.example.auth.viewmodel

import com.google.firebase.auth.FirebaseUser

/**
 * TODO
 *
 */
sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class SignInSuccess(val user: FirebaseUser) : AuthState()
    data class SignInFail(val message: String) : AuthState()
    data class CreateAccountSuccess(val user: FirebaseUser) : AuthState()
    data class CreateAccountFail(val message: String) : AuthState()
}