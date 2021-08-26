package com.muramsyah.hima.voti.core.data.firebase

sealed class FirebaseResponse<out R> {
    data class Success<out T>(val data: T): FirebaseResponse<T>()
    data class Error(val message: String): FirebaseResponse<Nothing>()
    object Empty : FirebaseResponse<Nothing>()
}
