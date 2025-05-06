package com.example.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthViewModel(
    private val auth: FirebaseAuth,
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun checkUser(): Boolean = auth.currentUser != null

    private suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result!!)
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Unknown error")
                        )
                    }
                }
        }
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        try {
            val result = signInWithEmail(email, password)
            _authState.value = AuthState.SignInSuccess(result.user!!)
        } catch (e: Exception) {
            _authState.value = AuthState.SignInFail(e.message ?: "Unknown error")
        }
    }

    private suspend fun register(email: String, password: String): AuthResult {
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result!!)
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Unknown error")
                        )
                    }
                }
        }
    }

    fun createAccount(email: String, password: String, confirmPassword: String) =
        viewModelScope.launch {
            if (password != confirmPassword) {
                _authState.value = AuthState.CreateAccountFail("Passwords don't match")
                return@launch
            }

            _authState.value = AuthState.Loading
            try {
                val result = register(email, password)
                _authState.value = AuthState.CreateAccountSuccess(result.user!!)
            } catch (e: Exception) {
                _authState.value = AuthState.CreateAccountFail(e.message ?: "Unknown error")
            }
        }

    fun logOut() = auth.signOut()

}