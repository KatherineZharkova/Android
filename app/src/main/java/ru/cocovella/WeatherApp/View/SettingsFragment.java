package ru.cocovella.WeatherApp.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import java.util.Objects;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.Model.Tags;
import ru.cocovella.WeatherApp.R;


public class SettingsFragment extends Fragment implements Tags, View.OnClickListener {
    private Settings settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        settings = Settings.getInstance();
        setThemeSwitch();
        setApplyButton();
    }

    private void setThemeSwitch() {
        Switch themeSwitch = Objects.requireNonNull(getView()).findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(settings.getThemeID() == R.style.ColdTheme);
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setThemeID(isChecked ? R.style.ColdTheme : R.style.AppTheme);
                Objects.requireNonNull(getActivity()).recreate();
            }
        });
    }

    private void setApplyButton() {
        Objects.requireNonNull(getView()).findViewById(R.id.applyButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.container, new ForecastFragment()).commit();
    }
}
