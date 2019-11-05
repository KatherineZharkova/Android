package ru.cocovella.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    Button saveButton;
    Switch nightModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO : муляж, нужен полноценный образец приложения со сменой темы;
                setTheme(isChecked ? R.style.ColdTheme : R.style.AppTheme);
            }
        });

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
