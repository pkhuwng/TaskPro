package com.example.taskpro;

import static androidx.core.app.ActivityCompat.recreate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        LinearLayout accountButton = rootView.findViewById(R.id.accountButton);
        LinearLayout privacyButton = rootView.findViewById(R.id.privacyButton);
        LinearLayout helpButton = rootView.findViewById(R.id.helpButton);
        LinearLayout aboutButton = rootView.findViewById(R.id.aboutButton);

        LinearLayout languageButton = rootView.findViewById(R.id.languageButton);

        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog();
            }
        });

        SwitchCompat darkModeSwitch = rootView.findViewById(R.id.switch1);
        int nightModeFlags = rootView.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        darkModeSwitch.setChecked(nightModeFlags == Configuration.UI_MODE_NIGHT_YES);
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Set the phone to dark mode
                    int nightModeFlags = rootView.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                    if (nightModeFlags != Configuration.UI_MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                         // Optional: Restart the activity for changes to take effect immediately
                        ((Activity)rootView.getContext()).recreate();
                    }
                } else {
                    // Set the phone to light mode
                    int nightModeFlags = rootView.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                    if (nightModeFlags != Configuration.UI_MODE_NIGHT_NO) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        // Optional: Restart the activity for changes to take effect immediately
                        ((Activity)rootView.getContext()).recreate();
                    }
                }
            }
        });

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountFragment fragment = new AccountFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivacyFragment fragment = new PrivacyFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpFragment fragment = new HelpFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutFragment fragment = new AboutFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

    private void showLanguageDialog() {
        // List of languages to choose from
        final String[] languages = {"English", "Tiếng Việt"};
        final String[] languageCodes = {"en", "vi"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Choose Language")
                .setItems(languages, (dialog, which) -> {
                    setLocale(languageCodes[which]);
                    Toast.makeText(requireContext(), getString(R.string.state_language_changed_to) + languages[which], Toast.LENGTH_SHORT).show();
                })
                .create()
                .show();
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Restart the activity to apply changes
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }

}

