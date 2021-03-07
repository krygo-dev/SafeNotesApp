package com.example.safenotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        setSupportActionBar(findViewById(R.id.appbar))
    }
}