package ru.cocovella.WeatherApp.Model;

import android.os.Handler;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ForecastLoader implements Keys {
    private Settings settings = Settings.getInstance();
    private static final String WEATHER_API_KEY = Keys.API_KEY1;
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric";
    private static final String KEY = "x-api-key";
    private Handler handler = new Handler();
    private static JSONObject jsonObject;


    public void request() {
        new Thread(() -> {
            settings.setServerResultCode(CONFIRMATION_WAIT);
            try {
                URL url = new URL(String.format(WEATHER_API_URL, settings.getCity()));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try {
                    jsonObject = null;
                    connection.addRequestProperty(KEY, WEATHER_API_KEY);
                    connection.connect();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder rawData = new StringBuilder(1024);
                    String tempVariable;
                    while ((tempVariable = reader.readLine()) != null) { rawData.append(tempVariable).append("\n"); }
                    reader.close();

                    jsonObject = new JSONObject(rawData.toString());
                    Log.d(LOG_TAG, "json: " + jsonObject.toString());
                    handler.post(() -> {
                        parseJSON(jsonObject);
                        parseDayTimesForecast(jsonObject);
                    });
                } finally {
                    connection.disconnect();
                    if (jsonObject != null) {
                        int res = jsonObject.getInt("cod");
                        if (res > 0) handler.post(() -> settings.setServerResultCode(res));
                    }
                    handler.post(() -> Log.i(Keys.LOG_TAG, "RESULT CODE = " + settings.getServerResultCode()));
                }
            } catch (Exception e) {
                settings.setServerResultCode(CONFIRMATION_ERROR);
            }
        }).start();
    }

    private void parseJSON(JSONObject jsonObject) {
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

        } catch (JSONException e) {
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
            e.printStackTrace();
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
            if(currentTime >= sunrise && currentTime < sunset) {
                icon = "\uF00D";    //sunny
            } else {
                icon = "\uF02E";  //clear_night
            }
        } else {
            switch (id) {
                case 2: {
                    icon = "\uF01E";    //thunder
                    break;
                }
                case 3: {
                    icon = "\uF009";  // drizzle
                    break;
                }
                case 5: {
                    icon = "\uF019";  // rainy
                    break;
                }
                case 6: {
                    icon = "\uF01B";  //snowy
                    break;
                }
                case 7: {
                    icon = "\uF021";  //foggy
                    break;
                }
                case 8: {
                    icon = "\uF013";    //cloudy
                    break;
                }
            }
        }

        return icon;
    }

    private void parseDayTimesForecast(JSONObject jsonObject) {
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
