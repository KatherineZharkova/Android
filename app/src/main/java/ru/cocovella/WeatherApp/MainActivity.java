package ru.cocovella.WeatherApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.Model.Tags;

public class MainActivity extends AppCompatActivity  implements Tags {
    TextView cityName;
    TextView temperature;
    TextView description;
    Button weatherSetButton;
    Button weatherBrowserButton;
    Button appSetButton;
    LinearLayout humidity;
    TextView humidityTV;
    TextView humidityInfo;
    LinearLayout wind;
    TextView windTV;
    TextView windInfo;
    LinearLayout barometer;
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
        setWeatherSettingsButton();
        setInfoButton();
        setAppSettingsButton();
    }

    private void initViews() {
        cityName = findViewById(R.id.cityName);
        temperature = findViewById(R.id.temperature);
        description = findViewById(R.id.descriptionTV);
        weatherSetButton = findViewById(R.id.findCityButton);
        weatherBrowserButton = findViewById(R.id.infoButton);
        appSetButton = findViewById(R.id.settingsButton);
        humidity = findViewById(R.id.humidity);
        humidityTV = findViewById(R.id.humidityTV);
        humidityInfo = findViewById(R.id.humidityInfo);
        wind = findViewById(R.id.wind);
        windTV = findViewById(R.id.windTV);
        windInfo = findViewById(R.id.windInfo);
        barometer = findViewById(R.id.barometer);
        barometerTV = findViewById(R.id.barometerTV);
        barometerInfo = findViewById(R.id.barometerInfo);
    }

    private void inflateViews() {
        if (getIntent().getStringExtra(CITY_KEY) == null) {
            if (settings.getCity() == null) {
                cityName.setText(getString(R.string.welcome));
            } else {
                cityName.setText(settings.getCity());
                temperature.setText(settings.getTemperature());
                description.setText(settings.getDescription());
                humidityInfo.setText(settings.getHumidity());
                windInfo.setText(settings.getWind());
                barometerInfo.setText(settings.getBarometer());
                showExtraInfo(settings.isHumidityCB(), humidity);
                showExtraInfo(settings.isWindCB(), wind);
                showExtraInfo(settings.isBarometerCB(), barometer);
            }
        }
    }

    private void showExtraInfo(boolean isVisible, LinearLayout item) {
        item.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void setWeatherSettingsButton() {
        weatherSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherSettingsActivity.class);
                startActivityForResult(intent, REQ_CODE_WEATHER_SETTINGS);
            }
        });
    }

    private void setInfoButton() {
        weatherBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.google.ru/search?q=" + settings.getCity() + "+weather+forecast");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    private void setAppSettingsButton() {
        appSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppSettingsActivity.class);
                startActivityForResult(intent, REQ_CODE_APP_SETTINGS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_APP_SETTINGS) {
            if (resultCode == RESULT_OK) {
                recreate();
            } else if (resultCode == RESULT_CANCELED) {
                settings.setThemeID(settings.getThemeID() == R.style.AppTheme ?
                        R.style.ColdTheme : R.style.AppTheme);
            }
        }

        if (requestCode == REQ_CODE_WEATHER_SETTINGS) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    cityName.setText(data.getStringExtra(CITY_KEY));
                    temperature.setText(settings.getTemperature());
                    description.setText(settings.getDescription());
                    humidityInfo.setText(settings.getHumidity());
                    windInfo.setText(settings.getWind());
                    barometerInfo.setText(settings.getBarometer());
                    showExtraInfo(data.getBooleanExtra(HUMIDITY_KEY, false), humidity);
                    showExtraInfo(data.getBooleanExtra(WIND_KEY, false), wind);
                    showExtraInfo(data.getBooleanExtra(BAROMETER_KEY, false), barometer);
                }
            } else if (resultCode == RESULT_ERROR){
                cityName.setText(R.string.error_city_not_found);
                temperature.setText("");
                description.setText("");
                humidity.setVisibility(View.GONE);
                wind.setVisibility(View.GONE);
                barometer.setVisibility(View.GONE);
            }
        }
    }
}
