package com.muramsyah.hima.voti.ui.home

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.muramsyah.hima.voti.core.data.firebase.FirebaseDataSource.Companion.TAG_EMAIL_MAHASISWA
import com.muramsyah.hima.voti.core.data.firebase.FirebaseDataSource.Companion.TAG_MAHASISWA
import com.muramsyah.hima.voti.core.domain.model.Mahasiswa
import com.muramsyah.hima.voti.core.domain.usecase.VotiUseCase

class HomeViewModel(val useCase: VotiUseCase) : ViewModel() {

    fun getMahasiswa() = LiveDataReactiveStreams.fromPublisher(useCase.getMahasiswa())

    fun getCandidates() = LiveDataReactiveStreams.fromPublisher(useCase.getCandidates())

    fun logoutUser() = Firebase.auth.signOut()

}