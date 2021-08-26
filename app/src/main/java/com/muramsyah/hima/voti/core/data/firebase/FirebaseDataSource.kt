package com.muramsyah.hima.voti.core.data.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.muramsyah.hima.voti.core.domain.model.CalonKahim
import com.muramsyah.hima.voti.core.domain.model.Mahasiswa
import com.muramsyah.hima.voti.core.domain.model.Vote
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class FirebaseDataSource {

    val auth = Firebase.auth
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference(TAG_MAHASISWA)


    fun signInAccount(email: String, password: String): Flowable<FirebaseResponse<Mahasiswa>> {
        val result = PublishSubject.create<FirebaseResponse<Mahasiswa>>()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (auth.currentUser!!.isEmailVerified) {
                    myRef.child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val mahasiswa = snapshot.getValue(Mahasiswa::class.java)
                            if (mahasiswa != null) {
                                result.onNext(FirebaseResponse.Success(mahasiswa))
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            result.onNext(FirebaseResponse.Error(error.message))
                        }

                    })
                } else {
                    result.onNext(FirebaseResponse.Error("Email belum terverifikasi!"))
                }
            } else {
                result.onNext(FirebaseResponse.Error(task.exception.toString()))
            }
        }

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    fun registerNewAccount(data: Mahasiswa, password: String): Flowable<FirebaseResponse<String>> {
        val result = PublishSubject.create<FirebaseResponse<String>>()

        database.getReference(TAG_MAHASISWA).orderByChild(TAG_ID_MAHASISWA).equalTo(data.nim)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        result.onNext(FirebaseResponse.Error("Nim telah terdaftar"))
                    } else {
                        auth.createUserWithEmailAndPassword(data.email, password).addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                val mahasiswa = Mahasiswa(
                                    data.nim,
                                    data.nama,
                                    data.angkatan,
                                    data.semester,
                                    data.email)

                                database.getReference(TAG_MAHASISWA).child(auth.currentUser!!.uid)
                                    .setValue(mahasiswa)
                                    .addOnCompleteListener {
                                        auth.currentUser?.sendEmailVerification()
                                        result.onNext(FirebaseResponse.Success("Success Membuat Akun, Silahkan verifikasi email anda!"))
                                    }
                            } else {
                                result.onNext(FirebaseResponse.Error("${task.exception}"))
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    result.onNext(FirebaseResponse.Error(error.message))
                }

            })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    fun voteCandidate(data: Mahasiswa, dataCandidate: CalonKahim, date: String): Flowable<FirebaseResponse<String>> {
        val result = PublishSubject.create<FirebaseResponse<String>>()

        database.getReference(TAG_VOTE).orderByChild(TAG_ID_MAHASISWA_VOTE).equalTo(data.nim)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        result.onNext(FirebaseResponse.Error("Anda telah melakukan voting!"))
                    } else {
                        val vote = Vote(data.nim, dataCandidate.id, date)
                        database.getReference(TAG_VOTE).child(auth.currentUser!!.uid).setValue(vote)
                        result.onNext(FirebaseResponse.Success("Sukses voting calon kahim!"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    result.onNext(FirebaseResponse.Error(error.message))
                }

            })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    fun getCandidates(): Flowable<FirebaseResponse<List<CalonKahim>>> {
        val result = PublishSubject.create<FirebaseResponse<List<CalonKahim>>>()

        database.getReference(TAG_CALON_KAHIM).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val candidates = ArrayList<CalonKahim>()

                    for (data in snapshot.children) {
                        val candidate = data.getValue(CalonKahim::class.java)
                        if (candidate != null) {
                            candidates.add(candidate)
                        }
                    }

                    result.onNext(FirebaseResponse.Success(candidates))

                } else {
                    result.onNext(FirebaseResponse.Empty)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                result.onNext(FirebaseResponse.Error(error.message))
            }

        })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    fun getMahasiswa(): Flowable<FirebaseResponse<Mahasiswa>> {
        val result = PublishSubject.create<FirebaseResponse<Mahasiswa>>()

        myRef.orderByChild(TAG_EMAIL_MAHASISWA).equalTo(Firebase.auth.currentUser?.email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val mahasiswa = ArrayList<Mahasiswa>()
                        snapshot.children.forEach {
                            mahasiswa.add(it.getValue(Mahasiswa::class.java)!!)
                        }
                        result.onNext(FirebaseResponse.Success(mahasiswa[0]))
                    } else {
                        result.onNext(FirebaseResponse.Empty)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    result.onNext(FirebaseResponse.Error(error.message))
                }

            })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    companion object {
        const val TAG_MAHASISWA = "mahasiswa"
        const val TAG_ID_MAHASISWA = "nim"
        const val TAG_ID_MAHASISWA_VOTE = "idMahasiswa"
        const val TAG_VOTE = "vote"
        const val TAG_CALON_KAHIM = "calon-kahim"
        const val TAG_EMAIL_MAHASISWA = "email"
    }

}