package ru.cocovella.WeatherApp.View;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;
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
                case R.id.preferencesButton :
                    transaction.replace(R.id.container, new ForecastPreferencesFragment()).addToBackStack(null).commit();
                    break;

                case R.id.settingsButton :
                    if (Objects.requireNonNull(getActivity()).findViewById(R.id.cityInputBox) != null) {
                        Snackbar snackbar = Snackbar.make(view, "Leave without saving?", Snackbar.LENGTH_LONG);
                        snackbar.setAction("confirm", v1 -> transaction.replace(R.id.container, new SettingsFragment()).addToBackStack(null).commit())
                                .setActionTextColor(Color.WHITE)
                                .getView().setBackgroundColor(Color.WHITE);
                        snackbar.show();
                    } else {
                        transaction.replace(R.id.container, new SettingsFragment()).addToBackStack(null).commit();
                    }
                    break;

                case R.id.homeButton :
                    if (Objects.requireNonNull(getActivity()).findViewById(R.id.webButton) == null && getActivity().findViewById(R.id.messageText) == null) {
                        Snackbar snackbar = Snackbar.make(view, "Leave without saving?", Snackbar.LENGTH_LONG);
                        snackbar.setAction("confirm", v1 -> getFragmentManager().popBackStack())
                                .setActionTextColor(Color.WHITE)
                                .getView().setBackgroundColor(Color.WHITE);
                        snackbar.show();
                    }
            }
            return false;
        });

    }

}
