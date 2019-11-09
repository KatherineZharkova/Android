package ru.cocovella.WeatherApp;

import androidx.annotation.Nullable;
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
        inflateViews();
        onChooseCityButtonClick();
        onSettingsButtonClick();
    }

    @Override
    protected void onResume() {
        inflateViews();
        super.onResume();
    }

    private void initViews() {
        cityName = findViewById(R.id.cityName);
        chooseCityButton = findViewById(R.id.chooseCityButton);
        settingsButton = findViewById(R.id.settingsButton);
        humidityTV = findViewById(R.id.humidityTV);
        humidityInfo = findViewById(R.id.humidityInfo);
        windTV = findViewById(R.id.windTV);
        windInfo = findViewById(R.id.windInfo);
        barometerTV = findViewById(R.id.barometerTV);
        barometerInfo = findViewById(R.id.barometerInfo);
    }

    private void inflateViews() {
        cityName.setText(settings.getCity());
        showExtraInfo(settings.isHumidityCB(), humidityTV, humidityInfo);
        showExtraInfo(settings.isWindCB(), windTV, windInfo);
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
                Intent intent = new Intent(getApplicationContext(), ChooseCityActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onSettingsButtonClick() {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(intent, 7);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            recreate();
        }
    }
}
