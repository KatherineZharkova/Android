package ru.cocovella.WeatherApp.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.Objects;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;


public class ForecastFragment extends Fragment implements View.OnClickListener {
    private TextView cityName;
    private TextView temperature;
    private TextView description;
    private LinearLayout humidity;
    private TextView humidityInfo;
    private LinearLayout wind;
    private TextView windInfo;
    private LinearLayout barometer;
    private TextView barometerInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
        inflateViews();
    }

    private void initViews() {
        cityName = Objects.requireNonNull(getView()).findViewById(R.id.cityName);
        temperature = getView().findViewById(R.id.temperature);
        description = getView().findViewById(R.id.descriptionTV);
        humidity = getView().findViewById(R.id.humidity);
        humidityInfo = getView().findViewById(R.id.humidityInfo);
        wind = getView().findViewById(R.id.wind);
        windInfo = getView().findViewById(R.id.windInfo);
        barometer = getView().findViewById(R.id.barometer);
        barometerInfo = getView().findViewById(R.id.barometerInfo);
        getView().findViewById(R.id.webButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse("https://www.google.ru/search?q=" + Settings.getInstance().getCity() + "+weather+forecast");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void inflateViews() {
        Settings settings = Settings.getInstance();
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

    private void showExtraInfo(boolean isVisible, View item) {
        item.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

}