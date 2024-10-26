package com.example.taskpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView forgotPasswordTextView;
    private FirebaseAuth mAuth;
    private PasswordResetHelper passwordResetHelper;
    private boolean isNetworkAvailable = true;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        passwordResetHelper = new PasswordResetHelper();

        editTextEmail = findViewById(R.id.editTextTextEmail);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        forgotPasswordTextView = findViewById(R.id.textView23);
        ImageView imageViewLogin = findViewById(R.id.imageView5);
        TextView registerTextView = findViewById(R.id.textView5);

        imageViewLogin.setOnClickListener(v -> login());

        forgotPasswordTextView.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (email.isEmpty()) {
                editTextEmail.setError("Email is Required");
                editTextEmail.requestFocus();
            } else {
                passwordResetHelper.resetPassword(email, LoginActivity.this);
            }
        });

        registerTextView.setOnClickListener(v -> {
            Intent signUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signUpIntent);
        });

        // Register network change receiver
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister network change receiver
        unregisterReceiver(networkChangeReceiver);
    }

    private void login() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!isNetworkAvailable) {
            editTextEmail.setEnabled(false);
            editTextPassword.setEnabled(false);
            Toast.makeText(LoginActivity.this, R.string.toast_no_internet_connection_please_check_your_network_settings, Toast.LENGTH_LONG).show();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.error_email_is_required));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.error_password_is_required));
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, R.string.state_login_successful, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.state_login_failed_please_check_your_credentials, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isNetworkAvailable()) {
                isNetworkAvailable = true;
                editTextEmail.setEnabled(true);
                editTextPassword.setEnabled(true);
                Toast.makeText(LoginActivity.this, R.string.state_network_connection_available, Toast.LENGTH_SHORT).show();
            } else {
                isNetworkAvailable = false;
                editTextEmail.setEnabled(false);
                editTextPassword.setEnabled(false);
                Toast.makeText(LoginActivity.this, R.string.state_no_internet_connection_please_check_your_network_settings, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
