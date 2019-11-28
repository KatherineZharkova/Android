package ru.cocovella.WeatherApp.Presenter;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;
import ru.cocovella.WeatherApp.Model.Keys;
import ru.cocovella.WeatherApp.Model.Observer;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;
import ru.cocovella.WeatherApp.View.ForecastFragment;
import ru.cocovella.WeatherApp.View.MessageFragment;


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
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        debugBackStack();
    }

    private void debugBackStack() {
        String writingMethodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int backStackLogLastEntryNumber = getSupportFragmentManager().getBackStackEntryCount();
        ArrayList<String> fragmentNamesList = new ArrayList<>();
        List fragments = getSupportFragmentManager().getFragments();
        for (Object fragment : fragments) {
            fragmentNamesList.add(fragment.getClass().getSimpleName());
        }
        Log.d(Keys.LOG_TAG,
                writingMethodName + " => " +
                        "#" + backStackLogLastEntryNumber + " : " + fragmentNamesList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        debugBackStack();
    }

    @Override
    public void update() {
        Settings settings = Settings.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (settings.getServerResultCode() == 1) {
            transaction.replace(R.id.container, new ForecastFragment()).addToBackStack(null).commitAllowingStateLoss();
        } else if (settings.getServerResultCode() == 0) {
            settings.setMessage("Please wait...");
            transaction.replace(R.id.container, new MessageFragment()).addToBackStack(null).commitAllowingStateLoss();
        } else if (settings.getServerResultCode() == -1){
                settings.setMessage(getString(R.string.error_city_not_found));
                transaction.replace(R.id.container, new MessageFragment()).addToBackStack(null).commitAllowingStateLoss();
        }
    }
}
