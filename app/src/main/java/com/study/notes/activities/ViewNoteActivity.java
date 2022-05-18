package com.study.notes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.notes.R;
import com.study.notes.entities.Note;

public class ViewNoteActivity extends AppCompatActivity {

    private TextView noteTitle, noteBody;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        ImageView imageBackButton = findViewById(R.id.imageBack);
        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        noteTitle = findViewById(R.id.titleTextView);
        noteBody = findViewById(R.id.bodyTextView);

        note = (Note) getIntent().getSerializableExtra("note");
        noteTitle.setText(note.getTitle());
        noteBody.setText(note.getBody());

        ImageView editButtonView = findViewById(R.id.editNote);
        editButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                intent.putExtra("isViewOrUpdate", true);
                intent.putExtra("note", note);
                startActivity(intent);
            }
        });
    }
}
