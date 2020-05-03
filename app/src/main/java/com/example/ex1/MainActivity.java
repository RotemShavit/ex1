package com.example.ex1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String TAG ="MAIN";
    private ArrayList<String> todo_list = new ArrayList<>();
    private ArrayList<Integer> done_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edit = (EditText) findViewById(R.id.EditText);
        final Button button = (Button) findViewById(R.id.create_button);
        final RecyclerView recycle = (RecyclerView) findViewById(R.id.recycle);

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
                    todo_list.add(str);
                    done_list.add(1);
                    initRecyclerView();
                }
            }
        });
    }

    private void initTodo()
    {
        todo_list.add("OS week 4");
        todo_list.add("DSP ex1");
        todo_list.add("OS ex2");
        todo_list.add("PostPC ex3");
        todo_list.add("PostPC ex4");
        todo_list.add("PostPC ex5");
        todo_list.add("PostPC ex6");
        todo_list.add("PostPC ex7");
        initRecyclerView();
    }

    private void initRecyclerView()
    {
        Log.d(TAG, "init");
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
                    String todo = todo_list.get(position);
                    todo_list.set(position , "Done :) -> " + todo);
                    ad.notifyItemChanged(position);
                    done_list.set(position, 0);
                    Toast.makeText(context, "TODO" + todo + "is now DONE. BOOM!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
