package ru.cocovella.WeatherApp.View;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;
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
        initViews(Objects.requireNonNull(getView()));
        setTheme();

    }

    private void setTheme() {
        Objects.requireNonNull(getView()).setBackgroundColor(
                getResources().getColor(Settings.getInstance().getThemeID() == R.style.AppTheme ?
                        R.color.colorPrimary : R.color.colorPrimaryCold));
    }

    private void initViews(View view) {
        view.findViewById(R.id.preferencesButton).setOnClickListener(this);
        view.findViewById(R.id.homeButton).setOnClickListener(this);
        view.findViewById(R.id.settingsButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();

        switch (v.getId()) {
            case R.id.preferencesButton :
                transaction.replace(R.id.container, new ForecastPreferencesFragment()).addToBackStack(null).commit(); break;

            case R.id.settingsButton :
                if (Objects.requireNonNull(getActivity()).findViewById(R.id.cityInputBox) != null) {
                    Snackbar snackbar = Snackbar.make(v, "Leave without saving?", Snackbar.LENGTH_LONG);
                    snackbar.setAction("confirm", v1 ->
                            transaction.replace(R.id.container, new SettingsFragment()).addToBackStack(null).commit())
                            .setActionTextColor(Color.WHITE)
                            .getView().setBackgroundColor(Color.WHITE);
                    snackbar.show();
                } else {
                    transaction.replace(R.id.container, new SettingsFragment()).addToBackStack(null).commit();
                }
                break;

            case R.id.homeButton :
                if (Objects.requireNonNull(getActivity()).findViewById(R.id.cityInputBox) != null) {
                    Snackbar snackbar = Snackbar.make(v, "Leave without saving?", Snackbar.LENGTH_LONG);
                    snackbar.setAction("confirm", v1 -> Objects.requireNonNull(getActivity()).onBackPressed())
                            .setActionTextColor(Color.WHITE)
                            .getView().setBackgroundColor(Color.WHITE);
                    snackbar.show();
                } else if (getFragmentManager().getBackStackEntryCount() > 1 &&
                        Objects.requireNonNull(getActivity()).findViewById(R.id.webButton) == null){
                    Objects.requireNonNull(getActivity()).onBackPressed();
                }
        }
    }
}
