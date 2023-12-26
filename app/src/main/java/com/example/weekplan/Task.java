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

    public Task(int id, String title, String description, String date, String time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
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
}

