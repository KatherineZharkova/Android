package ru.cocovella.WeatherApp.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;
import java.util.regex.Pattern;
import ru.cocovella.WeatherApp.Model.ForecastLoader;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;


public class ForecastPreferencesFragment extends Fragment {
    private Settings settings;
    private TextInputEditText city;
    private RecyclerView recyclerView;
    private CheckBox humidityCB;
    private CheckBox windCB;
    private CheckBox barometerCB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setRetainInstance(true);
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

    private void inflateViews() {
        settings = Settings.getInstance();
        city.setText(settings.getCity());
        humidityCB.setChecked(settings.isHumidityCB());
        windCB.setChecked(settings.isWindCB());
        barometerCB.setChecked(settings.isBarometerCB());
        setSpellingCheck();
        setRecyclerView();
        setApplyButton();
    }

    private void setSpellingCheck() {
        city.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus || Objects.requireNonNull(city.getText()).toString().isEmpty()) return;
            Pattern pattern = Pattern.compile(getString(R.string.checkCityNameRegex));
            String value = Objects.requireNonNull(city.getText()).toString();
            String message = getString(R.string.InputErrorMessage);
            city.setError(pattern.matcher(value).matches() ? null : message);
        });
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
                    updateSettings();
                    new ForecastLoader().request();
        });
    }

    private void updateSettings() {
        settings.setCity(Objects.requireNonNull(city.getText()).toString());
        settings.setHumidityCB(humidityCB.isChecked());
        settings.setWindCB(windCB.isChecked());
        settings.setBarometerCB(barometerCB.isChecked());
    }

}
