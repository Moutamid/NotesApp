package com.moutamid.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.moutamid.notesapp.models.NotesModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    Button save;
    EditText et_title, et_notes;
    NotesModel notes;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Your Note");

        et_title = findViewById(R.id.etTitle);
        et_notes = findViewById(R.id.etNote);
        save = findViewById(R.id.SaveNote);
        notes = new NotesModel();

        try {
            notes = (NotesModel) getIntent().getSerializableExtra("old_note");
            et_title.setText(notes.getTitle());
            et_notes.setText(notes.getNotes());
            isOldNote = true;
        } catch (Exception e){
            e.printStackTrace();
        }

        save.setOnClickListener(v -> {
            String title = et_title.getText().toString();
            String note = et_notes.getText().toString();

            if (note.isEmpty()){
                Toast.makeText(this, "Please Add Note", Toast.LENGTH_SHORT).show();
                return;
            }
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();

            if (!isOldNote){
                notes = new NotesModel();
            }

            notes.setTitle(title);
            notes.setNotes(note);
            notes.setDate(format.format(date));

            Intent i = new Intent();
            i.putExtra("note", notes);
            setResult(Activity.RESULT_OK, i);
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}