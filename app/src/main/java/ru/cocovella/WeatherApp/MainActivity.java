package ru.cocovella.WeatherApp;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;
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
        showWelcome();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showWelcome();
        debugBackStack();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        showWelcome();
        debugBackStack();
    }

    private void showWelcome() {
        if (getSupportFragmentManager().getFragments().size() < 2) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new MessageFragment()).commit();
        }
    }

    private void debugBackStack() {
        String writingMethodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int backStackLogLastEntryNumber = getSupportFragmentManager().getBackStackEntryCount();
        ArrayList<String> fragmentNamesList = new ArrayList<>();
        List fragments = getSupportFragmentManager().getFragments();
        for (Object fragment : fragments) {
            fragmentNamesList.add(fragment.getClass().getSimpleName());
        }
        Log.d("FRAGMENTS",
                writingMethodName + " => " +
                        "#" + backStackLogLastEntryNumber + " : " + fragmentNamesList);
    }

}
