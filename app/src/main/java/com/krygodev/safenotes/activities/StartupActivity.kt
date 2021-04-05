package com.krygodev.safenotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.krygodev.safenotes.R

class StartupActivity : AppCompatActivity() {

    private val fbAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
    }


    override fun onStart() {
        super.onStart()
        isCurrentUser()
    }


    private fun isCurrentUser() {
        fbAuth.currentUser?.let {
            val intent = Intent(applicationContext, HomeScreenActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
        }
    }
}