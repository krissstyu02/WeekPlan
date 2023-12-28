package com.example.weekplan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class Task {
    private int id;
    private String title;
    private String description;
    private String date;
    private String time;
    private boolean isChecked; // Новое поле

    public Task(int id, String title, String description, String date, String time, boolean isChecked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    // Новый метод для получения состояния isChecked
    public boolean isChecked() {
        return isChecked;
    }
}
