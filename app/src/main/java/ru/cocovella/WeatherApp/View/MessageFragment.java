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
import ru.cocovella.WeatherApp.R;


public class MessageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        setMessageText(view);
        setMessageButton(view);
        return view;
    }

    private void setMessageText(View view) {
        TextView messageTV = view.findViewById(R.id.messageText);
        String message = getString(R.string.welcome);

        if (Settings.getInstance().getCity() == null) {
            message = getString(R.string.welcome);
        } else if (Settings.getInstance().getCity().length() < 2) {
            message = getString(R.string.error_city_not_found);
        }
        messageTV.setText(message);
    }


    private void setMessageButton(View v) {
        v.findViewById(R.id.messageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                transaction.replace(R.id.container, new ForecastPreferencesFragment())
//                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}
