package ru.cocovella.WeatherApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class ChooseCityActivity extends AppCompatActivity {
    SharedPreferences settings;
    EditText city;
    Button exitButton;
    RadioGroup citiesRG;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioButton radioButton5;
    CheckBox humidityCB;
    CheckBox windCB;
    CheckBox barometerCB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        setTheme(settings.getInt("THEME", R.style.AppTheme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);

        initViews();
        inflateViews();
        onApplyListener();
    }

    private void initViews() {
        city = findViewById(R.id.cityInputBox);
        exitButton = findViewById(R.id.exitButton);
        citiesRG = findViewById(R.id.citiesRadioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton5 = findViewById(R.id.radioButton5);
        humidityCB = findViewById(R.id.humidityCB);
        windCB  = findViewById(R.id.windCB);
        barometerCB = findViewById(R.id.barometerCB);
    }

    private void inflateViews() {
        city.setText(settings.getString("city", "Moscow"));

        citiesRG.check(settings.getInt("radioCity", R.id.radioButton1));
        citiesRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton tmp = findViewById(checkedId);
                city.setText(tmp.getText().toString());
            }
        });

        humidityCB.setChecked(settings.getBoolean("humidity", true));
        windCB.setChecked(settings.getBoolean("wind", true));
        barometerCB.setChecked(settings.getBoolean("barometer", true));
    }

    private void onApplyListener() {
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void updateSettings() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("city", city.getText().toString());
        editor.putBoolean("humidity", humidityCB.isChecked());
        editor.putBoolean("wind", windCB.isChecked());
        editor.putBoolean("barometer", barometerCB.isChecked());
        editor.putInt("radioCity", citiesRG.getCheckedRadioButtonId());
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city", city.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        city.setText(savedInstanceState.getString("city"));
    }

}
