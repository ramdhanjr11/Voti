package com.muramsyah.hima.voti

import android.app.Application
import com.muramsyah.hima.voti.core.di.coreModule
import com.muramsyah.hima.voti.core.di.sfModule
import com.muramsyah.hima.voti.di.useCaseModule
import com.muramsyah.hima.voti.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    coreModule,
                    useCaseModule,
                    viewModelModule,
                    sfModule
                )
            )
        }
    }
}