package com.example.taskpro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordDialogFragment extends DialogFragment {
    private PasswordResetHelper passwordResetHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordResetHelper = new PasswordResetHelper();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.dialog_change_password)
                .setMessage(R.string.label_do_you_want_to_change_your_password)
                .setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Get the current user's email
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userEmail = user.getEmail();

                        if (userEmail != null) {
                            // Call the password reset method with the user's email
                            passwordResetHelper.resetPassword(userEmail, requireActivity());
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the dialog when the user selects "No"
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
