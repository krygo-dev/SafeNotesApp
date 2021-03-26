package com.krygodev.safenotes.viewmodels

import androidx.lifecycle.ViewModel
import com.krygodev.safenotes.data.Note
import com.krygodev.safenotes.repository.FirebaseRepository


class HomeScreenViewModel: ViewModel() {

    private val repository = FirebaseRepository()

    val user = repository.getUserData()
    val userNotesDesc = repository.getUserNotes("desc")
    val userNotesAsc = repository.getUserNotes("asc")

    fun deleteNote(note: Note) {
        repository.deleteNote(note)
    }
}