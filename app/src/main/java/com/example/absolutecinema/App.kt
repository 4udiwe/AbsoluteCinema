package com.example.absolutecinema

import android.app.Application
import com.example.absolutecinema.di.authModule
import com.example.absolutecinema.di.dataModule
import com.example.absolutecinema.di.featureDetailsModule
import com.example.absolutecinema.di.featureFeedModule
import com.example.absolutecinema.di.featureUsersModule
import com.example.absolutecinema.di.loggerModule
import com.example.absolutecinema.di.searchModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@App)
            modules(
                listOf(
                    dataModule,
                    featureFeedModule,
                    featureUsersModule,
                    featureDetailsModule,
                    loggerModule,
                    searchModule,
                    authModule
                )
            )
        }
    }
}