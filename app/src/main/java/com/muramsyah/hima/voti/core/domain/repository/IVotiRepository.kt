package com.muramsyah.hima.voti.core.domain.repository

import com.muramsyah.hima.voti.core.data.Resource
import com.muramsyah.hima.voti.core.domain.model.CalonKahim
import com.muramsyah.hima.voti.core.domain.model.Mahasiswa
import io.reactivex.Flowable

interface IVotiRepository {
    fun signInAccount(email: String, password: String): Flowable<Resource<Mahasiswa>>

    fun registerNewAccount(data: Mahasiswa, password: String): Flowable<Resource<String>>

    fun voteTheCandidate(data: Mahasiswa, dataCandidate: CalonKahim, date: String): Flowable<Resource<String>>

    fun getCandidates(): Flowable<Resource<List<CalonKahim>>>

    fun getMahasiswa(): Flowable<Resource<Mahasiswa>>
}