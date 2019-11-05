package ru.cocovella.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ChooseCityActivity extends AppCompatActivity {
    SharedPreferences settings;
    EditText city;
    Button goButton;
    TextView city_1;
    TextView city_2;
    TextView city_3;
    TextView city_4;
    TextView city_5;
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

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("city", city.getText().toString());
                intent.putExtra("humidity", humidityCB.isChecked());
                intent.putExtra("wind", windCB.isChecked());
                intent.putExtra("barometer", barometerCB.isChecked());
                startActivity(intent);
            }
        });

    }

    private void initViews() {
        city = findViewById(R.id.cityInputBox);
        city.setText(settings.getString("city", "Atlantis"));
        goButton = findViewById(R.id.goButton);

        city_1 = findViewById(R.id.City1);
        city_2 = findViewById(R.id.City2);
        city_3 = findViewById(R.id.City3);
        city_4 = findViewById(R.id.City4);
        city_5 = findViewById(R.id.City5);

        humidityCB = findViewById(R.id.humidityCB);
        humidityCB.setChecked(settings.getBoolean("humidity", true));
        windCB  = findViewById(R.id.windCB);
        windCB.setChecked(settings.getBoolean("wind", true));
        barometerCB = findViewById(R.id.barometerCB);
        barometerCB.setChecked(settings.getBoolean("barometer", true));

    }

}
