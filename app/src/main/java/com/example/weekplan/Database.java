package com.example.weekplan;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "your_database_name.db";
    private static final int DATABASE_VERSION = 1;

    // Конструктор
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Вызывается при создании базы данных
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создайте таблицу для хранения данных
        String createTableQuery = "CREATE TABLE tasks ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT,"
                + "description TEXT,"
                + "date TEXT,"
                + "time TEXT)";
        db.execSQL(createTableQuery);
    }

    // Вызывается при обновлении базы данных (изменение версии)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновите структуру базы данных при необходимости
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }

    // Добавить методы для выполнения операций с базой данных, такие как вставка, обновление, выборка и удаление
    // Например:
    public long addTask(String title, String description, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("date", date);
        values.put("time", time);
        long newRowId = db.insert("tasks", null, values);
        db.close();
        return newRowId;
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tasks", "id=?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    // Метод для получения данных из базы данных для конкретной даты
    public List<Task> getTasks(Calendar date) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        if (date == null) {
            db.close();
            return taskList; // Возможно, стоит вернуть пустой список, если дата равна null
        }

        // Задайте SQL-запрос для выборки данных по дате
        String selectQuery = "SELECT * FROM tasks WHERE date = ?";


        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String formattedDate = sdf.format(date.getTime());


        Cursor cursor = db.rawQuery(selectQuery, new String[]{formattedDate});

        // Проверяем успешность выполнения запроса
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Создаем объект Task для хранения данных
                Task task = new Task(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("time"))
                );

                // Добавляем задачу в список
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // Закрываем курсор и базу данных
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return taskList;
    }

    public void updateTask(int taskId, String title, String description, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("date", date);
        values.put("time", time);

        db.update("tasks", values, "id=?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    public Task getTaskById(int taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Task task = null;

        Cursor cursor = db.query(
                "tasks",
                new String[]{"id", "title", "description", "date", "time"},
                "id=?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            task = new Task(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("description")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("time"))
            );
            cursor.close();
        }

        db.close();

        return task;
    }



}


