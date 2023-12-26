package com.example.weekplan;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private EditText etNoteName;
    private EditText etDate;
    private EditText etTime;
    private EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_page);

        etNoteName = findViewById(R.id.etNoteName);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etDescription = findViewById(R.id.description);

        // Инициализация кнопки и установка слушателя
        Button btnAddNote = findViewById(R.id.btnAction);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получите данные из полей ввода
                String title = etNoteName.getText().toString();
                String date = etDate.getText().toString();
                String time = etTime.getText().toString();
                String description = etDescription.getText().toString();

                // Добавьте данные в базу данных
                Database dbHelper = new Database(AddActivity.this);
                long newRowId = dbHelper.addTask(title, description, date, time);

                // Проверьте успешность операции
                if (newRowId != -1) {
                    showToast(getString(R.string.task_added_successfully));
                } else {
                    showToast(getString(R.string.error_adding_task));
                }

                // Переход на главную страницу
                openMainPage();
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void openMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Опционально, зависит от вашей логики
    }
}
