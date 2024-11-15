package com.example.taskpro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDiaryFragment extends Fragment {

    private EditText titleEditText, contentEditText;
    private Button saveButton;
    private DatabaseReference diaryRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_diary, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        contentEditText = view.findViewById(R.id.contentEditText);
        saveButton = view.findViewById(R.id.saveButton);

        // Khởi tạo tham chiếu đến nút diary trong cơ sở dữ liệu Firebase
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        diaryRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("diaries");

        saveButton.setOnClickListener(v -> saveDiary());

        return view;
    }

    private void saveDiary() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Diary mới
        Diary diary = new Diary();
        diary.setTitle(title);
        diary.setContent(content);

        // Lưu vào Firebase
        diaryRef.push().setValue(diary).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), R.string.toast_diary_saved_successfully, Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack(); // Quay lại fragment trước
            } else {
                Toast.makeText(getContext(), R.string.toast_failed_to_save_diary, Toast.LENGTH_SHORT).show();
            }
        });
    }
}