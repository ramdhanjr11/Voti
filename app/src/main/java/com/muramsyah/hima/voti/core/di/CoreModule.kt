package com.muramsyah.hima.voti.core.di

import com.muramsyah.hima.voti.core.data.VotiRepository
import com.muramsyah.hima.voti.core.data.firebase.FirebaseDataSource
import com.muramsyah.hima.voti.core.domain.repository.IVotiRepository
import com.muramsyah.hima.voti.core.sf.AppSharedPreference
import org.koin.dsl.module

val coreModule = module {
    single { FirebaseDataSource() }
    single<IVotiRepository> { VotiRepository(get()) }
}

val sfModule = module {
    factory { AppSharedPreference(get()) }
}