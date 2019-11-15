package ru.cocovella.WeatherApp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import java.util.Objects;

import ru.cocovella.WeatherApp.MainActivity;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;


public class NavigationFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setTheme();
        initViews();
    }

    private void setTheme() {
        Objects.requireNonNull(getView()).setBackgroundColor(
                getResources().getColor(Settings.getInstance().getThemeID() == R.style.AppTheme ?
                        R.color.colorPrimary : R.color.colorPrimaryCold));
    }

    private void initViews() {
        assert getView() != null;
        getView().findViewById(R.id.preferencesButton).setOnClickListener(this);
        getView().findViewById(R.id.homeButton).setOnClickListener(this);
        getView().findViewById(R.id.settingsButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new ForecastFragment();
        switch (v.getId()) {
            case R.id.preferencesButton : fragment = new ForecastPreferencesFragment(); break;
            case R.id.settingsButton : fragment = new SettingsFragment();
        }
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
    }
}
