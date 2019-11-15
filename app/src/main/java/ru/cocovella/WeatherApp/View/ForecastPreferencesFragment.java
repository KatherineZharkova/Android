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
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;


public class ForecastPreferencesFragment extends Fragment {
    private Settings settings;
    private EditText city;
    private Button applyButton;
    private RadioGroup citiesRG;
    private CheckBox humidityCB;
    private CheckBox windCB;
    private CheckBox barometerCB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast_preferences, container, false);
            settings = Settings.getInstance();
            initViews(view);
            inflateViews();
            setRadioListener();
            setApplyButton();
        return view;
    }

    private void initViews(View v) {
        city = v.findViewById(R.id.cityInputBox);
        applyButton = v.findViewById(R.id.applyButton);
        citiesRG = v.findViewById(R.id.citiesRadioGroup);
            v.findViewById(R.id.radioButton1);
            v.findViewById(R.id.radioButton2);
            v.findViewById(R.id.radioButton3);
            v.findViewById(R.id.radioButton4);
            v.findViewById(R.id.radioButton5);
        humidityCB = v.findViewById(R.id.humidityCB);
        windCB  = v.findViewById(R.id.windCB);
        barometerCB = v.findViewById(R.id.barometerCB);
    }

    private void inflateViews() {
        city.setText(settings.getCity());
        citiesRG.check(settings.getRadioCityID());
        humidityCB.setChecked(settings.isHumidityCB());
        windCB.setChecked(settings.isWindCB());
        barometerCB.setChecked(settings.isBarometerCB());
    }

    private void setRadioListener() {
        citiesRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioCity = Objects.requireNonNull(getView()).findViewById(checkedId);
                city.setText(radioCity.getText().toString());
            }
        });
    }

    private void setApplyButton() {
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
                FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                transaction.replace(R.id.container, new ForecastFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void updateSettings() {
        settings.setCity(city.getText().toString());
        settings.setHumidityCB(humidityCB.isChecked());
        settings.setWindCB(windCB.isChecked());
        settings.setBarometerCB(barometerCB.isChecked());
        settings.setRadioCityID(citiesRG.getCheckedRadioButtonId());
    }
}
