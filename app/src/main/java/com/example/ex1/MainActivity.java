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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongFunction;

import javax.sql.StatementEvent;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private ArrayList<TodoWithImage> todo_list = new ArrayList<>();
    private String ERRORMSG = "OOPS!\nYOU CAN NOT CREATE AN EMPTY TODOBOOM :)";
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edit = (EditText) findViewById(R.id.EditText);
        final Button button = (Button) findViewById(R.id.create_button);
        final RecyclerView recycle = (RecyclerView) findViewById(R.id.recycle);


        if (savedInstanceState != null && savedInstanceState.getString("todo_list") != null)
        {
            readBundle(savedInstanceState);
            initRecyclerView();
        }
        else
        {
            readFire();
            loadingDialog = new LoadingDialog();
            loadingDialog.show(getSupportFragmentManager(), "loading dialog");
        }
//        Todoboom app = (Todoboom) getApplicationContext();
//        this.todo_list = app.todo_list;
//        initRecyclerView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edit.getText().toString();
                edit.setText("");
                Context context = getApplicationContext();
                if (str.equals("")) {
                    Toast.makeText(context, ERRORMSG, Toast.LENGTH_SHORT).show();
                } else {
                    Date d = new Date();
                    TodoWithImage cur = new TodoWithImage(str, 0, 1, getID(), d.toString(), d.toString());
                    todo_list.add(cur);
                    saveToFire(cur);
                    initRecyclerView();
                }
            }
        });
    }

    private long getID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextLong(10_000_000_000L, 100_000_000_000L);
    }

    private void initRecyclerView() {
        RecyclerView rv = findViewById(R.id.recycle);
        final TodoAdapter ad = new TodoAdapter(todo_list, this);
        rv.setAdapter(ad);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ad.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // logic when clicked!
                openActivity(todo_list.get(position), position);
            }

            @Override
            public boolean onLongClick(int position) {
                openDialog(position);
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (todo_list.size() > 0) {
            outState.putString("todo_list", todo_list_to_string());
        } else {
            outState = null;
        }
    }

    public void openDialog(final int position) {
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setOnDeleteClickListener(new DeleteDialog.OnDeleteClickListener() {
            @Override
            public void onPosClick() {
                deleteFromFire(todo_list.get(position));
                delete_todo(position);
            }

            @Override
            public void onNegClick() {
            }
        });
        deleteDialog.show(getSupportFragmentManager(), "delete dialog");
    }


    private String todo_list_to_string() {
        String s = "";
        for (TodoWithImage a : todo_list) {
            s = s + a.getString() + ";" + a.getImage() + ";" + a.getIsDone() + ";" + a.getID() + ";"
            + a.getDate("create") + ";" + a.getDate("edit") + ";";
        }
        return s;
    }

    private void readBundle(Bundle bundle)
    {
        String s = bundle.getString("todo_list");
        String[] a = s.split(";");
        for(int i = 0; i < a.length; i = i + 6)
        {
            TodoWithImage cur = new TodoWithImage(a[i], Integer.parseInt(a[i+1]), Integer.parseInt(a[i+2]),
                    Long.parseLong(a[i+3]), a[i+4], a[i+5]);
            todo_list.add(cur);
        }
    }

    private void openActivity(TodoWithImage todo_item, int position)
    {
        if(todo_list.get(position).getIsDone() == 1)
        {
            Intent intent = new Intent(this, TodoEditActivity.class);
            intent.putExtra("todo_string", todo_item.getString());
            intent.putExtra("pos", position);
            intent.putExtra("create", todo_item.getDate("create"));
            intent.putExtra("edit", todo_item.getDate("edit"));
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
                if(!s.equals(todo_list.get(pos).getString()))
                {
                    todo_list.get(pos).changeText(s);
                    Date d = new Date();
                    todo_list.get(pos).changeDate(d.toString());
                }
                if(data.getStringExtra("is_done").equals("yes"))
                {
                    todo_list.get(pos).changeImage(R.drawable.todo_done_foreground);
                    todo_list.get(pos).changeIsDone(0);
                    Date d = new Date();
                    todo_list.get(pos).changeDate(d.toString());
                    Toast.makeText(this, "TODO " + todo_list.get(pos).getString() +
                            " is now DONE. BOOM!", Toast.LENGTH_SHORT).show();
                }
                updateFire(todo_list.get(pos));
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
                    todo_list.get(pos).changeIsDone(1);
                    todo_list.get(pos).changeImage(0);
                    Date d = new Date();
                    todo_list.get(pos).changeDate(d.toString());
                    updateFire(todo_list.get(pos));
                    initRecyclerView();
                }
                String to_delete = data.getStringExtra("to_delete");
                if(to_delete.equals("yes"))
                {
                    deleteFromFire(todo_list.get(pos));
                    delete_todo(pos);
                }
            }
        }
    }

    private void delete_todo(int position)
    {
        todo_list.remove(position);
        initRecyclerView();
    }

    private void saveToFire(TodoWithImage todo)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> todo_db = new HashMap<>();
        todo_db.put("ID", todo.getID());
        todo_db.put("String", todo.getString());
        todo_db.put("image", todo.getImage());
        todo_db.put("is_done", todo.getIsDone());
        todo_db.put("create", todo.getDate("create"));
        todo_db.put("edit", todo.getDate("edit"));
        db.collection("todo_items").document("" + todo.getID()).set(todo_db);
    }

    private void deleteFromFire(TodoWithImage todo)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("todo_items").document("" + todo.getID()).delete();
    }

    private void updateFire(TodoWithImage todo)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("todo_items").document("" + todo.getID())
                .update("String", todo.getString());
        db.collection("todo_items").document("" + todo.getID())
                .update("image", todo.getImage());
        db.collection("todo_items").document("" + todo.getID())
                .update("is_done", todo.getIsDone());
        db.collection("todo_items").document("" + todo.getID())
                .update("edit", todo.getDate("edit"));
    }

    private void readFire()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("todo_items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot doc : task.getResult())
                            {
                                TodoWithImage todo = new TodoWithImage(doc.get("String").toString(),
                                        Integer.parseInt(doc.get("image").toString())
                                        , Integer.parseInt(doc.get("is_done").toString())
                                        , Long.parseLong(doc.get("ID").toString())
                                        , doc.get("create").toString()
                                        , doc.get("edit").toString());
                                todo_list.add(todo);
                            }
                            Log.d(TAG, "todo_list - success");
                        }
                        else
                        {
                            Log.d(TAG, "todo_list - not success");
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                loadingDialog.dismiss();
                initRecyclerView();
            }
        });
    }
}
