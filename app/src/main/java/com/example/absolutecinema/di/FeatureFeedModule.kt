package com.example.absolutecinema.di

import com.example.feed.viewmodel.FeedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureFeedModule = module {
    viewModel<FeedViewModel> {
        FeedViewModel(repository = get())
    }
}