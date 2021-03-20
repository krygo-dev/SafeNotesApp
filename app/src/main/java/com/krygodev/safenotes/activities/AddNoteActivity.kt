package com.krygodev.safenotes.activities

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.krygodev.safenotes.R
import com.krygodev.safenotes.data.Note
import com.krygodev.safenotes.viewmodels.AddNoteViewModel
import kotlinx.android.synthetic.main.activity_add_note.*
import java.util.*


class AddNoteActivity : AppCompatActivity() {

    private val addNoteViewModel by viewModels<AddNoteViewModel>()

    private var selectedColor = R.color.red

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        setSupportActionBar(findViewById(R.id.appbar))

        setOnClicks()
    }


    // Menu functionality
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.save_menu_item -> addNote()
            R.id.back_menu_item -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun addNote() {
        val title = title_details.text.toString()
        val content = note_details.text.toString()
        val color = selectedColor

        if (title == "" || content == "")
            Toast.makeText(applicationContext, "Fill title and content fields!", Toast.LENGTH_SHORT).show()
        else {
            val note = Note("", title, content, "", Timestamp(Date()), color)
            addNoteViewModel.createNewNote(note)
            Toast.makeText(applicationContext, "Note created!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }


    private fun setOnClicks() {
        view_color_red.setOnClickListener {
            selectedColor = R.color.red
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red_checked)
            view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow)
            view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue)
            view_color_green.setBackgroundResource(R.drawable.background_note_color_green)
            view_color_white.setBackgroundResource(R.drawable.background_note_color_white)
        }

        view_color_yellow.setOnClickListener {
            selectedColor = R.color.yellow
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red)
            view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow_checked)
            view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue)
            view_color_green.setBackgroundResource(R.drawable.background_note_color_green)
            view_color_white.setBackgroundResource(R.drawable.background_note_color_white)
        }

        view_color_blue.setOnClickListener {
            selectedColor = R.color.blue
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red)
            view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow)
            view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue_checked)
            view_color_green.setBackgroundResource(R.drawable.background_note_color_green)
            view_color_white.setBackgroundResource(R.drawable.background_note_color_white)
        }

        view_color_green.setOnClickListener {
            selectedColor = R.color.green
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red)
            view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow)
            view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue)
            view_color_green.setBackgroundResource(R.drawable.background_note_color_green_checked)
            view_color_white.setBackgroundResource(R.drawable.background_note_color_white)
        }

        view_color_white.setOnClickListener {
            selectedColor = R.color.white
            view_color_red.setBackgroundResource(R.drawable.background_note_color_red)
            view_color_yellow.setBackgroundResource(R.drawable.background_note_color_yellow)
            view_color_blue.setBackgroundResource(R.drawable.background_note_color_blue)
            view_color_green.setBackgroundResource(R.drawable.background_note_color_green)
            view_color_white.setBackgroundResource(R.drawable.background_note_color_white_checked)
        }
    }
}