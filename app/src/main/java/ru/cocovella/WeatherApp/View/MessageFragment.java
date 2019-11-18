package ru.cocovella.WeatherApp.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Objects;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.Model.Tags;
import ru.cocovella.WeatherApp.R;


public class MessageFragment extends Fragment implements Tags {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setMessageText();
        setMessageButton();
    }

    private void setMessageText() {
        TextView messageTV = Objects.requireNonNull(getView()).findViewById(R.id.messageText);
        messageTV.setText(Settings.getInstance().getMessage());
    }

    private void setMessageButton() {
        Objects.requireNonNull(getView()).findViewById(R.id.messageButton).setOnClickListener(v -> {
            FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
            transaction.replace(R.id.container, new ForecastPreferencesFragment()).addToBackStack(null).commit();
        });
    }

}
