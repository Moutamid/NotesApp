package com.moutamid.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moutamid.notesapp.adapter.NotesAdapter;
import com.moutamid.notesapp.database.RoomDB;
import com.moutamid.notesapp.models.NotesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<NotesModel> notesList;
    NotesAdapter adapter;
    RoomDB database;
    ExtendedFloatingActionButton fab, fab_donate;
    SearchView searchView;
    NotesModel selectedNote;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.fab_add);
        fab_donate = findViewById(R.id.fab_donate);
        searchView = findViewById(R.id.search_view);

        notesList = new ArrayList<>();

        database = RoomDB.getInstance(this);

        notesList = database.mainDAO().getAll();

        UpdateRecycler(notesList);

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to Delete this Note?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            database.mainDAO().Delete(selectedNote);
            notesList.remove(selectedNote);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Note Deleted Successfully", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });

        fab.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivityForResult(i, 1);
        });
        fab_donate.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, DonateActivity.class);
            startActivityForResult(i, 1);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String newText) {
        List<NotesModel> filterdList = new ArrayList<>();
        for (NotesModel singleNote : notesList){
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase()) || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filterdList.add(singleNote);
            }
        }
        adapter.filterList(filterdList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode == Activity.RESULT_OK) {
                NotesModel notesModel = (NotesModel) data.getSerializableExtra("note");
                database.mainDAO().insert(notesModel);
                notesList.clear();
                notesList.addAll(database.mainDAO().getAll());
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK) {
                NotesModel newNotes = (NotesModel) data.getSerializableExtra("note");
                database.mainDAO().update(newNotes.getID(), newNotes.getTitle(), newNotes.getNotes(), newNotes.getDate());
                notesList.clear();
                notesList.addAll(database.mainDAO().getAll());
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void UpdateRecycler(List<NotesModel> notesList) {
        adapter = new NotesAdapter(MainActivity.this, notesList, clickListner);
        recyclerView.setAdapter(adapter);
    }

    private final NotesClickListner clickListner = new NotesClickListner() {
        @Override
        public void onClick(NotesModel notesModel) {
            Intent i = new Intent(getApplicationContext(), AddNoteActivity.class);
            i.putExtra("old_note", notesModel);
            startActivityForResult(i, 2);
        }

        @Override
        public void onClickDelete(NotesModel notesModel) {
            selectedNote = new NotesModel();
            selectedNote = notesModel;
            showPopUp();
        }
    };

    private void showPopUp() {
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.privacy_menu, menu);
        MenuItem menuShare = menu.findItem(R.id.action_privacy);

        menuShare.setOnMenuItemClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/e/2PACX-1vSiVOyF-G9UAO1U1DfthqeF1bG6mCxH5d39W1s8NNQw-p-ctSOSgq4Vhu684RqnDVZBIl9iIoDTVNvf/pub"));
            startActivity(browserIntent);
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }
}