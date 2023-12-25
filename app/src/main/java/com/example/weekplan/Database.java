package com.example.weekplan;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

    // Метод для получения данных из базы данных
    public List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Задайте SQL-запрос для выборки данных
        String selectQuery = "SELECT * FROM tasks";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // Перебираем результаты запроса
        if (cursor.moveToFirst()) {
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
        cursor.close();
        db.close();

        return taskList;
    }
}


