package com.krygodev.safenotes.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.krygodev.safenotes.data.Note
import com.krygodev.safenotes.data.User


// Class responsible for integration with Firebase
class FirebaseRepository {

    // Firebase services instances
    private val fbStorage = FirebaseStorage.getInstance()
    private val fbAuth = FirebaseAuth.getInstance()
    private val fbFirestore = FirebaseFirestore.getInstance()
    private val uid = fbAuth.currentUser?.uid

    private val REPO_DEBUG = "REPOSITORY_DEBUG"


    // Getting user data from Firestore
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


    // Getting user notes from Firestore
    fun getUserNotes(sort: String): LiveData<List<Note>> {
        val cloudResult = MutableLiveData<List<Note>>()
        var order = Query.Direction.DESCENDING

        if (sort == "asc") order = Query.Direction.ASCENDING

        fbFirestore.collection("Users")
            .document(uid!!)
            .collection("Notes")
            .orderBy("timestamp", order)
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


    fun createNewNote(note: Note) {
        // Creating empty note to get generated id
        val noteID = fbFirestore.collection("Users")
            .document(uid!!)
            .collection("Notes")
            .document()
            .id

        // Updating note id to generated id
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


    fun updateNote(note: Note, map: Map<String, Comparable<*>?>) {
        fbFirestore.collection("Users")
            .document(uid!!)
            .collection("Notes")
            .document(note.id!!)
            .update(map)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "Note updated!")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }


    fun uploadPhoto(bytes: ByteArray, note: Note) {
        fbStorage.getReference("notePhotos")
            .child("${note.id}.jpg")
            .putBytes(bytes)
            .addOnCompleteListener {
                Log.d(REPO_DEBUG, "Photo uploaded!")
            }
            .addOnSuccessListener {
                getPhotoURL(it!!.storage, note)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }


    fun deleteNote(note: Note) {
        deleteNotePhoto(note)
        fbFirestore.collection("Users")
            .document(uid!!)
            .collection("Notes")
            .document(note.id!!)
            .delete()
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "Note deleted!")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }


    fun deleteNotePhoto(note: Note) {
        if (note.image != "") {
            fbStorage.getReference("notePhotos")
                .child("${note.id}.jpg")
                .delete()
                .addOnSuccessListener {
                    Log.d(REPO_DEBUG, "Image deleted!")
                }
                .addOnFailureListener {
                    Log.d(REPO_DEBUG, it.message.toString())
                }
        } else {
            Log.d(REPO_DEBUG, "No image to delete!")
        }
    }


    // Getting photo URL
    private fun getPhotoURL(storage: StorageReference, note: Note) {
        storage.downloadUrl
            .addOnSuccessListener {
                updateNote(note, mapOf("image" to it.toString()))
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }
}