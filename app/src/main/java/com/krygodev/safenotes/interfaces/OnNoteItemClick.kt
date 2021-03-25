package com.krygodev.safenotes.interfaces

import com.krygodev.safenotes.data.Note


interface OnNoteItemClick {

    fun onNoteItemClick(note: Note, position: Int) {}

}