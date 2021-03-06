package com.krygodev.safenotes.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.krygodev.safenotes.R
import com.krygodev.safenotes.data.Note
import com.krygodev.safenotes.viewmodels.AddNoteViewModel
import kotlinx.android.synthetic.main.activity_add_note.*
import java.io.ByteArrayOutputStream
import java.util.*


// Activity responsible for handling adding new note and updating selected note
@Suppress("DEPRECATION")
class AddNoteActivity : AppCompatActivity() {

    private val addNoteViewModel by viewModels<AddNoteViewModel>()

    // Variables holding selected color and photo
    private var selectedColor = "#de5246"
    private var byteArray: ByteArray? = null

    private val REQUEST_CODE_STORAGE_PERMISSION = 1
    private val REQUEST_CODE_SELECT_IMAGE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        // Setting up custom AppBar
        setSupportActionBar(findViewById(R.id.appbar_add_note))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Handling taking data from Intent
        if (intent.hasExtra("note")) {
            val note = intent.getParcelableExtra<Note>("note")

            title_details.setText(note!!.title)
            note_details.setText(note.content)
            selectedColor = note.color!!
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red)

            when (note.color) {
                "#de5246" -> view_color_red.setBackgroundResource(R.drawable.background_note_color_red_checked)
                "#fdd835" -> view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow_checked)
                "#3949ab" -> view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue_checked)
                "#43a047" -> view_color_green.setBackgroundResource(R.drawable.background_note_color_green_checked)
                "#ffffff" -> view_color_white.setBackgroundResource(R.drawable.background_note_color_white_checked)
            }

            if (note.image != "") {
                Glide.with(this).load(note.image).into(note_image)
                note_image.visibility = View.VISIBLE
            }

        }

        setOnClicks()
    }


    // Back to previous activity functionality
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    // Menu functionality
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note, menu)
        return super.onCreateOptionsMenu(menu)
    }


    // Menu clicks functionality
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.save_menu_item -> {
                if (intent.hasExtra("note")) updateNote(intent.getParcelableExtra("note"))
                else addNote()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    // Add note functionality
    private fun addNote() {
        val title = title_details.text.toString()
        val content = note_details.text.toString()
        val color = selectedColor

        if (title == "" || content == "")
            Toast.makeText(applicationContext, "Fill title and content fields!", Toast.LENGTH_SHORT).show()
        else {
            val note = Note("", title, content, "", Timestamp(Date()), color)
            addNoteViewModel.createNewNote(note)

            if (byteArray != null) {
                addNoteViewModel.uploadNotePhoto(byteArray!!, note)
                // "Uploading photo" progress dialog
                createProgressDialog()
            } else {
                backToHomeScreen.run()
            }
        }
    }


    // Update note functionality
    private fun updateNote(note: Note?) {
        val title = title_details.text.toString()
        val content = note_details.text.toString()
        val color = selectedColor

        if (title == "" || content == "") {
            Toast.makeText(applicationContext, "Fill title and content fields!", Toast.LENGTH_SHORT).show()
        } else {
            val map = mapOf("id" to note!!.id,
                            "title" to title,
                            "content" to content,
                            "image" to note.image,
                            "timestamp" to Timestamp(Date()),
                            "color" to color)

            addNoteViewModel.updateNote(note, map)

            if (byteArray != null) {
                // Deleting old and uploading new photo
                addNoteViewModel.deleteNotePhoto(note)
                addNoteViewModel.uploadNotePhoto(byteArray!!, note)
                createProgressDialog()
            } else {
                backToHomeScreen.run()
            }
        }
    }


    // Progress dialog builder
    private fun createProgressDialog() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(R.layout.progress_dialog_upload_photo)
            .create()
            .show()

        Handler().postDelayed(backToHomeScreen, 3000)
    }


    // Runnable responsible for backing to home screen
    private val backToHomeScreen = Runnable {
        Toast.makeText(applicationContext, "Note created!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(applicationContext, HomeScreenActivity::class.java))
    }


    // Function setting up all onClicks on buttons
    // Colors and photo
    private fun setOnClicks() {
        view_color_red.setOnClickListener {
            selectedColor = "#de5246"
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red_checked)
            view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow)
            view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue)
            view_color_green.setBackgroundResource(R.drawable.background_note_color_green)
            view_color_white.setBackgroundResource(R.drawable.background_note_color_white)
        }

        view_color_yellow.setOnClickListener {
            selectedColor = "#fdd835"
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red)
            view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow_checked)
            view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue)
            view_color_green.setBackgroundResource(R.drawable.background_note_color_green)
            view_color_white.setBackgroundResource(R.drawable.background_note_color_white)
        }

        view_color_blue.setOnClickListener {
            selectedColor = "#3949ab"
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red)
            view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow)
            view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue_checked)
            view_color_green.setBackgroundResource(R.drawable.background_note_color_green)
            view_color_white.setBackgroundResource(R.drawable.background_note_color_white)
        }

        view_color_green.setOnClickListener {
            selectedColor = "#43a047"
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red)
            view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow)
            view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue)
            view_color_green.setBackgroundResource(R.drawable.background_note_color_green_checked)
            view_color_white.setBackgroundResource(R.drawable.background_note_color_white)
        }

        view_color_white.setOnClickListener {
            selectedColor = "#ffffff"
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red)
            view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow)
            view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue)
            view_color_green.setBackgroundResource(R.drawable.background_note_color_green)
            view_color_white.setBackgroundResource(R.drawable.background_note_color_white_checked)
        }

        view_add_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_STORAGE_PERMISSION
                )
            }
            else selectImage()
        }
    }


    // Function creating intent to MediaStore
    private fun selectImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, REQUEST_CODE_SELECT_IMAGE)
    }


    // Get result from selectImage function
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            val image = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image)

            // Loading image into note_image image view with Glide library
            Glide.with(this)
                .load(image)
                .into(note_image)

            note_image.visibility = View.VISIBLE

            val stream = ByteArrayOutputStream()
            val result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            if (result) byteArray = stream.toByteArray()
        }
    }


    // Requesting permissions to MediaStore
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) selectImage()
            else Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }
}