package com.example.ex1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
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
            Log.d(TAG, "todo init if");
            Log.d(TAG, "todo if, saved = " + savedInstanceState);
            String t = savedInstanceState.getString("todo_list");
            String d = savedInstanceState.getString("done_list");
            String[] list_t = t.split(";");
            String[] list_d = d.split(";");

            for(int i = 0; i < list_t.length - 1; i = i + 2)
            {
                    todo_list.add(new TodoWithImage(list_t[i], Integer.parseInt(list_t[i + 1])));
            }
            for(String i : list_d)
            {
                done_list.add(Integer.parseInt(i));
            }
            initRecyclerView();
        }
        else
        {
            Log.d(TAG, "todo init else");
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            String t = sp.getString("todo_list", null);
            String d = sp.getString("done_list", null);
            Log.d(TAG, "todo String t: " + t);
            Log.d(TAG, "todo String d: " + d);
            if(t != null && d != null && !t.equals("") && !d.equals(""))
            {
                String[] list_t = t.split(";");
                String[] list_d = d.split(";");
                for(int i = 0; i < list_t.length - 1; i = i + 2)
                {
                    todo_list.add(new TodoWithImage(list_t[i], Integer.parseInt(list_t[i + 1])));
                }
                for(String i : list_d)
                {
                    done_list.add(Integer.parseInt(i));
                }
                initRecyclerView();
            }
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

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("todo_list", todo_list_to_string());
                    editor.putString("done_list", done_list_to_string());
                    editor.apply();
                    initRecyclerView();
                }
            }
        });
    }

    private void initRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.recycle);
        final TodoAdapter ad = new TodoAdapter(todo_list, this);
        rv.setAdapter(ad);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ad.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // logic when clicked!
                Context context = getApplicationContext();
                openActivity(todo_list.get(position), position);
            }

            @Override
            public boolean onLongClick(int position) {
                Context context = getApplicationContext();
                TodoWithImage todo = todo_list.get(position);
                openDialog(position);
                return true;
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
            outState.putString("todo_list", todo_list_to_string());
            outState.putString("done_list", done_list_to_string());
        }
        else
        {
            outState = null;
        }
    }

    public void openDialog(final int position)
    {
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setOnDeleteClickListener(new DeleteDialog.OnDeleteClickListener() {
            @Override
            public void onPosClick() {
                delete_todo(position);
            }

            @Override
            public void onNegClick() {
            }
        });
        deleteDialog.show(getSupportFragmentManager(), "delete dialog");
    }


    private String todo_list_to_string()
    {
        String s = "";
        for (TodoWithImage a : todo_list)
        {
            s = s + a.getString() + ";" + String.valueOf(a.getImage()) + ";";
        }
        return s;
    }

    private String done_list_to_string()
    {
        String d = "";
        for (int a : done_list)
        {
            d = d + a + ";";
        }
        return d;
    }

    private void openActivity(TodoWithImage todo_item, int position)
    {
        if(done_list.get(position) == 1)
        {
            Intent intent = new Intent(this, TodoEditActivity.class);
            intent.putExtra("todo_string", todo_item.getString());
            intent.putExtra("pos", position);
            startActivityForResult(intent, 1);
        }
        else
        {
            Log.d(TAG, "todo done activity");
            Intent intent = new Intent(this, TodoDoneActivity.class);
            intent.putExtra("todo_string", todo_item.getString());
            intent.putExtra("pos", position);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // return from edit activity
            if (resultCode == RESULT_OK) {
                int pos = data.getIntExtra("pos", 0);
                String s = data.getStringExtra("updated_text");
                Log.d(TAG,"kaka: " + s);
                todo_list.get(pos).changeText(s);
                if(data.getStringExtra("is_done").equals("yes"))
                {
                    todo_list.get(pos).changeImage(R.drawable.todo_done_foreground);
                    done_list.set(pos, 0);
                    Toast.makeText(this, "TODO " + todo_list.get(pos).getString() + " is now DONE. BOOM!", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("done_list", done_list_to_string());
                editor.putString("todo_list", todo_list_to_string());
                editor.apply();
                initRecyclerView();
            }
        }
        else if(requestCode == 2)
        {
            // return from done activity
            if(resultCode == RESULT_OK)
            {
                int pos = data.getIntExtra("pos", 0);
                String is_done = data.getStringExtra("is_done");
                if(is_done.equals("no"))
                {
                    done_list.set(pos, 1);
                    todo_list.get(pos).changeImage(0);
                    initRecyclerView();
                }
                String to_delete = data.getStringExtra("to_delete");
                if(to_delete.equals("yes"))
                {
                    delete_todo(pos);
                }
            }
        }
    }

    private void delete_todo(int position)
    {
        todo_list.remove(position);
        done_list.remove(position);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("todo_list", todo_list_to_string())
                .putString("done_list", done_list_to_string());
        initRecyclerView();
        editor.apply();
    }
}
