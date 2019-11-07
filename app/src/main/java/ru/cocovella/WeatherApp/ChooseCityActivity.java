package ru.cocovella.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ChooseCityActivity extends AppCompatActivity {
    SharedPreferences settings;
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
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        setTheme(settings.getInt("THEME", R.style.AppTheme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);

        initViews();
        inflateViews();
        onApplyListener();

        makeLog();
    }

    private void initViews() {
        city = findViewById(R.id.cityInputBox);
        applyButton = findViewById(R.id.weatherSettingsButton);
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
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("radioCity", citiesRG.getCheckedRadioButtonId());
                intent.putExtra("city", city.getText().toString());
                intent.putExtra("humidity", humidityCB.isChecked());
                intent.putExtra("wind", windCB.isChecked());
                intent.putExtra("barometer", barometerCB.isChecked());
                startActivity(intent);
            }
        });
    }

    private void makeLog() {
        Log.d(getLocalClassName(), Thread.currentThread().getStackTrace()[3].getMethodName());
        String message = getLocalClassName() + " >> " + Thread.currentThread().getStackTrace()[3].getMethodName();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        makeLog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        makeLog();
    }

    @Override
    protected void onDestroy() {
        makeLog();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        makeLog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeLog();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        makeLog();
    }

}
