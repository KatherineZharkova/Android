package ru.cocovella.WeatherApp.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Objects;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.Model.Tags;
import ru.cocovella.WeatherApp.R;


public class ForecastFragment extends Fragment implements Tags {
    private TextView cityName;
    private TextView temperature;
    private TextView description;
    private LinearLayout humidity;
    private TextView humidityInfo;
    private LinearLayout wind;
    private TextView windInfo;
    private LinearLayout barometer;
    private TextView barometerInfo;
    private Settings settings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_forecast, container, false);
            settings = Settings.getInstance();
            initViews(view);
            setInfoButton(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         inflateViews();
    }

    private void initViews(View v) {
        cityName = v.findViewById(R.id.cityName);
        temperature = v.findViewById(R.id.temperature);
        description = v.findViewById(R.id.descriptionTV);
        humidity = v.findViewById(R.id.humidity);
        humidityInfo = v.findViewById(R.id.humidityInfo);
        wind = v.findViewById(R.id.wind);
        windInfo = v.findViewById(R.id.windInfo);
        barometer = v.findViewById(R.id.barometer);
        barometerInfo = v.findViewById(R.id.barometerInfo);
    }

    private void setInfoButton(View v) {
        v.findViewById(R.id.webButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.google.ru/search?q=" + Settings.getInstance().getCity() + "+weather+forecast");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    private void inflateViews() {

        if (requestForecast()) {
            cityName.setText(settings.getCity());
            temperature.setText(settings.getTemperature());
            description.setText(settings.getDescription());
            humidityInfo.setText(settings.getHumidity());
            windInfo.setText(settings.getWind());
            barometerInfo.setText(settings.getBarometer());
            showExtraInfo(settings.isHumidityCB(), humidity);
            showExtraInfo(settings.isWindCB(), wind);
            showExtraInfo(settings.isBarometerCB(), barometer);
        } else {
            FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
            transaction.replace(R.id.container, new MessageFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private boolean requestForecast() {
        // имитация запроса на сервер погоды;
        // если получим код ошибки - return false;
        if (settings.getCity() != null && settings.getCity().length() > 2) {
            settings.setTemperature("17");
            settings.setDescription(getString(R.string.ForecastDescription));
            settings.setHumidity("85");
            settings.setWind("4");
            settings.setBarometer("845");
            return true;
        } else {
            return false;
        }
    }

    private void showExtraInfo(boolean isVisible, LinearLayout item) {
        item.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

}
