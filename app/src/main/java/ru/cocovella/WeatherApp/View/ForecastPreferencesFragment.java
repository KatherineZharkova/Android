package ru.cocovella.WeatherApp.View;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;
import java.util.regex.Pattern;
import ru.cocovella.WeatherApp.Model.ForecastServer;
import ru.cocovella.WeatherApp.Model.Observer;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;


public class ForecastPreferencesFragment extends Fragment implements Observer {
    private Settings settings;
    private TextInputEditText city;
    private RecyclerView recyclerView;
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
        initViews();
        inflateViews();
    }

    private void initViews() {
        city = Objects.requireNonNull(getView()).findViewById(R.id.cityInput);
        humidityCB = getView().findViewById(R.id.humidityCB);
        windCB = getView().findViewById(R.id.windCB);
        barometerCB = getView().findViewById(R.id.barometerCB);
        recyclerView = getView().findViewById(R.id.recycler_view);
    }

    private void validate() {
        city.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus || Objects.requireNonNull(city.getText()).toString().isEmpty()) return;
            Pattern pattern = Pattern.compile(getString(R.string.checkCityNameRegex));
            String value = Objects.requireNonNull(city.getText()).toString();
            String message = getString(R.string.InputErrorMessage);
            city.setError(pattern.matcher(value).matches() ? null : message);
        });
    }

    private void inflateViews() {
        settings = Settings.getInstance();
        city.setText(settings.getCity());
        humidityCB.setChecked(settings.isHumidityCB());
        windCB.setChecked(settings.isWindCB());
        barometerCB.setChecked(settings.isBarometerCB());
        setApplyButton();
        setRecyclerView();
        validate();
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        settings.setCitiesChoice(getResources().getStringArray(R.array.cities));
        CitiesListAdapter adapter = new CitiesListAdapter(settings.getCitiesChoice());
        adapter.setOnItemClickListener((itemName, position) -> city.setText(itemName));
        recyclerView.setAdapter(adapter);
    }

    private void setApplyButton() {
        Objects.requireNonNull(getView()).findViewById(R.id.applyButton).setOnClickListener(v -> {
            String prompt = "Request forecast for " + Objects.requireNonNull(city.getText()).toString() + "?";
            Snackbar snackbar = Snackbar.make(getView(), prompt, Snackbar.LENGTH_LONG);
            snackbar.setAction("confirm", v1 -> {
                        updateSettings();
                        settings.addObserver(ForecastPreferencesFragment.this);
                        new ForecastServer().request();
                    })
                    .setActionTextColor(Color.WHITE)
                    .getView().setBackgroundColor(Color.WHITE);
            snackbar.show();
        });
    }

    private void updateSettings() {
        String newCity = Objects.requireNonNull(city.getText()).toString();
        settings.setCity(newCity);
        if(!settings.getCitiesChoice().contains(newCity)) {
            settings.getCitiesChoice().add(newCity);
        }
        settings.setHumidityCB(humidityCB.isChecked());
        settings.setWindCB(windCB.isChecked());
        settings.setBarometerCB(barometerCB.isChecked());
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
