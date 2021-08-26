package com.muramsyah.hima.voti.ui.login

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.muramsyah.hima.voti.core.domain.usecase.VotiUseCase

class LoginViewModel(val useCase: VotiUseCase) : ViewModel() {

    fun signInAccount(email: String, password: String) =
        LiveDataReactiveStreams.fromPublisher(useCase.signInAccount(email, password))

}