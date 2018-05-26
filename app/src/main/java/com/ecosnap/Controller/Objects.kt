package com.ecosnap.Controller

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Date(val date: String,
                val row: Array<Item>): Parcelable

@Parcelize
data class Item(val name: String,
                val percent: Int,
                val recyclable: Boolean,
                val path: String): Parcelable