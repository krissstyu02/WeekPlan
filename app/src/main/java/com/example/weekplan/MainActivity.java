package com.example.weekplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvToday;
    private TextView tvYesterday;
    private TextView tvTomorrow;
    private Calendar selectedDate;

    private static final String PREFS_FILE_NAME = "";
    private static final String SELECTED_LANGUAGE_KEY = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String savedLanguage = getAppLanguage();
        applyLanguage(savedLanguage);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Получаем ссылку на кнопку добавления
        Button addButton = findViewById(R.id.addButton);

        // Назначаем слушатель кликов на кнопку добавления
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Открываем новую активность (AddNoteActivity)
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        tvToday = findViewById(R.id.tvToday);
        tvYesterday = findViewById(R.id.tvYesterday);
        tvTomorrow = findViewById(R.id.tvTomorrow);
        selectedDate = Calendar.getInstance();

        // Установка текста в TextView
        updateDateLabels();

        // Назначаем слушатели на кнопки переключения дней
        ImageButton btnPreviousDay = findViewById(R.id.btnPreviousDay);
        ImageButton btnNextDay = findViewById(R.id.btnNextDay);

        btnPreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate.add(Calendar.DAY_OF_YEAR, -1);
                updateDateLabels(); // Переключение на предыдущий день
            }
        });


        btnNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate.add(Calendar.DAY_OF_YEAR, 1);
                updateDateLabels(); // Переключение на следующий день
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.rus) {
            setLocale("ru");
            updateDateLabels();
            return true;
        } else if (item.getItemId() == R.id.eng) {
            setLocale("en");
            updateDateLabels();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setLocale(String languageCode) {
        // Сохранение выбранного языка в SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(SELECTED_LANGUAGE_KEY, languageCode).apply();

        // Применение нового языка
        applyLanguage(languageCode);

        // Перезапуск активности (требуется только для некоторых изменений, например, изменения языка)
        Intent refreshIntent = new Intent(this, MainActivity.class);
        refreshIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(refreshIntent);
        finish();
    }

    private void applyLanguage(String languageCode) {
        // Установка новой локали
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private String getAppLanguage() {
        // Используем язык, сохраненный в SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(SELECTED_LANGUAGE_KEY, "");
    }

//    private void setLocale(String languageCode) {
//        // Сохранение выбранного языка в SharedPreferences
//        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
//        prefs.edit().putString(SELECTED_LANGUAGE_KEY, languageCode).apply();
//
//        // Установка новой локали
//        Locale locale = new Locale(languageCode);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.setLocale(locale);
//        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
//
//        // Перезагрузка активности для применения изменений
//        Intent refreshIntent = new Intent(this, MainActivity.class);
//        refreshIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(refreshIntent);
//        finish();
//    }


    private void updateDateLabels() {
        String savedLanguage = getAppLanguage();
        Locale locale;

        if (!savedLanguage.isEmpty()) {
            locale = new Locale(savedLanguage);
        } else {
            locale = Locale.getDefault();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM", locale);


        // Установка текста в TextView
        tvToday.setText(sdf.format(selectedDate.getTime()));

        Calendar yesterday = (Calendar) selectedDate.clone();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        tvYesterday.setText(sdf.format(yesterday.getTime()));

        Calendar tomorrow = (Calendar) selectedDate.clone();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        tvTomorrow.setText(sdf.format(tomorrow.getTime()));
    }

}
