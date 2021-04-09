package com.krygodev.safenotes.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize


// Data class holding note data
@Parcelize
data class Note(var id: String? = null,
                val title: String? = null,
                val content: String? = null,
                var image: String? = null,
                val timestamp: Timestamp? = null,
                val color: String? = null): Parcelable
