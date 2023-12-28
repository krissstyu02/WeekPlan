package com.example.weekplan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Класс EditActivity
public class EditActivity extends AppCompatActivity {
    private EditText etNoteName;
    private EditText etDate;
    private EditText etTime;
    private EditText description;
    private Button btnSaveAndClose;

    private int taskId; // Идентификатор задачи для редактирования

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_page);

        // Инициализация компонентов пользовательского интерфейса
        etNoteName = findViewById(R.id.etNoteName);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        description = findViewById(R.id.description);
        btnSaveAndClose = findViewById(R.id.btnAction);

        // Получение идентификатора задачи из интента
        taskId = getIntent().getIntExtra("taskId", -1);

        // Загрузка существующих данных о задаче по идентификатору и заполнение полей пользовательского интерфейса
        loadTaskDetails();

        // Назначение слушателя на кнопку сохранения
        btnSaveAndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Обработка действия сохранения, обновление существующей задачи новыми данными
                updateTask();
            }
        });

        // Настройка кнопки закрытия
        Button btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Метод для загрузки деталей задачи
    private void loadTaskDetails() {
        // Использование идентификатора задачи для получения существующих данных из базы данных
        Database dbHelper = new Database(this);
        Task existingTask = dbHelper.getTaskById(taskId);

        // Заполнение полей пользовательского интерфейса существующими данными
        if (existingTask != null) {
            etNoteName.setText(existingTask.getTitle());
            etDate.setText(existingTask.getDate());
            etTime.setText(existingTask.getTime());
            description.setText(existingTask.getDescription());
        }
    }

    // Метод для обновления задачи
    private void updateTask() {
        // Получение новых значений из полей пользовательского интерфейса
        String newNoteName = etNoteName.getText().toString();
        String newDate = etDate.getText().toString();
        String newTime = etTime.getText().toString();
        String newDescription = description.getText().toString();

        // Обновление существующей задачи в базе данных
        Database dbHelper = new Database(this);
        dbHelper.updateTask(taskId, newNoteName, newDescription, newDate, newTime);

        // Показ уведомления об изменении записи
        showToast(getString(R.string.edit));

        setResult(RESULT_OK);

        // Закрытие EditActivity
        finish();
    }

    // Метод для отображения всплывающего уведомления
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
