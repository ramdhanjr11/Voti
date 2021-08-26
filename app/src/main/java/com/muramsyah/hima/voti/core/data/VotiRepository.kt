package com.muramsyah.hima.voti.core.data

import com.muramsyah.hima.voti.core.data.firebase.FirebaseDataSource
import com.muramsyah.hima.voti.core.data.firebase.FirebaseResponse
import com.muramsyah.hima.voti.core.domain.model.CalonKahim
import com.muramsyah.hima.voti.core.domain.model.Mahasiswa
import com.muramsyah.hima.voti.core.domain.repository.IVotiRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class VotiRepository(private val firebaseDataSource: FirebaseDataSource) : IVotiRepository {

    val compositeDisposable = CompositeDisposable()

    override fun signInAccount(email: String, password: String): Flowable<Resource<Mahasiswa>> {
        val result = PublishSubject.create<Resource<Mahasiswa>>()

        result.onNext(Resource.Loading(null))
        val signIn = firebaseDataSource.signInAccount(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                compositeDisposable.dispose()
            }
            .subscribe { response ->
                when (response) {
                    is FirebaseResponse.Success -> {
                        result.onNext(Resource.Success(response.data))
                    }
                    is FirebaseResponse.Error -> {
                        result.onNext(Resource.Error(response.message))
                    }
                }
            }
        compositeDisposable.add(signIn)
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun registerNewAccount(data: Mahasiswa, password: String): Flowable<Resource<String>> {
        val result = PublishSubject.create<Resource<String>>()

        result.onNext(Resource.Loading(null))
        result.onNext(Resource.Loading("Loading"))
        val registerAccount = firebaseDataSource.registerNewAccount(data, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                compositeDisposable.dispose()
            }
            .subscribe { response ->
                when (response) {
                    is FirebaseResponse.Success -> {
                        result.onNext(Resource.Success(response.data))
                    }
                    is FirebaseResponse.Error -> {
                        result.onNext(Resource.Error(response.message))
                    }
                }
            }
        compositeDisposable.add(registerAccount)
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun voteTheCandidate(
        data: Mahasiswa,
        dataCandidate: CalonKahim,
        date: String
    ): Flowable<Resource<String>> {
        val result = PublishSubject.create<Resource<String>>()

        result.onNext(Resource.Loading(null))
        val voteCandidate = firebaseDataSource.voteCandidate(data, dataCandidate, date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                compositeDisposable.dispose()
            }
            .subscribe { response ->
                when (response) {
                    is FirebaseResponse.Success -> {
                        result.onNext(Resource.Success(response.data))
                    }
                    is FirebaseResponse.Error -> {
                        result.onNext(Resource.Error(response.message))
                    }
                }
            }
        compositeDisposable.add(voteCandidate)
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getCandidates(): Flowable<Resource<List<CalonKahim>>> {
        val result = PublishSubject.create<Resource<List<CalonKahim>>>()

        result.onNext(Resource.Loading())
        val candidates = firebaseDataSource.getCandidates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                compositeDisposable.dispose()
            }
            .subscribe { response ->
                when (response) {
                    is FirebaseResponse.Success -> {
                        result.onNext(Resource.Success(response.data))
                    }
                    is FirebaseResponse.Error -> {
                        result.onNext(Resource.Error(response.message))
                    }
                }
            }
        result.onNext(Resource.Loading())
        compositeDisposable.add(candidates)
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getMahasiswa(): Flowable<Resource<Mahasiswa>> {
        val result = PublishSubject.create<Resource<Mahasiswa>>()

        result.onNext(Resource.Loading(null))
        val mahasiswa = firebaseDataSource.getMahasiswa()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                compositeDisposable.dispose()
            }
            .subscribe { response ->
                when (response) {
                    is FirebaseResponse.Success -> {
                        result.onNext(Resource.Success(response.data))
                    }
                    is FirebaseResponse.Error -> {
                        result.onNext(Resource.Error(response.message))
                    }
                }
            }
//        result.onNext(Resource.Loading(null))
        compositeDisposable.add(mahasiswa)
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }
}