package com.example.weekplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_page);

        // Инициализация кнопки и установка слушателя
        Button btnAddNote = findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ваш код
                String message = getString(R.string.add);
                showToast(message);

                // Переход на главную страницу
                openMainPage();
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
