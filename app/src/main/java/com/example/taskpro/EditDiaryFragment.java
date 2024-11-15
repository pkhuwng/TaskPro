package com.example.taskpro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDiaryFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private String title;
    private String content;
    private String id;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference diaryRef;

    public EditDiaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param diary Diary entry needs to be edited.
     * @return A new instance of fragment EditDiaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDiaryFragment newInstance(Diary diary) {
        EditDiaryFragment fragment = new EditDiaryFragment();
        Bundle args = new Bundle();
        args.putString("title", diary.getTitle());
        args.putString("content", diary.getContent());
        args.putString("id", diary.getKey());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            content = getArguments().getString("content");
            id = getArguments().getString("id");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_diary, container, false);
        // Populate data for fragment
        TextInputEditText titleEditText = rootView.findViewById(R.id.titleEditText4);
        TextInputEditText contentEditText = rootView.findViewById(R.id.contentEditText4);
        Button saveButton = rootView.findViewById(R.id.saveButton4);

        titleEditText.setText(title);
        contentEditText.setText(content);

        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : "";

        // Tạo tham chiếu đến nút diary trong cơ sở dữ liệu Firebase
        diaryRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("diaries").child(id);

        saveButton.setOnClickListener(view -> {
            String title = titleEditText.getText().toString().trim();
            String content = contentEditText.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(getContext(), R.string.toast_please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng Diary mới
            Diary diary = new Diary();
            diary.setTitle(title);
            diary.setContent(content);
            diaryRef.setValue(diary).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), R.string.toast_diary_saved_successfully, Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack(); // Quay lại fragment trước
                } else {
                    Toast.makeText(getContext(), R.string.toast_failed_to_save_diary, Toast.LENGTH_SHORT).show();
                }
            });
        });


        return rootView;
    }

}