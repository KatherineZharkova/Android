package ru.cocovella.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ChooseCityActivity extends AppCompatActivity {

    EditText city;
    Button okButton;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);
        initViews();

        okButton.setOnClickListener(new View.OnClickListener() {
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
        okButton = findViewById(R.id.saveButton);
        city_1 = findViewById(R.id.City1);
        city_2 = findViewById(R.id.City2);
        city_3 = findViewById(R.id.City3);
        city_4 = findViewById(R.id.City4);
        city_5 = findViewById(R.id.City5);
        humidityCB = findViewById(R.id.humidityCB);
        windCB  = findViewById(R.id.windCB);
        barometerCB = findViewById(R.id.barometerCB);
    }

}
