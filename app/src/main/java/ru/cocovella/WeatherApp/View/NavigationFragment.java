package ru.cocovella.WeatherApp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;


public class NavigationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
            setTheme(view);
            Button preferencesButton = view.findViewById(R.id.preferencesButton);
            Button homeButton = view.findViewById(R.id.homeButton);
            Button settingsButton = view.findViewById(R.id.settingsButton);
            setButton(preferencesButton);
            setButton(homeButton);
            setButton(settingsButton);
        return view;
    }

    private void setTheme(View view) {
        view.setBackgroundColor(
                getResources().getColor(Settings.getInstance().getThemeID() == R.style.AppTheme ?
                        R.color.colorPrimary : R.color.colorPrimaryCold));
    }

    private void setButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ForecastFragment();
                switch (v.getId()) {
                    case R.id.preferencesButton : fragment = new ForecastPreferencesFragment(); break;
                    case R.id.settingsButton : fragment = new SettingsFragment();
                }

                FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                transaction.replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


    }

}
