package ru.cocovella.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String TAG = "MAIN_ACTIVITY ";
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

        updateSettings();
        initViews();
        onChooseCityButtonClick();
        onSettingsButtonClick();

        makeLog();
    }

    private void updateSettings() {
        if (getIntent().getStringExtra("city") != null) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("city", getIntent().getStringExtra("city"));
            editor.putBoolean("humidity", getIntent().getBooleanExtra("humidity", false));
            editor.putBoolean("wind", getIntent().getBooleanExtra("wind", false));
            editor.putBoolean("barometer", getIntent().getBooleanExtra("barometer", false));
            editor.putInt("radioCity", getIntent().getIntExtra("radioCity", 0));
            editor.apply();
        }
    }

    private void initViews() {
        cityName = findViewById(R.id.cityName);
        cityName.setText(getCityName());

        chooseCityButton = findViewById(R.id.chooseCityButton);
        settingsButton = findViewById(R.id.settingsButton);

        humidityTV = findViewById(R.id.humidityTV);
        humidityInfo = findViewById(R.id.humidityInfo);
        showExtraInfo("humidity", humidityTV, humidityInfo);

        windTV = findViewById(R.id.windTV);
        windInfo = findViewById(R.id.windInfo);
        showExtraInfo("wind", windTV, windInfo);

        barometerTV = findViewById(R.id.barometerTV);
        barometerInfo = findViewById(R.id.barometerInfo);
        showExtraInfo("barometer", barometerTV, barometerInfo);
    }

    private String getCityName() {
        String cityFromIntent = getIntent().getStringExtra("city");
        String cityFromPrefs = settings.getString("city", "Moscow");
        return cityFromIntent != null ?  cityFromIntent : cityFromPrefs;
    }

    private void showExtraInfo(String key, TextView keyTV, TextView keyInfo) {
        if (!settings.getBoolean(key, true)) {
            keyTV.setVisibility(View.GONE);
            keyInfo.setVisibility(View.GONE);
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




    private void makeLog() {
        Log.d(TAG, Thread.currentThread().getStackTrace()[3].getMethodName());
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

    @Override
    public void onBackPressed() {
        // отключила эту функцию, чтобы MainActivity всегда была Main, а остальные - "диалоговыми" окнами
        makeLog();
    }

}
