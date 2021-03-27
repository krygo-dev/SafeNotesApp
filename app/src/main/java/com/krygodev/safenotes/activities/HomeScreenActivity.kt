package com.krygodev.safenotes.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.krygodev.safenotes.R
import com.krygodev.safenotes.adapters.NoteAdapter
import com.krygodev.safenotes.data.Note
import com.krygodev.safenotes.data.User
import com.krygodev.safenotes.interfaces.OnNoteItemClick
import com.krygodev.safenotes.interfaces.OnNoteItemLongClick
import com.krygodev.safenotes.viewmodels.HomeScreenViewModel
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.bottom_sheet_home_screen.*
import java.util.ArrayList


class HomeScreenActivity : AppCompatActivity(), OnNoteItemLongClick, OnNoteItemClick {

    private val fbAuth = FirebaseAuth.getInstance()
    private val homeScreenViewModel by viewModels<HomeScreenViewModel>()
    private val adapter = NoteAdapter(this, this)

    private val HOME_DEBUG = "HOME_SCREEN_DEBUG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        setSupportActionBar(findViewById(R.id.appbar))

        addNewNote()

        recycler_view_home_screen.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler_view_home_screen.adapter = adapter

        homeScreenViewModel.user.observe(this, Observer<User> { user ->
            bindUserData(user)
        } )

        homeScreenViewModel.userNotesDesc.observe(this, Observer<List<Note>> { list ->
            list?.let { notesList ->
                adapter.setNotes(notesList) }
        })
    }


    // Menu functionality
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_screen, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout_menu_item -> signOut()
            R.id.sort_asc_menu_item -> sortByDateAsc()
            R.id.sort_desc_menu_item -> sortByDateDesc()
        }

        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("InflateParams")
    override fun onNoteItemLongClick(note: Note, position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_home_screen, null, false)

        val delete = view.findViewById<ImageView>(R.id.bottom_sheet_delete)
        val share = view.findViewById<ImageView>(R.id.bottom_sheet_share)

        delete.setOnClickListener {
            deleteNote(note, position)
            bottomSheetDialog.dismiss()
        }

        share.setOnClickListener {
            shareNote(note)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }


    override fun onNoteItemClick(note: Note, position: Int) {
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("note", note)
        startActivity(intent)
    }


    private fun signOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(this, gso).signOut()
        fbAuth.signOut()

        val intent = Intent(applicationContext, StartupActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }


    private fun sortByDateAsc() {
        homeScreenViewModel.userNotesAsc.observe(this, Observer<List<Note>> { list ->
            list?.let { notesList ->
                adapter.setNotes(notesList) }
        })
    }


    private fun sortByDateDesc() {
        homeScreenViewModel.userNotesDesc.observe(this, Observer<List<Note>> { list ->
            list?.let { notesList ->
                adapter.setNotes(notesList) }
        })
    }


    private fun addNewNote() {
        add_new_note_button.setOnClickListener {
            val intent = Intent(applicationContext, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }


    private fun deleteNote(note: Note, position: Int) {
        homeScreenViewModel.deleteNote(note)
        adapter.deleteNote(note, position)
        Toast.makeText(applicationContext, "Note deleted!", Toast.LENGTH_LONG).show()
    }


    private fun shareNote(note: Note) {

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Title: ${note.title} \nContent: ${note.content} \n${note.image}")
            type = "text/plain"
        }

        startActivity(Intent.createChooser(shareIntent, "Share"))
    }


    private fun bindUserData(user: User?) {
        Log.d(HOME_DEBUG, user.toString())
    }
}

