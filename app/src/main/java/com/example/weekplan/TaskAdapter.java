package com.example.weekplan;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weekplan.Task;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item_2, parent, false);
        }

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        ImageView deleteIcon = convertView.findViewById(R.id.deleteIcon);

        if (task != null) {
            text1.setText(task.getTitle());
            text2.setText(task.getDescription());
            timeTextView.setText(task.getTime());

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
        }

        return convertView;
    }
}
