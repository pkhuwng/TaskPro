package com.example.taskpro;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Patterns;

import com.example.taskpro.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//implements OnMapReadyCallback
public class ContactFragment extends Fragment  {

    //private MapView mapView;
    //private GoogleMap mMap;
    private DatabaseReference usersRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        // Initialize the MapView
        /*mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);*/

        // Initialize the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        // Retrieve references to the views
        final EditText messageEditText = view.findViewById(R.id.message);
        final EditText emailAddressEditText = view.findViewById(R.id.editTextTextEmailAddress);
        Button sendMessageButton = view.findViewById(R.id.sendMessage);

        // Set click listener for the send message button
        sendMessageButton.setOnClickListener(v -> {
            // Retrieve the values entered by the user
            String message = messageEditText.getText().toString().trim();
            String emailAddress = emailAddressEditText.getText().toString().trim();

            // Check if any of the EditText fields is empty
            if (message.isEmpty() || emailAddress.isEmpty()) {
                showToast(getString(R.string.toast_please_fill_in_all_fields));
                return;
            }

            // Validate the email address
            if (!isValidEmail(emailAddress)) {
                showToast(getString(R.string.toast_invalid_email_address));
                return;
            }

            // Generate the current datetime
            String datetime = getCurrentDateTime();

            // Assuming you have implemented Firebase Authentication and the user is authenticated
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef = usersRef.child(userId).child("feedback");
            DatabaseReference feedbackRef = userRef.child(datetime);
            feedbackRef.child("message").setValue(message, (error, ref) -> {
                if (error == null) {
                    // Message saved successfully
                    showToast(getString(R.string.toast_message_sent_successfully));
                    // Clear the EditText views
                    messageEditText.setText("");
                    emailAddressEditText.setText("");
                } else {
                    // Error occurred while saving data
                    showToast(getString(R.string.toast_error) + error.getMessage());
                }
            });
            feedbackRef.child("emailAddress").setValue(emailAddress);
        });

        // Set focus change listener for the email address field
        emailAddressEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // Check if the email address is valid
                String emailAddress = emailAddressEditText.getText().toString().trim();
                if (!isValidEmail(emailAddress)) {
                    emailAddressEditText.setError(getString(R.string.toast_invalid_email_address));
                } else {
                    emailAddressEditText.setError(null);
                }
            }
        });

        return view;
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker and move the camera to a specific location
        LatLng location = new LatLng(-6.814018774740469, 39.28006551534149); // Replace with desired latitude and longitude
        float zoomLevel = 12.0f; // Replace with desired zoom level
        mMap.addMarker(new MarkerOptions().position(location).title("Dar Es Salaam Institute of Technology"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));

        // Set the marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Zoom in when marker is clicked
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17.0f));
                return true;
            }
        });
    }*/
}