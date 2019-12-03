package ru.cocovella.WeatherApp.Presenter;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import ru.cocovella.WeatherApp.Model.Observer;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;
import ru.cocovella.WeatherApp.View.ForecastFragment;
import ru.cocovella.WeatherApp.View.MessageFragment;
import ru.cocovella.WeatherApp.View.SensorsFragment;


public class MainActivity extends FragmentActivity implements Observer {
    private Settings settings = Settings.getInstance();

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
        welcome();
    }

    private void welcome() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new ForecastFragment()).addToBackStack(null).commit();
    }

    @Override
    public void update() {
        int resultCode = settings.getServerResultCode();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (resultCode == 1) {
            transaction.replace(R.id.container, new ForecastFragment());
        } else if (resultCode == 0) {
            settings.setMessage("Please wait...");
            transaction.replace(R.id.container, new MessageFragment());
        } else if (resultCode == -1){
            String myLocation = getResources().getStringArray(R.array.cities)[0];
            if (settings.getCity().equals(myLocation)) {
                transaction.replace(R.id.container, new SensorsFragment());
            } else {
                settings.setMessage(getString(R.string.error_city_not_found));
                transaction.replace(R.id.container, new MessageFragment());
            }
        }
        transaction.addToBackStack(null).commitAllowingStateLoss();
    }
}
