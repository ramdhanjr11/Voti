package com.muramsyah.hima.voti.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalonKahim(
    val id: String,
    val nama: String,
    val email: String,
    val semester: Int,
    val visi: String,
    val misi: String,
    val imageUrl: String
): Parcelable {
    constructor() : this("", "", "", 0, "", "", "")
}
