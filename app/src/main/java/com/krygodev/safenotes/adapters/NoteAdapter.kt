package com.krygodev.safenotes.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.krygodev.safenotes.R
import com.krygodev.safenotes.data.Note
import com.krygodev.safenotes.interfaces.OnNoteItemLongClick


class NoteAdapter(private val listener: OnNoteItemLongClick) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val notesList = ArrayList<Note>()


    fun setNotes(list: List<Note>) {
        notesList.clear()
        notesList.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_view, parent, false)
        return NoteViewHolder(view)
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val noteView = holder.itemView.findViewById<ConstraintLayout>(R.id.note_card_view)
        val title = holder.itemView.findViewById<TextView>(R.id.title_card_view)
        val content = holder.itemView.findViewById<TextView>(R.id.text_card_view)
        val image = holder.itemView.findViewById<ImageView>(R.id.note_image_card_view)

        notesList[holder.adapterPosition].color?.let { noteView.setBackgroundColor(it) }
        title.text = notesList[holder.adapterPosition].title
        content.text = notesList[holder.adapterPosition].content
        // Image potem
    }


    override fun getItemCount(): Int {
        return notesList.size
    }


    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnLongClickListener {
                listener.onNoteItemLongClick(notesList[adapterPosition], adapterPosition)
                true
            }
        }
    }
}