package com.moutamid.notesapp.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.moutamid.notesapp.models.NotesModel;

import java.util.List;

@Dao
public interface MainDAO {

    @Insert(onConflict = REPLACE)
    void insert(NotesModel notes);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<NotesModel> getAll();

    @Query("UPDATE notes SET title = :title, notes = :notes, date= :date where ID = :id")
    void update(int id, String title, String notes, String date);

    @Delete
    void Delete(NotesModel note);
}
