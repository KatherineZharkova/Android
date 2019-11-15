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


public class SettingsFragment extends Fragment implements Tags {
    private Settings settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
            settings = Settings.getInstance();
            setThemeSwitch(view);
            setApplyButton(view);
        return view;
    }

    private void setThemeSwitch(View v) {
        Switch themeSwitch = v.findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(settings.getThemeID() == R.style.ColdTheme);
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setThemeID(isChecked ? R.style.ColdTheme : R.style.AppTheme);
                Objects.requireNonNull(getActivity()).recreate();
            }
        });
    }

    private void setApplyButton(View v) {
        v.findViewById(R.id.applyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                transaction.add(R.id.container, new ForecastFragment())
                        .remove(Objects.requireNonNull(getFragmentManager().findFragmentById(R.id.container)))
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


}
