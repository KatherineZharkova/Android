package ru.cocovella.WeatherApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import ru.cocovella.WeatherApp.Model.Settings;


public class ChooseCityActivity extends AppCompatActivity {
    Settings settings;
    EditText city;
    Button applyButton;
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
        settings = Settings.getInstance();
        setTheme(settings.getThemeID());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);

        initViews();
        inflateViews();
        radioGroupListener();
        applyButtonListener();
    }

    private void initViews() {
        city = findViewById(R.id.cityInputBox);
        applyButton = findViewById(R.id.exitButton);
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
        city.setText(settings.getCity());
        citiesRG.check(settings.getRadioCityID());
        humidityCB.setChecked(settings.isHumidityCB());
        windCB.setChecked(settings.isWindCB());
        barometerCB.setChecked(settings.isBarometerCB());
    }

    private void radioGroupListener() {
        citiesRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioCity = findViewById(checkedId);
                city.setText(radioCity.getText().toString());
            }
        });
    }

    private void applyButtonListener() {
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finishAffinity();
            }
        });
    }

    private void updateSettings() {
        settings.setCity(city.getText().toString());
        settings.setHumidityCB(humidityCB.isChecked());
        settings.setWindCB(windCB.isChecked());
        settings.setBarometerCB(barometerCB.isChecked());
        settings.setRadioCityID(citiesRG.getCheckedRadioButtonId());
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
