package com.krygodev.safenotes.viewmodels

import androidx.lifecycle.ViewModel
import com.krygodev.safenotes.data.User
import com.krygodev.safenotes.repository.FirebaseRepository


class RegistrationViewModel: ViewModel() {

    private val repository = FirebaseRepository()

    fun createNewUser(user: User) {
        repository.createNewUser(user)
    }
}