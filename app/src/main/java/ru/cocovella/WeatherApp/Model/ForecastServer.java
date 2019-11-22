package ru.cocovella.WeatherApp.Model;

import android.util.Log;
import java.util.ArrayList;
import java.util.Random;


public class ForecastServer {
    private Settings settings = Settings.getInstance();
    private int resultCode = Tags.CONFIRMATION_CODE;
//    private int resultCode = new Random().nextInt(2) + 403;

    public void request() {
        onResultCodeReceivedImitation();
    }

    private void onResultCodeReceivedImitation() {
        settings.setServerResultCode(resultCode);
        if (settings.getServerResultCode()) {
            settings.setTemperature(17);
            settings.setDescription("🌺🌺🌺 Sunny skies all day long ☀☀☀");
            settings.setHumidity("85");
            settings.setWind("4");
            settings.setBarometer("845");
            settings.setForecasts(new ForecastServer().parseDayTimesForecast());
        }
        Log.i("MY_FRAGMENTS", "RESULT CODE = " + settings.getServerResultCode());
    }


    private ArrayList<Forecast> parseDayTimesForecast() {
        ArrayList<Forecast> forecasts = new ArrayList<>();
        String[] dayTimes = {"06:00", "09:00", "12:00", "15:00", "18:00", "21:00", "24:00"};
        String[] icons = {"\uF0C8", "\uF0C5", "\uF075", "\uF071", "\uF056", "\uF074", "\uF010"};
        int[] temperatures = {-3, -1, 3, 5, 4, 1, 0};
        for (int i = 0; i < dayTimes.length; i++) {
            String icon = icons[new Random().nextInt(icons.length)];
            int temperature = settings.getTemperature() + temperatures[i];
            forecasts.add(new Forecast(dayTimes[i], icon, temperature));
        }
        return forecasts;
    }


    public class Forecast {
        String dayTime;
        String icon;
        int temperature;

        private Forecast(String dayTime, String icon, int temperature) {
            this.dayTime = dayTime;
            this.icon = icon;
            this.temperature = temperature;
        }

        public String getDayTime() {
            return dayTime;
        }

        public String getIcon() {
            return icon;
        }

        public int getTemperature() {
            return temperature;
        }

    }

}
