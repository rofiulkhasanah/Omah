package com.creartpro.smarthome.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PerangkatEntity(
    val imgPerangkatOn: Int,
    val imgPerangkatOff: Int,
    val nama_perangkat: String,
    val name: String
) : Parcelable