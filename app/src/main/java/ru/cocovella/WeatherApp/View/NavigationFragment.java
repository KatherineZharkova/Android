package ru.cocovella.WeatherApp.View;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import ru.cocovella.WeatherApp.Model.Keys;
import ru.cocovella.WeatherApp.R;


public class NavigationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        initBottomBar(view);
        return view;
    }

    private void initBottomBar(View view) {
        BottomNavigationView bottomBar = view.findViewById(R.id.bottom_bar);
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();

            switch (item.getItemId()) {
                case R.id.preferencesButton:
                    transaction.replace(R.id.container, new ForecastPreferencesFragment()).commit();
                    break;

                case R.id.settingsButton:
                    if (isNewInput()) {
                        Snackbar snackbar = Snackbar.make(view, "Leave without saving?", Snackbar.LENGTH_LONG);
                        snackbar.setAction("confirm", v1 ->
                                transaction.replace(R.id.container, new SettingsFragment()).addToBackStack(null).commit())
                                .setActionTextColor(Color.WHITE).getView().setBackgroundColor(Color.WHITE);
                        snackbar.show();
                    } else { transaction.replace(R.id.container, new SettingsFragment()).commit(); }
                    break;

                case R.id.homeButton:

                    if (isNewInput()) {
                        Snackbar snackbar = Snackbar.make(view, "Leave without saving?", Snackbar.LENGTH_LONG);
                        snackbar.setAction("confirm", v1 ->
                                transaction.replace(R.id.container, new ForecastFragment()).commit())
                                .setActionTextColor(Color.WHITE).getView().setBackgroundColor(Color.WHITE);
                        snackbar.show();
                    } else {
                        String cityNameInput = Objects.requireNonNull(getActivity())
                                .getSharedPreferences(Keys.SHARED_PREFS, Context.MODE_PRIVATE).getString(Keys.CITY_KEY, "");
                        if (cityNameInput.isEmpty()) break;
                        transaction.replace(R.id.container, new ForecastFragment()).commit();
                    }
            }
            return false;
        });

    }

    private boolean isNewInput() {
        if (Objects.requireNonNull(getActivity()).findViewById(R.id.cityInput) != null) {
            TextInputEditText cityInput = Objects.requireNonNull(getActivity()).findViewById(R.id.cityInput);
            String recentInput = Objects.requireNonNull(cityInput.getText()).toString().trim();
            String priorInput = getActivity().getSharedPreferences(Keys.SHARED_PREFS, Context.MODE_PRIVATE).getString(Keys.CITY_KEY, "");
            if (recentInput.isEmpty()) return false;
            return !recentInput.equalsIgnoreCase(priorInput);
        } else {
            return  false;
        }
    }

}
