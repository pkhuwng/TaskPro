package com.example.taskpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {
    private List<Diary> diaryList;
    private DatabaseReference diaryRef;
    private Context context;

    public DiaryAdapter(List<Diary> diaryList, DatabaseReference diaryRef, Context context) {
        this.diaryList = diaryList;
        this.diaryRef = diaryRef;
        this.context = context;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        int reversedPosition = getItemCount() - 1 - position;
        Diary diary = diaryList.get(reversedPosition);
        holder.bind(diary);

        // Xử lý sự kiện nhấn vào hình xóa
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(diary);
            }
        });

        // Xử lý sự kiện nhấn vào hình chỉnh sửa
        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo EditDiaryFragment và truyền đối tượng Diary
                EditDiaryFragment editDiaryFragment = EditDiaryFragment.newInstance(diary);
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, editDiaryFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void showDeleteConfirmationDialog(final Diary diary) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_delete_diary_entry);
        builder.setMessage(R.string.label_are_you_sure_you_want_to_delete_this_diary_entry);
        builder.setPositiveButton(R.string.dialog_positive_button_delete_task, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDiary(diary);
            }
        });
        builder.setNegativeButton(R.string.dialog_negative_button, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteDiary(Diary diary) {
        diaryRef.child(diary.getKey()).removeValue();
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder {
        private TextView monthTextView;
        private TextView dateTextView;
        private TextView yearTextView;
        private TextView titleTextView;
        private ImageView deleteImageView;
        private ImageView editImageView;
        private TextView contentTextView;
        private TextView timeTextView;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthYearTextView3);
            dateTextView = itemView.findViewById(R.id.dateTextView3);
            //yearTextView = itemView.findViewById(R.id.yearTextView3);
            titleTextView = itemView.findViewById(R.id.titleTextView3);
            deleteImageView = itemView.findViewById(R.id.deleteImageView3);
            editImageView = itemView.findViewById(R.id.editImageView3);
            contentTextView = itemView.findViewById(R.id.contentTextView3);
            timeTextView = itemView.findViewById(R.id.timeTextView3);
        }

        public void bind(Diary diary) {
            monthTextView.setText(getMonthString(diary.getMonth()));
            dateTextView.setText(String.valueOf(diary.getDay()));
            //yearTextView.setText(String.valueOf(diary.getYear()));
            titleTextView.setText(diary.getTitle());
            contentTextView.setText(diary.getContent());
            timeTextView.setText(diary.getHour());
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