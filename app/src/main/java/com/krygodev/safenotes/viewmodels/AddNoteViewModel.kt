package com.krygodev.safenotes.viewmodels

import androidx.lifecycle.ViewModel
import com.krygodev.safenotes.data.Note
import com.krygodev.safenotes.repository.FirebaseRepository


class AddNoteViewModel: ViewModel() {

    private val repository = FirebaseRepository()

    fun createNewNote(note: Note) {
        repository.createNewNote(note)
    }
}