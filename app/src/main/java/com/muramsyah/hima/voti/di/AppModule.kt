package com.muramsyah.hima.voti.di

import com.muramsyah.hima.voti.core.domain.usecase.VotiInteractor
import com.muramsyah.hima.voti.core.domain.usecase.VotiUseCase
import com.muramsyah.hima.voti.ui.detail.calon.DetailCalonViewModel
import com.muramsyah.hima.voti.ui.home.HomeViewModel
import com.muramsyah.hima.voti.ui.login.LoginViewModel
import com.muramsyah.hima.voti.ui.register.RegisterViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<VotiUseCase> { VotiInteractor(get()) }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { DetailCalonViewModel(get()) }
}