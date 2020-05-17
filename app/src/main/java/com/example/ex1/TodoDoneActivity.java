package com.example.ex1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TodoDoneActivity extends AppCompatActivity {

    private static String TAG = "done activity";

    TextView editText;
    Button undone;
    Button delete;

    int pos;
    String is_done = "yes";
    String to_delete = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_done);

        editText = findViewById(R.id.todo_done_Edit_text);
        undone = findViewById(R.id.un_done_button);
        delete = findViewById(R.id.delete_button);

        Log.d(TAG, "todo done page");

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", -1);

        editText.setText(intent.getStringExtra("todo_string"));


        undone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "todo done page: clicked on undone");
                is_done = "no";
                returnResultsToMainActivity();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "todo done page: clicked on delete");
                openDialog();
            }
        });
    }



    private void openDialog()
    {
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setOnDeleteClickListener(new DeleteDialog.OnDeleteClickListener() {
            @Override
            public void onPosClick()
            {
                to_delete = "yes";
                returnResultsToMainActivity();
            }

            @Override
            public void onNegClick() {

            }
        });
        deleteDialog.show(getSupportFragmentManager(), "delete dialog");
    }

    private void returnResultsToMainActivity()
    {
        Log.d(TAG, "todo pos: "+pos + " is_done "+is_done+" to_delete "+to_delete);
        Intent intentBack = new Intent();
        intentBack.putExtra("is_done", is_done);
        intentBack.putExtra("to_delete", to_delete);
        intentBack.putExtra("pos", pos);
        setResult(RESULT_OK, intentBack);
        finish();
    }
}
