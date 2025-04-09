package com.example.absolutecinema.di

import com.example.users.viewmodel.UsersViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureUsersModule = module {
    viewModel<UsersViewModel> {
        UsersViewModel(repository = get())
    }
}