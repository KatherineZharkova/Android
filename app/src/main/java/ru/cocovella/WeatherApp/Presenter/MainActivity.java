package ru.cocovella.WeatherApp.Presenter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import ru.cocovella.WeatherApp.Model.DataLoader;
import ru.cocovella.WeatherApp.Model.Keys;
import ru.cocovella.WeatherApp.Model.Observer;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;
import ru.cocovella.WeatherApp.View.ForecastFragment;
import ru.cocovella.WeatherApp.View.MessageFragment;
import ru.cocovella.WeatherApp.View.SensorsFragment;


public class MainActivity extends FragmentActivity implements Observer, Keys {
    private Settings settings;
    private FragmentTransaction transaction;
    private SharedPreferences sharedPreferences;
    private String recentInput;
    private String sensors;
    private int resultCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        setTheme(sharedPreferences.getInt(THEME_ID, R.style.ColdTheme));
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = Settings.getInstance();
        settings.addObserver(this);
        setBackground();
        setCitiesChoice();
        welcome();
    }

    private void setBackground() {
        ImageView imageView = findViewById(R.id.customBackgroundView);
        String path = sharedPreferences.getString(BACKGROUND, getString(R.string.defaultBackgroundUrl));
        Picasso.with(this)
                .load(path)
                .fit()
                .into(imageView);
    }

    private void setCitiesChoice() {
        String [] array  = getResources().getStringArray(R.array.cities);
        sensors = array[0];
        HashSet<String> set = new HashSet<>(Arrays.asList(array));
        ArrayList<String> result = new ArrayList<>(sharedPreferences.getStringSet(CITIES_LIST, set));
        settings.setCitiesChoice(result);
    }

    private void welcome () {
        recentInput = sharedPreferences.getString(CITY_KEY, "");
//        if (recentInput.isEmpty() || recentInput.contains("Sensors") || recentInput.contains("sensors")) {
            handleTransaction();
//            return;
//        }
        new Thread(() -> new DataLoader(recentInput)).start();
    }

    private void handleTransaction() {
        recentInput = sharedPreferences.getString(CITY_KEY, "");
        resultCode = settings.getServerResultCode();
        transaction = getSupportFragmentManager().beginTransaction();
        if (showForecast()) return;
        if (showSensors()) return;
        prepareMessage();
    }

    private boolean showForecast() {
        if (resultCode != CONFIRMATION_OK) return false;
        transaction.replace(R.id.container, new ForecastFragment());
        transaction.commitAllowingStateLoss();
        return true;
    }

    private boolean showSensors() {
        if (!recentInput.contains(sensors)) { return false; }
        transaction.replace(R.id.container, new SensorsFragment());
        transaction.commit();
        return true;
    }

    private void prepareMessage() {
        String message;
        if (recentInput.isEmpty()) {
            message = getString(R.string.welcome);
        } else if (resultCode == CONFIRMATION_WAIT) {
            message = getString(R.string.please_wait);
        } else if (resultCode == CONFIRMATION_BANNED){
            message = getString(R.string.error_banned);
        } else {
            message = getString(R.string.error_city_not_found);
        }
        transaction.replace(R.id.container, new MessageFragment(message));
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void update() {
        handleTransaction();
    }

    @Override
    public void onBackPressed() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.MainLayout), "Exit now?", Snackbar.LENGTH_LONG);
        snackbar.setAction("confirm", v1 -> super.onBackPressed())
                .setActionTextColor(Color.WHITE).getView().setBackgroundColor(Color.WHITE);
        snackbar.show();
    }

}
