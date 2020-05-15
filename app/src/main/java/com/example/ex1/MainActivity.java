package com.example.ex1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String TAG ="MainActivity";
    //private ArrayList<String> todo_list = new ArrayList<>();
    private ArrayList<TodoWithImage> todo_list = new ArrayList<>();
    private ArrayList<Integer> done_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "todo init");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edit = (EditText) findViewById(R.id.EditText);
        final Button button = (Button) findViewById(R.id.create_button);
        final RecyclerView recycle = (RecyclerView) findViewById(R.id.recycle);
        if(savedInstanceState != null && savedInstanceState.getString("todo_list") != null)
        {
            Log.d(TAG, "todo if, saved = " + savedInstanceState);
            String s = savedInstanceState.getString("todo_list");
            String d = savedInstanceState.getString("done");
            String[] list_s = s.split(";");
            String[] list_d = d.split(";");

            for(int i = 0; i < list_s.length - 1; i = i + 2)
            {
                    todo_list.add(new TodoWithImage(list_s[i], Integer.parseInt(list_s[i + 1])));
            }
            for(String i : list_d)
            {
                done_list.add(Integer.parseInt(i));
            }
            initRecyclerView();
        }

        //initTodo();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String str = edit.getText().toString();
                String empty_str = "";
                String error_msg = "OOPS!\nYOU CAN NOT CREATE AN EMPTY TODOBOOM :)";
                edit.setText(empty_str);
                Context context = getApplicationContext();
                if(str.equals(empty_str))
                {
                    Toast.makeText(context, error_msg, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    TodoWithImage cur = new TodoWithImage(str, 0);
                    todo_list.add(cur);
                    done_list.add(1);
                    initRecyclerView();
                }
            }
        });
    }

//    private void initTodo()
//    {
//        todo_list.add("OS week 4");
//        todo_list.add("DSP ex1");
//        todo_list.add("OS ex2");
//        todo_list.add("PostPC ex3");
//        todo_list.add("PostPC ex4");
//        todo_list.add("PostPC ex5");
//        todo_list.add("PostPC ex6");
//        todo_list.add("PostPC ex7");
//        initRecyclerView();
//    }

    private void initRecyclerView()
    {
        //Log.d(TAG, "init");
        RecyclerView rv = findViewById(R.id.recycle);
        final TodoAdapter ad = new TodoAdapter(todo_list, this);
        rv.setAdapter(ad);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ad.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // logic when clicked!
                Context context = getApplicationContext();
                if(done_list.get(position) == 1)
                {
                    TodoWithImage todo = todo_list.get(position);
                    //todo_list.set(position , "Done :) -> " + todo);
                    //todo.changeText("Done :) -> " + todo.getString());
                    todo.changeImage(R.drawable.todo_done_foreground);
                    ad.notifyItemChanged(position);
                    done_list.set(position, 0);
                    Toast.makeText(context, "TODO " + todo.getString() + " is now DONE. BOOM!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "todo size: " + todo_list.size());
        if(todo_list.size() > 0)
        {
            String s = "";
            String d = "";
            for (TodoWithImage a : todo_list)
            {
                s = s + a.getString() + ";" + String.valueOf(a.getImage()) + ";";
            }
            for (int i : done_list)
            {
                d = d + String.valueOf(i) + ";";
            }
            outState.putString("todo_list", s);
            outState.putString("done", d);
        }
        else
        {
            outState = null;
        }
    }
}
