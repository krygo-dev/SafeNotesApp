package com.krygodev.safenotes.data

import com.google.firebase.Timestamp


data class Note(var id: String? = null,
                val title: String? = null,
                val content: String? = null,
                val image: String? = null,
                val timestamp: Timestamp? = null,
                val color: Int? = null)
