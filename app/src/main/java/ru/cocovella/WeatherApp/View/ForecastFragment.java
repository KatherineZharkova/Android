package ru.cocovella.WeatherApp.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ru.cocovella.WeatherApp.Model.Keys;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;


public class ForecastFragment extends Fragment implements Keys {
    private TextView cityName;
    private TextView icon;
    private TextView temperature;
    private TextView description;
    private LinearLayout humidity;
    private TextView humidityInfo;
    private LinearLayout wind;
    private TextView windInfo;
    private LinearLayout barometer;
    private TextView barometerInfo;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private Settings settings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
        setWebButton();
        inflateViews();
    }

    private void initViews() {
        cityName = Objects.requireNonNull(getView()).findViewById(R.id.cityName);
        icon = getView().findViewById(R.id.icon);
        temperature = getView().findViewById(R.id.temperature);
        description = getView().findViewById(R.id.descriptionTV);
        humidity = getView().findViewById(R.id.humidity);
        humidityInfo = getView().findViewById(R.id.humidityInfo);
        wind = getView().findViewById(R.id.wind);
        windInfo = getView().findViewById(R.id.windInfo);
        barometer = getView().findViewById(R.id.barometer);
        barometerInfo = getView().findViewById(R.id.barometerInfo);
        recyclerView = getView().findViewById(R.id.week_forecast);
    }

    private void setWebButton() {
        Objects.requireNonNull(getView()).findViewById(R.id.webButton).setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.google.ru/search?q=" + Settings.getInstance().getCity() + "+weather+forecast");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });
    }

    private void inflateViews() {
        settings = Settings.getInstance();
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CITY_KEY, settings.getCity());
        editor.apply();

        cityName.setText(settings.getCity());
        icon.setText(settings.getIcon());
        String tmp = (int)settings.getTemperature() + "°C";
        temperature.setText(tmp);
        description.setText(settings.getDescription());
        String h = settings.getHumidity() + "";
        humidityInfo.setText(h);
        String w = (int)settings.getWind() + " ";
        windInfo.setText(w);
        String b = settings.getBarometer() + " ";
        barometerInfo.setText(b);
        showExtraInfo(sharedPreferences.getBoolean(HUMIDITY_KEY, false), humidity);
        showExtraInfo(sharedPreferences.getBoolean(WIND_KEY, false), wind);
        showExtraInfo(sharedPreferences.getBoolean(BAROMETER_KEY, false), barometer);
        setRecyclerView();
    }

    private void showExtraInfo(boolean isVisible, View item) {
        item.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(sharedPreferences.getBoolean(PERIOD, false) ?
                new ForecastCardAdapter(settings.getHoursForecasts()) : new ForecastCardAdapter(settings.getDaysForecasts()));
    }

}
