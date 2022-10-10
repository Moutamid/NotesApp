package com.moutamid.notesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.notesapp.NotesClickListner;
import com.moutamid.notesapp.R;
import com.moutamid.notesapp.models.NotesModel;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    Context context;
    List<NotesModel> list;
    NotesClickListner clickListner;

    public NotesAdapter(Context context, List<NotesModel> list, NotesClickListner clickListner) {
        this.context = context;
        this.list = list;
        this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false);
        return new NotesAdapter.NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        NotesModel notes = list.get(position);
        holder.title.setText(notes.getTitle());
        holder.notes.setText(notes.getNotes());
        holder.date.setText(notes.getDate());

        holder.delete.setOnClickListener(v -> {
            clickListner.onClickDelete(list.get(holder.getAdapterPosition()));
        });

        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onClick(list.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<NotesModel> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{
        MaterialCardView notes_container;
        TextView title, notes, date;
        CardView delete;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            notes_container = itemView.findViewById(R.id.notes_container);
            title = itemView.findViewById(R.id.tv_title);
            notes = itemView.findViewById(R.id.tv_notes);
            date = itemView.findViewById(R.id.tv_date);
            delete = itemView.findViewById(R.id.delete);

        }
    }
}
