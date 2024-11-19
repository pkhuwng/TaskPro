package com.example.taskpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private DatabaseReference tasksRef;
    private Context context;

    public TaskAdapter(List<Task> taskList, DatabaseReference tasksRef, Context context) {
        this.taskList = taskList;
        this.tasksRef = tasksRef;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        int reversedPosition = getItemCount() - 1 - position;
        Task task = taskList.get(reversedPosition);
        holder.bind(task);

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(task);
            }
        });

        holder.editImageView.setOnClickListener(view -> {
            navigateToEditTaskFragment(task);
        });
    }

    private void navigateToEditTaskFragment(Task task) {
        EditTaskFragment editTaskFragment = new EditTaskFragment();

        // Create a bundle to pass the task details
        Bundle args = new Bundle();
        args.putString("taskKey", task.getKey());
        args.putString("title", task.getTitle());
        args.putString("content", task.getContent());
        args.putString("time", task.getTime());
        args.putInt("month", task.getMonth());
        args.putInt("day", task.getDay());
        args.putString("date", task.getDate());
        args.putInt("year", task.getYear());

        editTaskFragment.setArguments(args);

        // Replace the current fragment with EditTaskFragment
        FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editTaskFragment);
        transaction.addToBackStack(null); // Add to back stack for navigation
        transaction.commit();
    }

    private void showDeleteConfirmationDialog(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_title_delete_task);
        builder.setMessage(R.string.dialog_message_delete_task);
        builder.setPositiveButton(R.string.dialog_positive_button_delete_task, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask(task);
            }
        });
        builder.setNegativeButton(R.string.dialog_negative_button, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteTask(Task task) {
        tasksRef.child(task.getKey()).removeValue();
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView monthTextView;
        private TextView dateTextView;
        private TextView dateInitTextView;
        private TextView yearTextView;
        private TextView titleTextView;
        private ImageView deleteImageView;
        private TextView contentTextView;
        private TextView timeTextView;
        private ImageView editImageView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            dateInitTextView = itemView.findViewById(R.id.dateInitTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            editImageView = itemView.findViewById(R.id.editImageView);
        }

        public void bind(Task task) {
            String month = getMonthString(task.getMonth());
            String day = String.valueOf(task.getDay());
            String date = String.valueOf(task.getDate());
            String year = String.valueOf(task.getYear());

            monthTextView.setText(month);
            dateTextView.setText(day);
            dateInitTextView.setText(date);
            yearTextView.setText(year);
            titleTextView.setText(task.getTitle());
            contentTextView.setText(task.getContent());
            timeTextView.setText(task.getTime());
        }

        private String getMonthString(int month) {
            if (month >= 1 && month <= 12) {
                Locale currentLocale = Locale.getDefault(); // Get the current locale
                String[] monthArray = new DateFormatSymbols(currentLocale).getMonths(); // Get localized month names
                return monthArray[month - 1];
            } else {
                return ""; // Return an empty string for invalid input
            }
        }
    }
}