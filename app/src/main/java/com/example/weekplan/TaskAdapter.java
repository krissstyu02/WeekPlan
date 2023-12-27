package com.example.weekplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends ArrayAdapter<Task> {

    private static final int EDIT_ACTIVITY_REQUEST_CODE = 123;
    private List<Task> tasks;


    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
        this.tasks = tasks;
        Log.d("adapter", "Number of tasks: " + tasks.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item_2, parent, false);
        }

        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        ImageView deleteIcon = convertView.findViewById(R.id.deleteIcon);

        // Загрузка состояния чекбокса из SharedPreferences
        SharedPreferences prefs = getContext().getSharedPreferences("checkbox_state", Context.MODE_PRIVATE);
        boolean isChecked = prefs.getBoolean("task_" + task.getId(), false);
        checkBox.setChecked(isChecked);
        Log.d("адаптер", "Selected date: " );

        if (task != null) {
            text1.setText(task.getTitle());
            text2.setText(task.getDescription());
            timeTextView.setText(task.getTime()); // Устанавливаем время

            Log.d("вызывали метод", "Selected date: " + task);

            // Установка слушателя для удаления задачи
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Удаление из базы данных
                    Database dbHelper = new Database(getContext());
                    dbHelper.deleteTask(task.getId());

                    // Удаление из списка и обновление
                    remove(task);
                    notifyDataSetChanged();
                }
            });

            // Установка слушателя для изменения состояния чекбокса
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Сохранение состояния чекбокса в SharedPreferences
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("task_" + task.getId(), isChecked);
                    editor.apply();

                    // Обработка изменения состояния чекбокса
                    // Можете добавить здесь код для обновления состояния задачи в базе данных
                }
            });
        }

        final Task currentTask = getItem(position);

        // Set an OnClickListener for the edit icon
        ImageView editIcon = convertView.findViewById(R.id.editIcon);
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit action, you may open an edit activity here
                openEditActivity(currentTask);
            }
        });

        return convertView;
    }

    private void openEditActivity(Task task) {
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra("taskId", task.getId()); // You need to define getId() in your Task class
        ((Activity) getContext()).startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
    }
    public List<Task> getTasks() {
        return tasks;
    }

    public void sortTasksByTime() {
        Collections.sort(getTasks(), new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                // Ваша логика сравнения времени задач, например, с использованием SimpleDateFormat
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

                try {
                    Date time1 = sdf.parse(task1.getTime());
                    Date time2 = sdf.parse(task2.getTime());

                    // Сравнение времени
                    return time1.compareTo(time2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });

        notifyDataSetChanged();
    }



}
