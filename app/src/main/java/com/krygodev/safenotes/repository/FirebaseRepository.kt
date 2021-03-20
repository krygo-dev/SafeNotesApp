package com.krygodev.safenotes.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.Document
import com.google.firebase.storage.FirebaseStorage
import com.krygodev.safenotes.data.Note
import com.krygodev.safenotes.data.User


class FirebaseRepository {

    private val fbStorage = FirebaseStorage.getInstance()
    private val fbAuth = FirebaseAuth.getInstance()
    private val fbFirestore = FirebaseFirestore.getInstance()
    private val uid = fbAuth.currentUser?.uid

    private val REPO_DEBUG = "REPOSITORY_DEBUG"


    fun getUserData(): LiveData<User> {
        val cloudResult = MutableLiveData<User>()

        fbFirestore.collection("Users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                cloudResult.postValue(user!!)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }

        return cloudResult
    }


    fun getUserNotes(): LiveData<List<Note>> {
        val cloudResult = MutableLiveData<List<Note>>()

        fbFirestore.collection("Users")
            .document(uid!!)
            .collection("Notes")
            .get()
            .addOnSuccessListener {
                val notes = it.toObjects(Note::class.java)
                cloudResult.postValue(notes)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }

        return cloudResult
    }


    fun createNewUser(user: User) {
        fbFirestore.collection("Users")
            .document(user.uid!!)
            .set(user)
    }


    fun updateUserName(map: Map<String, String>) {
        fbFirestore.collection("Users")
            .document(uid!!)
            .update(map)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "Username updated!")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }


    fun createNewNote(note: Note) {
        val noteID = fbFirestore.collection("Users")
            .document(uid!!)
            .collection("Notes")
            .document()
            .id

        note.id = noteID

        fbFirestore.collection("Users")
            .document(uid)
            .collection("Notes")
            .document(noteID)
            .set(note)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "Note added")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }
}