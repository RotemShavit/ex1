package com.example.ex1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    // @NonNull

    private static String TAG ="TODOAdapter";

    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
        boolean onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    //private ArrayList<String> todo_string = new ArrayList<>();
    private ArrayList<TodoWithImage> todo_string = new ArrayList<>();
    private Context context;

    public TodoAdapter(@NonNull ArrayList<TodoWithImage> todo_string, Context context) {
        this.todo_string = todo_string;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todoboom, parent, false);
        ViewHolder holder = new ViewHolder(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
                 holder.todo_txt.setText(todo_string.get(position).getString());
                 holder.image.setImageResource(todo_string.get(position).getImage());
//                 holder.single_layout.setOnClickListener(new View.OnClickListener() {
//                     @Override
//                     public void onClick(View v) {
//                         // write logic
//                         Log.d(TAG, "Clicked on: " + todo_string.get(position));
//                     }
//                 });
    }

    @Override
    public int getItemCount() {
        return todo_string.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView todo_txt;
        ConstraintLayout single_layout;
        ImageView image;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            todo_txt = itemView.findViewById(R.id.single_todo);
            single_layout = itemView.findViewById(R.id.todo_layout);
            image = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "clicked");
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            //image.setImageResource(R.drawable.ic_launcher_background);
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG, "long clicked");
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            //image.setImageResource(R.drawable.ic_launcher_background);
                            listener.onLongClick(position);
                        }
                    }
                    return true;
                }
            });
        }
    }


}
