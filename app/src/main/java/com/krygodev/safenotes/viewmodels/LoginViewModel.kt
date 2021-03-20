package com.krygodev.safenotes.viewmodels

import androidx.lifecycle.ViewModel
import com.krygodev.safenotes.data.User
import com.krygodev.safenotes.repository.FirebaseRepository


class LoginViewModel: ViewModel() {

    private val repository = FirebaseRepository()

    fun createNewGoogleSignInUser(user: User) {
        repository.createNewUser(user)
    }
}