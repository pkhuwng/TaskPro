package com.example.taskpro; // Replace with your package name

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailEditActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword, editTextVerificationCode;
    private Button saveEmailButton, cancelEmailButton;
    private LinearLayout verificationCodeLayout;
    private TextView currentEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email); // Make sure you have activity_email_edit.xml

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        saveEmailButton = findViewById(R.id.saveEmailButton);
        cancelEmailButton = findViewById(R.id.cancelEmailButton);
        currentEmailTextView = findViewById(R.id.textView32);

        if (currentUser != null) {
            currentEmailTextView.setText(currentUser.getEmail());
        }

        saveEmailButton.setOnClickListener(v -> updateEmail());
        cancelEmailButton.setOnClickListener(v -> finish());
    }


    private void updateEmail() {
        String newEmail = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (newEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.toast_please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        String email  = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(this, task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(EmailEditActivity.this, R.string.toast_verification_email_sent_please_check_your_email_to_complete_the_update, Toast.LENGTH_LONG).show();
                        finish(); // Close the activity
                    } else {
                        Log.e("EmailUpdate", "verifyBeforeUpdateEmail:failure", task1.getException());
                        Toast.makeText(EmailEditActivity.this, R.string.toast_failed_to_send_verification_email_please_try_again, Toast.LENGTH_LONG).show();}});
                    } else {
                        Log.e("EmailUpdate", "reauthenticate:failure", task.getException());
                        Toast.makeText(EmailEditActivity.this, R.string.toast_incorrect_password_please_try_again, Toast.LENGTH_SHORT).show();
                    }
        });
    }

}