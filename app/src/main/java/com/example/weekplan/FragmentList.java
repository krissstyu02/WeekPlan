package com.example.weekplan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.List;

public class FragmentList extends Fragment {

    private ListView tasksListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.list_fragment, container, false);

        tasksListView = rootView.findViewById(R.id.tasksListView);


        return rootView; }

    public void updateTasks(List<Task> taskList) {
        Log.d("FragmentList", "Number of tasks: " + taskList.size());

        // Очистите адаптер перед добавлением новых задач
        tasksListView.setAdapter(null);

        // Создайте адаптер и установите его для ListView
        TaskAdapter taskAdapter = new TaskAdapter(requireContext(), taskList);
        tasksListView.setAdapter(taskAdapter);
    }


}

