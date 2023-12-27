package com.example.weekplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvToday;

    private TextView tvYesterday;
    private TextView tvTomorrow;
    private Calendar selectedDate;

    private Calendar selectedDateView;

    private static final String PREFS_FILE_NAME = "";
    private static final String SELECTED_LANGUAGE_KEY = "";

    private static final int EDIT_ACTIVITY_REQUEST_CODE = 123;

    private FragmentList taskListFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String savedLanguage = getAppLanguage();
        applyLanguage(savedLanguage);

        setContentView(R.layout.activity_main);

        taskListFragment = (FragmentList) getSupportFragmentManager().findFragmentById(R.id.fragment1);

        if (taskListFragment == null) {
            // Фрагмент еще не добавлен в макет, создадим и добавим его программно
            taskListFragment = new FragmentList();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment1, taskListFragment)
                    .commit();
        }

        TextView tvCurrentDate = findViewById(R.id.tvCurrentDate);

        selectedDate = Calendar.getInstance();

        // Отображаем задачи на сегодня
        displayTasks();


        // Форматирование текущей даты и дня недели
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, EEEE", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Установка текста в TextView
        tvCurrentDate.setText(currentDate);

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
        selectedDateView = Calendar.getInstance();

        // Установка текста в TextView
        updateDateLabels();

        // Назначаем слушатели на кнопки переключения дней
        ImageButton btnPreviousDay = findViewById(R.id.btnPreviousDay);
        ImageButton btnNextDay = findViewById(R.id.btnNextDay);

        btnPreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDateView.add(Calendar.DAY_OF_YEAR, -1);
                updateDateLabels(); // Переключение на предыдущий день
            }
        });


        btnNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDateView.add(Calendar.DAY_OF_YEAR, 1);
                updateDateLabels(); // Переключение на следующий день
            }
        });
        tvYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Выбор даты, представленной в tvYesterday
                String dateString = tvYesterday.getText().toString();
                selectedDate = convertStringToDate(dateString);
                Log.d("1", "Selected date: " + dateString);
                displayTasks(); // Отображение задач для новой даты
            }
        });

        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Выбор даты, представленной в tvToday
                String dateString = tvToday.getText().toString();
                selectedDate = convertStringToDate(dateString);
                Log.d("1", "Selected date: " + dateString);
                displayTasks(); // Отображение задач для новой даты
            }
        });

        tvTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Выбор даты, представленной в tvTomorrow
                String dateString = tvTomorrow.getText().toString();
                selectedDate = convertStringToDate(dateString);
                Log.d("1", "Selected date: " + dateString);
                displayTasks(); // Отображение задач для новой даты
            }
        });


    }

    private void displayTasks() {
        // Получите задачи из базы данных для выбранной даты
        Database dbHelper = new Database(this);
        List<Task> taskList = dbHelper.getTasks(selectedDate);

        Log.d("вызывали метод", "Selected date: " + taskList);

        // Получите ссылку на ваш фрагмент
        FragmentList taskListFragment = (FragmentList) getSupportFragmentManager().findFragmentById(R.id.fragment1);

        // Вызовите метод фрагмента для обновления данных
        if (taskListFragment != null) {
            taskListFragment.updateTasks(taskList);
            Log.d("вызывали метод", "Selected date: ");
        }
    }



    private Calendar convertStringToDate(String dateString) {
        String savedLanguage = getAppLanguage();
        Locale locale;

        if (!savedLanguage.isEmpty()) {
            locale = new Locale(savedLanguage);
        } else {
            locale = Locale.getDefault();
        }


        Log.d("2", "Selected date: " + dateString);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM", locale);
        Calendar calendar = new GregorianCalendar();  // Используем GregorianCalendar вместо Calendar
        Log.d("2", "Selected date: " + dateString);
        try {
            Date date = sdf.parse(dateString);
            Log.d("3", "Selected date: " + date);
            calendar.setTime(date);

            // Получаем текущий год
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            // Устанавливаем текущий год в объект Calendar
            calendar.set(Calendar.YEAR, currentYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
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
        tvToday.setText(sdf.format(selectedDateView.getTime()));

        Calendar yesterday = (Calendar) selectedDateView.clone();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        tvYesterday.setText(sdf.format(yesterday.getTime()));

        Calendar tomorrow = (Calendar) selectedDateView.clone();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        tvTomorrow.setText(sdf.format(tomorrow.getTime()));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Результат из EditActivity: задача была обновлена, обновим список задач
            displayTasks();
        }
    }
}
