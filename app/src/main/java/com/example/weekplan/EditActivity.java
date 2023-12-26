package com.example.weekplan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

// EditActivity class
public class EditActivity extends AppCompatActivity {
    private EditText etNoteName;
    private EditText etDate;
    private EditText etTime;
    private EditText description;
    private Button btnSaveAndClose;

    private int taskId; // Task ID to be edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_page);

        // Initialize your UI components
        etNoteName = findViewById(R.id.etNoteName);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        description = findViewById(R.id.description);
        btnSaveAndClose = findViewById(R.id.btnAction);

        // Get the taskId from the intent
        taskId = getIntent().getIntExtra("taskId", -1);

        // Load the existing task details based on taskId and populate the UI fields
        loadTaskDetails();

        // Set an OnClickListener for the save button
        btnSaveAndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle save action, update the existing task with new details
                updateTask();
            }
        });

        Button btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadTaskDetails() {
        // Use taskId to fetch existing task details from the database
        Database dbHelper = new Database(this);
        Task existingTask = dbHelper.getTaskById(taskId);

        // Populate UI fields with existing details
        if (existingTask != null) {
            etNoteName.setText(existingTask.getTitle());
            etDate.setText(existingTask.getDate());
            etTime.setText(existingTask.getTime());
            description.setText(existingTask.getDescription());
        }
    }


    private void updateTask() {
        // Get the new values from the UI fields
        String newNoteName = etNoteName.getText().toString();
        String newDate = etDate.getText().toString();
        String newTime = etTime.getText().toString();
        String newDescription = description.getText().toString();

        // Update the existing task in the database
        Database dbHelper = new Database(this);
        dbHelper.updateTask(taskId, newNoteName, newDescription, newDate, newTime);

        setResult(RESULT_OK);

        // Close the EditActivity
        finish();
    }
}

