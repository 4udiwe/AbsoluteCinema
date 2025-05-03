package com.example.absolutecinema.di

import android.content.Context
import android.content.SharedPreferences
import com.example.search.viewmodel.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

const val SEARCH_HISTORY_SHARED_PREF_KEY = "search_history"

val searchModule = module {
    viewModel<SearchViewModel> {
        SearchViewModel(
            repository = get(),
            sharedPreferences = get()
        )
    }

    single<SharedPreferences> {
        androidApplication().getSharedPreferences(SEARCH_HISTORY_SHARED_PREF_KEY, Context.MODE_PRIVATE)
    }
}