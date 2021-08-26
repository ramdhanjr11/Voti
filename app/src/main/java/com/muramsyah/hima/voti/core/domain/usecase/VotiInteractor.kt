package com.muramsyah.hima.voti.core.domain.usecase

import com.muramsyah.hima.voti.core.data.Resource
import com.muramsyah.hima.voti.core.domain.model.CalonKahim
import com.muramsyah.hima.voti.core.domain.model.Mahasiswa
import com.muramsyah.hima.voti.core.domain.repository.IVotiRepository
import io.reactivex.Flowable

class VotiInteractor(private val votiRepository: IVotiRepository) : VotiUseCase {
    override fun signInAccount(email: String, password: String): Flowable<Resource<Mahasiswa>> =
        votiRepository.signInAccount(email, password)

    override fun registerNewAccount(data: Mahasiswa, password: String): Flowable<Resource<String>> =
        votiRepository.registerNewAccount(data, password)

    override fun voteTheCandidate(
        data: Mahasiswa,
        dataCandidate: CalonKahim,
        date: String
    ): Flowable<Resource<String>> = votiRepository.voteTheCandidate(data, dataCandidate, date)

    override fun getCandidates(): Flowable<Resource<List<CalonKahim>>> = votiRepository.getCandidates()

    override fun getMahasiswa(): Flowable<Resource<Mahasiswa>> = votiRepository.getMahasiswa()
}