package com.example.ex1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    @NonNull

    private ArrayList<String> todo_string = new ArrayList<>();
    private Context context;

    public TodoAdapter(@NonNull ArrayList<String> todo_string, Context context) {
        this.todo_string = todo_string;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()). inflate(R.layout.item_todoboom, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                 holder.todo_txt.setText(todo_string.get(position));
                 holder.single_layout.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         // write logic
                     }
                 });
    }

    @Override
    public int getItemCount() {
        return todo_string.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView todo_txt;
        ConstraintLayout single_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todo_txt = itemView.findViewById(R.id.single_todo);
            single_layout = itemView.findViewById(R.id.todo_layout);
        }
    }
}