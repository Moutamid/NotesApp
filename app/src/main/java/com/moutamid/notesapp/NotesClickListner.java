package com.moutamid.notesapp;

import com.moutamid.notesapp.models.NotesModel;

public interface NotesClickListner {
    void onClick(NotesModel notesModel);
    void onClickDelete(NotesModel notesModel);
}
