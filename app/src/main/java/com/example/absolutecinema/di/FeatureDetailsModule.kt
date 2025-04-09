package com.example.absolutecinema.di

import com.example.details.viewmodel.DetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureDetailsModule = module {
    viewModel<DetailsViewModel> {
        DetailsViewModel(repository = get())
    }
}