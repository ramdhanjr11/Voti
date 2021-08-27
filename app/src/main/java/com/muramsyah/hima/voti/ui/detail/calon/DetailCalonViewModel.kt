package com.muramsyah.hima.voti.ui.detail.calon

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.muramsyah.hima.voti.core.domain.model.CalonKahim
import com.muramsyah.hima.voti.core.domain.model.Mahasiswa
import com.muramsyah.hima.voti.core.domain.usecase.VotiUseCase

class DetailCalonViewModel(val useCase: VotiUseCase) : ViewModel(){

    fun voteCandidate(data: Mahasiswa, dataCandidate: CalonKahim, date: String) = LiveDataReactiveStreams.fromPublisher(useCase.voteTheCandidate(data, dataCandidate, date))

    fun getMahasiswa() = LiveDataReactiveStreams.fromPublisher(useCase.getMahasiswa())

}