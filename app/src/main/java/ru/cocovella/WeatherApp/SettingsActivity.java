package ru.cocovella.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences settings;
    Button exitButton;
    Switch nightModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        setTheme(settings.getInt("THEME", R.style.AppTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nightModeSwitch = findViewById(R.id.themeSwitch);
        nightModeSwitch.setChecked(settings.getBoolean("ThemeSwitch", false));
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("THEME", isChecked ? R.style.ColdTheme : R.style.AppTheme);
                editor.putBoolean("ThemeSwitch", isChecked);
                editor.apply();
                recreate();
            }
        });

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        exitButton.performClick();
    }
}
