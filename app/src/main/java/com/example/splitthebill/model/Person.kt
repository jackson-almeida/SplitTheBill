package com.example.splitthebill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person (
    val id: Int,
    var name: String,
    var desc: String,
    var money: Double,
): Parcelable