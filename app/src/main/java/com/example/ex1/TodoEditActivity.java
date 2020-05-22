package com.example.ex1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TodoEditActivity extends AppCompatActivity {

    private static String TAG = "edit activity";

    EditText editText;
    Button apply;
    Button done;
    String is_done = "no";
    String updated_text;
    TextView create_time;
    TextView edit_time;
    String create_time_str;
    String edit_time_str;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_edit);

        editText = findViewById(R.id.todo_edit_Edit_text);
        apply = findViewById(R.id.apply_button);
        done = findViewById(R.id.done_button);
        create_time = findViewById(R.id.create_time);
        edit_time = findViewById(R.id.edit_time);

        Intent intent = getIntent();
        editText.setText(intent.getStringExtra("todo_string"));
        pos = intent.getIntExtra("pos", 0);
        String create_time_temp_str = intent.getStringExtra("create");
        create_time_str = create_time_temp_str.substring(0, create_time_temp_str.length() - 18);
        String edit_time_temp_str = intent.getStringExtra("edit");
        edit_time_str = edit_time_temp_str.substring(0, edit_time_temp_str.length() - 18);


        create_time.setText("created on " + create_time_str);
        edit_time.setText("edited on " + edit_time_str);

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
