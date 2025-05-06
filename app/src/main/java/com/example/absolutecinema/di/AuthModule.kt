package com.example.absolutecinema.di

import com.example.auth.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel<AuthViewModel> {
        AuthViewModel(
            auth = Firebase.auth
        )
    }

    single<FirebaseAuth> {
        FirebaseApp.initializeApp(androidApplication())
        Firebase.auth
    }
}