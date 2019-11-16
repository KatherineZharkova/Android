package ru.cocovella.WeatherApp.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.Objects;
import ru.cocovella.WeatherApp.Model.ForecastServer;
import ru.cocovella.WeatherApp.Model.Observer;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;


public class ForecastPreferencesFragment extends Fragment implements Observer {
    private Settings settings;
    private EditText city;
    private Button applyButton;
    private RadioGroup citiesRG;
    private CheckBox humidityCB;
    private CheckBox windCB;
    private CheckBox barometerCB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast_preferences, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        settings = Settings.getInstance();
        initViews();
        inflateViews();
        setRadioListener();
        setApplyButton();
    }

    private void initViews() {
        city = Objects.requireNonNull(getView()).findViewById(R.id.cityInputBox);
        applyButton = getView().findViewById(R.id.applyButton);
        citiesRG = getView().findViewById(R.id.citiesRadioGroup);
        getView().findViewById(R.id.radioButton1);
        getView().findViewById(R.id.radioButton2);
        getView().findViewById(R.id.radioButton3);
        getView().findViewById(R.id.radioButton4);
        getView().findViewById(R.id.radioButton5);
        humidityCB = getView().findViewById(R.id.humidityCB);
        windCB  = getView().findViewById(R.id.windCB);
        barometerCB = getView().findViewById(R.id.barometerCB);
    }

    private void inflateViews() {
        city.setText(settings.getCity());
        citiesRG.check(settings.getRadioCityID());
        humidityCB.setChecked(settings.isHumidityCB());
        windCB.setChecked(settings.isWindCB());
        barometerCB.setChecked(settings.isBarometerCB());
    }

    private void setRadioListener() {
        citiesRG.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioCity = Objects.requireNonNull(getView()).findViewById(checkedId);
            city.setText(radioCity.getText().toString());
        });
    }

    private void setApplyButton() {
        applyButton.setOnClickListener(v -> {
            updateSettings();
            settings.addObserver(this);
            new ForecastServer().request();
        });
    }

    private void updateSettings() {
        settings.setCity(city.getText().toString());
        settings.setHumidityCB(humidityCB.isChecked());
        settings.setWindCB(windCB.isChecked());
        settings.setBarometerCB(barometerCB.isChecked());
        settings.setRadioCityID(citiesRG.getCheckedRadioButtonId());
    }

    @Override
    public void update() {
        settings = Settings.getInstance();
        if (settings.getServerResultCode()) {
            FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
            transaction.replace(R.id.container, new ForecastFragment()).addToBackStack(null).commit();
        } else {
            settings.setMessage(getString(R.string.error_city_not_found));
            FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
            transaction.replace(R.id.container, new MessageFragment()).addToBackStack(null).commit();
        }
        settings.removeObserver(this);
    }
}
