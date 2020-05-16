package com.example.ex1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TodoEditActivity extends AppCompatActivity {

    private static String TAG = "edit activity";

    EditText editText;
    Button apply;
    Button done;
    String is_done = "no";
    String updated_text;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "openactivity: bla bla bla");

        setContentView(R.layout.activity_todo_edit);

        editText = findViewById(R.id.todo_edit_Edit_text);
        apply = findViewById(R.id.apply_button);
        done = findViewById(R.id.done_button);

        Intent intent = getIntent();
        editText.setText(intent.getStringExtra("todo_string"));
        pos = intent.getIntExtra("pos", 0);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "edit: clicked on apply");
                updated_text = editText.getText().toString();
                returnResultsToMainActivity();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "edit: clicked on apply");
                is_done = "yes";
                updated_text = editText.getText().toString();
                returnResultsToMainActivity();
            }
        });
    }

    private void returnResultsToMainActivity()
    {
        Intent intentBack = new Intent();
        intentBack.putExtra("updated_text", updated_text);
        intentBack.putExtra("is_done", is_done);
        intentBack.putExtra("pos", pos);
        setResult(RESULT_OK, intentBack);
        finish();
    }
}
