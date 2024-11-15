package com.example.taskpro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiaryFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton addDiaryButton;
    private DatabaseReference diaryRef;
    private ValueEventListener diaryValueEventListener;
    private List<Diary> diaryList; // Sử dụng Diary thay vì DiaryEntry
    private RecyclerView recyclerView;
    private DiaryAdapter diaryAdapter;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : "";

        // Tạo tham chiếu đến nút diary trong cơ sở dữ liệu Firebase
        diaryRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("diaries");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        addDiaryButton = view.findViewById(R.id.addDiaryButton);
        addDiaryButton.setOnClickListener(this);

        // Khởi tạo RecyclerView và Adapter của nó
        recyclerView = view.findViewById(R.id.recyclerView);
        diaryList = new ArrayList<>();
        diaryAdapter = new DiaryAdapter(diaryList, diaryRef, getContext());

        // Thiết lập adapter cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(diaryAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Khởi tạo ValueEventListener để lắng nghe các thay đổi trong nút diary
        diaryValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                diaryList.clear();
                for (DataSnapshot diarySnapshot : dataSnapshot.getChildren()) {
                    Diary diaryEntry = diarySnapshot.getValue(Diary.class); // Sử dụng Diary
                    if (diaryEntry != null) {
                        diaryEntry.setKey(diarySnapshot.getKey()); // Thiết lập key cho mỗi nhật ký
                        diaryList.add(diaryEntry);
                    }
                }
                diaryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(getActivity(), R.string.toast_error + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        // Bắt đầu lắng nghe các thay đổi trong nút diary
        diaryRef.addValueEventListener(diaryValueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Dừng lắng nghe các thay đổi trong nút diary
        if (diaryValueEventListener != null) {
            diaryRef.removeEventListener(diaryValueEventListener);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addDiaryButton) {
            // Mở AddDiaryFragment
            AddDiaryFragment addDiaryFragment = new AddDiaryFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, addDiaryFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}