package ru.cocovella.WeatherApp.Presenter;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import ru.cocovella.WeatherApp.Model.Keys;
import ru.cocovella.WeatherApp.Model.Observer;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;
import ru.cocovella.WeatherApp.View.ForecastFragment;
import ru.cocovella.WeatherApp.View.MessageFragment;
import ru.cocovella.WeatherApp.View.SensorsFragment;


public class MainActivity extends FragmentActivity implements Observer, Keys {
    private Settings settings = Settings.getInstance();
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(settings.getThemeID());
        settings.addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleTransaction();
    }

    @Override
    public void update() {
        handleTransaction();
    }

    private void handleTransaction() {
        int resultCode = settings.getServerResultCode();
        transaction = getSupportFragmentManager().beginTransaction();
        if (prepareForecast(resultCode)) return;
        prepareMessage(resultCode);
        prepareSensors(resultCode);
        transaction.addToBackStack(null).commitAllowingStateLoss();
    }

    private boolean prepareForecast(int code) {
        if (code != CONFIRMATION_OK) return false;
        transaction.replace(R.id.container, new ForecastFragment());
        transaction.addToBackStack(null).commitAllowingStateLoss();
        return true;
    }

    private void prepareMessage(int code) {
        String message = "";
        if (code == CONFIRMATION_WAIT) {
            if (settings.getCity().isEmpty()) {
                message = getString(R.string.welcome);
            } else {
                message = getString(R.string.please_wait);
            }
        } else if (code == CONFIRMATION_ERROR && !settings.getCity().equals(getResources().getStringArray(R.array.cities)[0])) {
                message = getString(R.string.error_city_not_found);
        }
        transaction.replace(R.id.container, new MessageFragment(message));

    }

    private void prepareSensors(int code) {
        if (code == CONFIRMATION_ERROR && settings.getCity().equals(getResources().getStringArray(R.array.cities)[0])) {
            transaction.replace(R.id.container, new SensorsFragment());
        }
    }

}
