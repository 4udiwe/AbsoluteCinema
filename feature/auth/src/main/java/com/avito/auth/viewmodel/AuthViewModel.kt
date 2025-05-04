package com.avito.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val auth: FirebaseAuth
) : ViewModel() {

    //private var auth: FirebaseAuth = Firebase.auth

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun checkUser(): Boolean = auth.currentUser != null


    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = AuthState.Loading

                if (task.isSuccessful) {
                    _authState.value = AuthState.SignInSuccess(user = auth.currentUser!!)
                } else {
                    _authState.value =
                        AuthState.SignInFail(message = task.exception!!.message.toString())
                }
            }
    }

    fun createAccount(email: String, password: String, confirmPassword: String) {

        if (password != confirmPassword){
            _authState.value = AuthState.CreateAccountFail(message = "Passwords doesn't match")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = AuthState.Loading

                if (task.isSuccessful) {
                    _authState.value = AuthState.CreateAccountSuccess(user = auth.currentUser!!)
                } else {
                    _authState.value =
                        AuthState.CreateAccountFail(message = task.exception!!.message.toString())
                }
            }

    }
}