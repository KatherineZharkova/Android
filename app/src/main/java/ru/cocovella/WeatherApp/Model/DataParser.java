package ru.cocovella.WeatherApp.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DataParser implements Keys {
    private Settings settings = Settings.getInstance();
    private JSONObject jsonObject;

    public DataParser(JSONObject jsonObject) {
        if (jsonObject != null) {
            this.jsonObject = jsonObject;
            if (!parseCurrentForecast()) return;
            parseDayTimesForecast();
            settings.setServerResultCode(Keys.CONFIRMATION_OK);
        }
    }


    private boolean parseCurrentForecast() {
        try {
            JSONObject cityObject = jsonObject.getJSONObject("city");
            JSONObject list = jsonObject.getJSONArray("list").getJSONObject(0);
            JSONObject main = list.getJSONObject("main");
            JSONObject weather = list.getJSONArray("weather").getJSONObject(0);

            String city = cityObject.getString("name") + ", " + cityObject.getString("country");
            String description = weather.getString("description");
            String icon = getWeatherIcon(cityObject, weather);
            int temperature = main.getInt("temp");
            String humidity = main.getString("humidity");
            String wind = list.getJSONObject("wind").getString("speed");
            String pressure = main.getString("pressure");

            settings.setCity(city);
            if(!settings.getCitiesChoice().contains(city)) { settings.getCitiesChoice().add(city); }
            settings.setDescription(description);
            settings.setIcon(icon);
            settings.setTemperature(temperature);
            settings.setHumidity(humidity);
            settings.setWind(wind);
            settings.setBarometer(pressure);
            return true;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Some fields are not found in the JSON data");
            e.printStackTrace();
            return false;
        }
    }

    private String getWeatherIcon(JSONObject city, JSONObject weather) throws JSONException {
        String icon = "";
        long sunrise = city.getLong("sunrise") * 1000;
        long sunset =  city.getLong("sunset") * 1000;
        int actualId = weather.getInt("id");
        int id = actualId / 100;

        if(actualId == 800) {
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

    private void parseDayTimesForecast() {
        ArrayList<Forecast> forecasts = new ArrayList<>();
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ROOT);

            try {
                for (int i = 1; i <= 8; i++) {

                    JSONObject list = jsonObject.getJSONArray("list").getJSONObject(i);
                    JSONObject cityObject = jsonObject.getJSONObject("city");
                    JSONObject weather = list.getJSONArray("weather").getJSONObject(0);

                    String time = dateFormat.format(new Date(list.getLong("dt") * 1000));
                    String icon = getWeatherIcon(cityObject, weather);
                    int temperature = list.getJSONObject("main").getInt("temp");

                    forecasts.add(new Forecast(time, icon, temperature));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        settings.setForecasts(forecasts);
    }

    public class Forecast {
        private String dayTime;
        private String icon;
        private int temperature;

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
