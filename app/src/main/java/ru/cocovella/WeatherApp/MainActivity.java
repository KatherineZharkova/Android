package ru.cocovella.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import ru.cocovella.WeatherApp.Model.Settings;

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
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = Settings.getInstance();
        setTheme(settings.getThemeID());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        onChooseCityButtonClick();
        onSettingsButtonClick();
    }

    private void initViews() {
        cityName = findViewById(R.id.cityName);
        cityName.setText(settings.getCity());

        chooseCityButton = findViewById(R.id.chooseCityButton);
        settingsButton = findViewById(R.id.settingsButton);

        humidityTV = findViewById(R.id.humidityTV);
        humidityInfo = findViewById(R.id.humidityInfo);
        showExtraInfo(settings.isHumidityCB(), humidityTV, humidityInfo);

        windTV = findViewById(R.id.windTV);
        windInfo = findViewById(R.id.windInfo);
        showExtraInfo(settings.isWindCB(), windTV, windInfo);

        barometerTV = findViewById(R.id.barometerTV);
        barometerInfo = findViewById(R.id.barometerInfo);
        showExtraInfo(settings.isBarometerCB(), barometerTV, barometerInfo);

    }

    private void showExtraInfo(boolean isVisible, TextView title, TextView info) {
        if (!isVisible) {
            title.setVisibility(View.GONE);
            info.setVisibility(View.GONE);
        }
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

}
