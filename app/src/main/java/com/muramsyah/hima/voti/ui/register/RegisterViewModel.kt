package com.muramsyah.hima.voti.ui.register

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.muramsyah.hima.voti.core.domain.model.Mahasiswa
import com.muramsyah.hima.voti.core.domain.usecase.VotiUseCase

class RegisterViewModel(val useCase: VotiUseCase) : ViewModel() {

    fun registerNewAccount(data: Mahasiswa, password: String) =
        LiveDataReactiveStreams.fromPublisher(useCase.registerNewAccount(data, password))

}