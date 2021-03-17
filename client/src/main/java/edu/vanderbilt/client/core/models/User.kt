package edu.vanderbilt.client.core.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var name: String, var email: String, var password: String): Parcelable