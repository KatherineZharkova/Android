package ru.cocovella.WeatherApp.Model;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ru.cocovella.WeatherApp.Model.ForecastModel.WeatherModel;


public class DataParser implements Keys {
    private Settings settings = Settings.getInstance();
    private WeatherModel model;

    DataParser(WeatherModel model) {
        if (model != null) {
            this.model = model;
            if (!parseCurrentForecast()) return;
            parseDayHoursForecast();
            parseWeekDaysForecast();
            settings.setServerResultCode(Keys.CONFIRMATION_OK);
        }
    }

    private boolean parseCurrentForecast() {
            String city = model.getCity().getName() + ", " + model.getCity().getCountry();
            String description = model.getList().get(0).getWeather().get(0).getDescription();
            String icon = getWeatherIcon();
            double temperature = model.getList().get(0).getMain().getFeelsLike();
            int humidity = model.getList().get(0).getMain().getHumidity();
            double wind = model.getList().get(0).getWind().getSpeed();
            int pressure = model.getList().get(0).getMain().getPressure();

            settings.setCity(city);
            if(!settings.getCitiesChoice().contains(city)) { settings.getCitiesChoice().add(city); }
            settings.setDescription(description);
            settings.setIcon(icon);
            settings.setTemperature(temperature);
            settings.setHumidity(humidity);
            settings.setWind(wind);
            settings.setBarometer(pressure);
            return true;
    }

    private String getWeatherIcon() {
        String icon = "";
        long sunrise = model.getCity().getSunrise() * 1000;
        long sunset =  model.getCity().getSunset() * 1000;
        int iconID = model.getList().get(0).getWeather().get(0).getId();
        int id = iconID / 100;

        if(iconID == 800) {
            long currentTime = new Date().getTime();
            icon = currentTime >= sunrise && currentTime < sunset ? "\uF00D" : "\uF02E";    //sunny or clear_night
        } else {
            switch (id) {
                case 2: { icon = "\uF01E"; break; }     //thunder
                case 3: { icon = "\uF009"; break; }     // drizzle
                case 5: { icon = "\uF019"; break; }     // rainy
                case 6: { icon = "\uF01B"; break; }     //snowy
                case 7: { icon = "\uF021"; break; }     //foggy
                case 8: { icon = "\uF013"; break; }     //cloudy
            }
        }
        return icon;
    }

    private void parseDayHoursForecast() {
        ArrayList<Forecast> forecasts = new ArrayList<>();
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 1; i <= 8; i++) {
            String time = dateFormat.format(new Date(model.getList().get(i).getDt() * 1000));
            String icon = getWeatherIcon();
            double temp = model.getList().get(i).getMain().getFeelsLike();
            String temperature = (int)temp + "°C";
            forecasts.add(new Forecast(time, icon, temperature));
        }
        settings.setHoursForecasts(forecasts);
    }

    private void parseWeekDaysForecast() {
        ArrayList<Forecast> forecasts = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i <= 4; i++) {
            String dayOfWeek = dateFormat.format(new Date(model.getList().get(i*8).getDt() * 1000));
            String icon = getWeatherIcon();
            double tempMin = model.getList().get(i*8).getMain().getFeelsLike();
            double tempMax = model.getList().get(i*8).getMain().getTempMax();
            String temperature = tempMax > tempMin ? (int) tempMin + ".." + (int) tempMax + "°C" :
                    (int) tempMax + ".." + (int) tempMin + "°C";
            forecasts.add(new Forecast(dayOfWeek, icon, temperature));
            Log.d(LOG_TAG, "time " + dayOfWeek + "temp " + temperature);
        }
        settings.setDaysForecasts(forecasts);
    }


    public class Forecast {
        private String dayTime;
        private String icon;
        private String temperature;

        private Forecast(String dayTime, String icon, String temperature) {
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

        public String getTemperature() {
            return temperature;
        }

    }

}
