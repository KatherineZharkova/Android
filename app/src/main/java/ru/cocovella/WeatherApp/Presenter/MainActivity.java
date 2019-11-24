package ru.cocovella.WeatherApp.Presenter;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;
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
        if (Settings.getInstance().getCity().isEmpty()) {
            showWelcome();
        }
    }

    private void showWelcome() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Settings.getInstance().setMessage(getString(R.string.welcome));
        transaction.replace(R.id.container, new MessageFragment()).addToBackStack(null).commit();
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
        Log.d("FRAGMENTS",
                writingMethodName + " => " +
                        "#" + backStackLogLastEntryNumber + " : " + fragmentNamesList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        debugBackStack();
    }
}
