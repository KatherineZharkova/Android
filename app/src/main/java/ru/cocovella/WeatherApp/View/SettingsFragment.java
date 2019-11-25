package ru.cocovella.WeatherApp.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import java.util.Objects;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;


public class SettingsFragment extends Fragment {
    private Settings settings = Settings.getInstance();
    private int currentTheme  = settings.getThemeID();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setThemeSwitch(view);
        return view;
    }

    private void setThemeSwitch(View view) {
        Switch themeSwitch = view.findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(settings.getThemeID() == R.style.ColdTheme);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settings.setThemeID(isChecked ? R.style.ColdTheme : R.style.AppTheme);
            if (currentTheme != settings.getThemeID()) { Objects.requireNonNull(getActivity()).recreate(); }
        });
    }

}
