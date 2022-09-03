package com.puulapp.wsumap.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.puulapp.wsumap.R;

public class SettingFragment extends Fragment {

    private SwitchCompat switch_update, switch_vibrate, switch_sound, switch_not;
    private Button button_reset;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        initialize(view);

        action();

        //contains notification vibrate, sound, auto update
        return view;
    }

    private void action() {

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit = prefs.edit();
                edit.putBoolean("notification", Boolean.TRUE);
                switch_not.setChecked(true);
                edit.putBoolean("vibration", Boolean.TRUE);
                switch_vibrate.setChecked(true);
                edit.putBoolean("sound", Boolean.TRUE);
                switch_sound.setChecked(true);
                edit.putBoolean("update", Boolean.FALSE);
                switch_update.setChecked(false);
                edit.apply();
            }
        });

        switch_update.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit = prefs.edit();
                    edit.putBoolean("update", Boolean.TRUE);
                    switch_update.setChecked(true);
                    edit.apply();
                } else {
                    edit = prefs.edit();
                    edit.putBoolean("update", Boolean.FALSE);
                    switch_update.setChecked(false);
                    edit.apply();
                }
            }
        });
        switch_not.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit = prefs.edit();
                    edit.putBoolean("notification", Boolean.TRUE);
                    switch_not.setChecked(true);
                    edit.apply();
                } else {
                    edit = prefs.edit();
                    edit.putBoolean("notification", Boolean.FALSE);
                    switch_not.setChecked(false);
                    edit.apply();
                }
            }
        });
        switch_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit = prefs.edit();
                    edit.putBoolean("sound", Boolean.TRUE);
                    switch_sound.setChecked(true);
                    edit.apply();
                } else {
                    edit = prefs.edit();
                    edit.putBoolean("sound", Boolean.FALSE);
                    switch_sound.setChecked(false);
                    edit.apply();
                }
            }
        });
        switch_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit = prefs.edit();
                    edit.putBoolean("vibration", Boolean.TRUE);
                    switch_vibrate.setChecked(true);
                    edit.apply();
                } else {
                    edit = prefs.edit();
                    edit.putBoolean("vibration", Boolean.FALSE);
                    switch_vibrate.setChecked(false);
                    edit.apply();
                }
            }
        });

    }

    private void initialize(View view) {
        switch_not = view.findViewById(R.id.switch_not);
        switch_sound = view.findViewById(R.id.switch_sound);
        switch_update = view.findViewById(R.id.switch_update);
        switch_vibrate = view.findViewById(R.id.switch_vibrate);
        button_reset = view.findViewById(R.id.button_reset);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean previouslyStarted = prefs.getBoolean("notification", true);
        switch_not.setChecked(previouslyStarted);
        boolean previouslyStarted1 = prefs.getBoolean("sound", true);
        switch_sound.setChecked(previouslyStarted1);
        boolean previouslyStarted2 = prefs.getBoolean("vibration", true);
        switch_vibrate.setChecked(previouslyStarted2);
        boolean previouslyStarted3 = prefs.getBoolean("update", false);
        switch_update.setChecked(previouslyStarted3);

    }
}