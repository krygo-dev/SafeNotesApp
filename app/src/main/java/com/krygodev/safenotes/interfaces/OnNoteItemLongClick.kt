package com.krygodev.safenotes.interfaces

import com.krygodev.safenotes.data.Note


interface OnNoteItemLongClick {

    fun onNoteItemLongClick(note: Note, position: Int) {}

}