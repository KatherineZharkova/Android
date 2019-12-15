package ru.cocovella.WeatherApp.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import ru.cocovella.WeatherApp.R;

import static ru.cocovella.WeatherApp.Model.Keys.BACKGROUND;
import static ru.cocovella.WeatherApp.Model.Keys.SHARED_PREFS;
import static ru.cocovella.WeatherApp.Model.Keys.THEME_ID;


public class SettingsFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int currentTheme;
    private TextInputEditText urlInput;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        urlInput = view.findViewById(R.id.urlInput);
        setThemeSwitch(view);
        setBackgroundButton(view);
        return view;
    }

    private void setThemeSwitch(View view) {
        Switch themeSwitch = view.findViewById(R.id.themeSwitch);
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        currentTheme = sharedPreferences.getInt(THEME_ID, R.style.ColdTheme);

        themeSwitch.setChecked(currentTheme == R.style.ColdTheme);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putInt(THEME_ID, isChecked ? R.style.ColdTheme : R.style.AppTheme);
            editor.apply();
            if (currentTheme != sharedPreferences.getInt(THEME_ID, -1)) { Objects.requireNonNull(getActivity()).recreate(); }
        });
    }

    private void setBackgroundButton(View view) {
        view.findViewById(R.id.setBackgroundButton).setOnClickListener(v -> {
            String path;
            String pathInput = Objects.requireNonNull(urlInput.getText()).toString();
            if (pathInput.isEmpty()) {
                path = "https://img2.goodfon.ru/wallpaper/nbig/a/3c/more-zakat-oblaka-3277.jpg";
            } else {
                path = pathInput;
            }
            editor.putString(BACKGROUND, path);
            editor.apply();
            Objects.requireNonNull(getActivity()).recreate();
        });
    }
}
