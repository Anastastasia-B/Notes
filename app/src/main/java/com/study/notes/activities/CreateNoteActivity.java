package com.study.notes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.notes.R;
import com.study.notes.database.NotesDatabase;
import com.study.notes.entities.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteBody;
    private String[] colors = new String[] {"#FF03DAC5", "#FF018786", "#FF3700B3", "#FFBB86FC"};
    private Random random = new Random();

    private Note alreadyExistingNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView imageBackButton = findViewById(R.id.imageBack);
        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inputNoteTitle = findViewById(R.id.titleInput);
        inputNoteBody = findViewById(R.id.bodyInput);

        TextView saveButtonView = findViewById(R.id.saveNote);
        saveButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        
        if(getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyExistingNote = (Note) getIntent().getSerializableExtra("note");
            setUpdateNoteData();
        }
    }

    private void setUpdateNoteData() {
        inputNoteTitle.setText(alreadyExistingNote.getTitle());
        inputNoteBody.setText(alreadyExistingNote.getBody());
    }

    private void saveNote() {
        if (inputNoteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note title can't be blank.", Toast.LENGTH_SHORT).show();
            return;
        } else if(inputNoteBody.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note body can't be blank.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Note note = new Note();
        note.setTitle(inputNoteTitle.getText().toString());
        note.setBody(inputNoteBody.getText().toString());

        if (alreadyExistingNote == null) {
            String randomColor = colors[random.nextInt(colors.length)];
            note.setColor(randomColor);

            String dateTime = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date());
            note.setDateTime(dateTime);
        } else {
            note.setId(alreadyExistingNote.getId());
            note.setColor(alreadyExistingNote.getColor() == null
                            ? colors[random.nextInt(colors.length)]
                            : alreadyExistingNote.getColor());
            note.setDateTime(alreadyExistingNote.getDateTime());
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDao().insetNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);

                if (alreadyExistingNote == null) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ViewNoteActivity.class);
                    intent.putExtra("note", note);
                    startActivity(intent);
                }
            }
        }

        new SaveNoteTask().execute();
    }
}