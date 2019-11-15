package ru.cocovella.WeatherApp;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.View.MessageFragment;


public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Settings.getInstance().getThemeID());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new MessageFragment())
                    .addToBackStack(null)
                    .commit();
        }

    }
}
