package com.krygodev.safenotes.viewmodels

import androidx.lifecycle.ViewModel
import com.krygodev.safenotes.repository.FirebaseRepository


class HomeScreenViewModel: ViewModel() {

    private val repository = FirebaseRepository()
    val user = repository.getUserData()
    val userNotes = repository.getUserNotes()
}