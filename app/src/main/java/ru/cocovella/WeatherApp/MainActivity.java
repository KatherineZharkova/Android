package ru.cocovella.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView cityName;
    Button chooseCityButton;
    ImageButton settingsButton;
    TextView humidityTV;
    TextView humidityInfo;
    TextView windTV;
    TextView windInfo;
    TextView barometerTV;
    TextView barometerInfo;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        setTheme(settings.getInt("THEME", R.style.AppTheme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        onChooseCityButtonClick();
        onSettingsButtonClick();
    }

    private void initViews() {
        cityName = findViewById(R.id.cityName);
        cityName.setText(getCityName());

        chooseCityButton = findViewById(R.id.chooseCityButton);
        settingsButton = findViewById(R.id.settingsButton);

        humidityTV = findViewById(R.id.humidityTV);
        humidityInfo = findViewById(R.id.humidityInfo);
        setExtraInfo("humidity", humidityTV, humidityInfo);

        windTV = findViewById(R.id.windTV);
        windInfo = findViewById(R.id.windInfo);
        setExtraInfo("wind", windTV, windInfo);

        barometerTV = findViewById(R.id.barometerTV);
        barometerInfo = findViewById(R.id.barometerInfo);
        setExtraInfo("barometer", barometerTV, barometerInfo);
    }

    private void onChooseCityButtonClick() {
        chooseCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ChooseCityActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onSettingsButtonClick() {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getCityName() {
        String cityFromIntent = getIntent().getStringExtra("city");

        if (cityFromIntent != null) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("city", cityFromIntent);
            editor.putBoolean("humidity", getIntent().getBooleanExtra("humidity", false));
            editor.putBoolean("wind", getIntent().getBooleanExtra("wind", false));
            editor.putBoolean("barometer", getIntent().getBooleanExtra("barometer", false));
            editor.putInt("radioCity", getIntent().getIntExtra("radioCity", -1));
            editor.apply();
            return cityFromIntent;
        } else {
            return settings.getString("city", "Atlantis");
        }
    }

    private void setExtraInfo(String key, TextView keyTV, TextView keyInfo) {
        if (!settings.getBoolean(key, true)) {
            keyTV.setVisibility(View.GONE);
            keyInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        // отключила эту функцию, чтобы MainActivity всегда была Main, а остальные - "диалоговыми" окнами
    }

}
