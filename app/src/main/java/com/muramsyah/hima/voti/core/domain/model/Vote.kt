package com.muramsyah.hima.voti.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vote(
    val idMahasiswa: String,
    val idCalonKahim: String,
    val tanggalVoting: String
): Parcelable {
    constructor() : this("", "", "")
}
