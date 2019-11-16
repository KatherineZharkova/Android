package ru.cocovella.WeatherApp.Model;

import android.util.Log;
import java.util.Random;


public class ForecastServer {
    private Settings settings = Settings.getInstance();
    private int resultCode = new Random().nextInt(2) + 403;

    public void request() {
        onResultCodeReceivedImitation();
    }

    private void onResultCodeReceivedImitation() {
        settings.setServerResultCode(resultCode);
        if (settings.getServerResultCode()) {
            settings.setTemperature("17");
            settings.setDescription("🌺🌺🌺 Sunny skies all day long ☀☀☀");
            settings.setHumidity("85");
            settings.setWind("4");
            settings.setBarometer("845");
        }
        Log.i("MY_FRAGMENTS", "RESULT CODE = " + settings.getServerResultCode());
    }

}
