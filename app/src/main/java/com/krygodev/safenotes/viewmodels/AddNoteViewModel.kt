package com.krygodev.safenotes.viewmodels

import androidx.lifecycle.ViewModel
import com.krygodev.safenotes.data.Note
import com.krygodev.safenotes.repository.FirebaseRepository


// ViewModel for AddNote Activity
class AddNoteViewModel: ViewModel() {

    private val repository = FirebaseRepository()

    fun createNewNote(note: Note) {
        repository.createNewNote(note)
    }

    fun uploadNotePhoto(bytes: ByteArray, note: Note) {
        repository.uploadPhoto(bytes, note)
    }

    fun updateNote(note: Note, map: Map<String, Comparable<*>?>) {
        repository.updateNote(note, map)
    }

    fun deleteNotePhoto(note: Note) {
        repository.deleteNotePhoto(note)
    }
}