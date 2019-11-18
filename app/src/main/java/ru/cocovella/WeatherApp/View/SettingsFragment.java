package ru.cocovella.WeatherApp.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import java.util.Objects;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.Model.Tags;
import ru.cocovella.WeatherApp.R;


public class SettingsFragment extends Fragment implements Tags, View.OnClickListener {
    private Settings settings = Settings.getInstance();
    private int currentTheme  = settings.getThemeID();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setThemeSwitch();
        setApplyButton();
    }

    private void setThemeSwitch() {
        Switch themeSwitch = Objects.requireNonNull(getView()).findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(settings.getThemeID() == R.style.ColdTheme);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                settings.setThemeID(isChecked ? R.style.ColdTheme : R.style.AppTheme));
    }

    private void setApplyButton() {
        Objects.requireNonNull(getView()).findViewById(R.id.applyButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (currentTheme != settings.getThemeID()) {
            Objects.requireNonNull(getActivity()).recreate();
        }
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }
}
