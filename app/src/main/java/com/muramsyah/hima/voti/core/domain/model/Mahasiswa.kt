package com.muramsyah.hima.voti.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mahasiswa(
    val nim: String,
    val nama: String,
    val angkatan: Int,
    val semester: Int,
    val email: String
): Parcelable {
    constructor() : this("", " ", 0, 0, "")
}
