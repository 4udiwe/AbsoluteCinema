package com.example.absolutecinema.di

import com.example.data.logger.ErrorLogger
import com.example.domain.logger.Logger
import org.koin.dsl.module

val loggerModule = module {
    single<Logger> { ErrorLogger() }
}