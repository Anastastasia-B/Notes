package com.study.notes.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.notes.R;
import com.study.notes.activities.CreateNoteActivity;
import com.study.notes.entities.Note;
import com.study.notes.listeners.NoteListener;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import java.util.logging.Logger;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private NoteListener noteListener;
    private Timer timer;
    private List<Note> allNotes;

    public NotesAdapter(List<Note> notes, NoteListener noteListener) {
        this.notes = notes;
        this.noteListener = noteListener;
        allNotes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListener.onNoteClicked(notes.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDateTime;
        LinearLayout layoutNote;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.titleText);
            textDateTime = itemView.findViewById(R.id.dateTimeText);
            layoutNote = itemView.findViewById(R.id.noteContainer);
        }

        @SuppressLint("Range")
        void setNote(Note note) {
            textTitle.setText(note.getTitle());
            textDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null && note.getColor().charAt(0) == '#') {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#636161"));
            }
        }
    }

    public void searchNotes(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    notes = allNotes;
                } else {
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note : allNotes) {
                        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
