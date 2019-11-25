package ru.cocovella.WeatherApp.Model;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class ForecastLoader implements Keys {
    private Settings settings = Settings.getInstance();
    private static final String WEATHER_API_KEY = Keys.API_KEY1;
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String KEY = "x-api-key";


    public void request() {
        new Thread(() -> {

            try {
                URL url = new URL(String.format(WEATHER_API_URL, settings.getCity()));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try {
                    connection.addRequestProperty(KEY, WEATHER_API_KEY);

                    if (connection.getConnectTimeout() > 5000) {
                        connection.disconnect();
                    } else {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder rawData = new StringBuilder(1024);
                        String tempVariable;
                        while ((tempVariable = reader.readLine()) != null) { rawData.append(tempVariable).append("\n"); }
                        reader.close();

                        JSONObject jsonObject = new JSONObject(rawData.toString());
                        Log.d(LOG_TAG, "json: " + jsonObject.toString());
                        settings.setServerResultCode(jsonObject.getInt("cod"));
                        Log.i(Keys.LOG_TAG, "RESULT CODE = " + settings.getServerResultCode());
                        parseJSON(jsonObject);
                    }
                } catch (FileNotFoundException exc) {
                    settings.setServerResultCode(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void parseJSON(JSONObject jsonObject) {
        try {
            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            String city = jsonObject.getString("name") + ", "
                    + jsonObject.getJSONObject("sys").getString("country");
            String description = details.getString("description");
            String icon = getWeatherIcon(jsonObject);
            int temperature = main.getInt("temp");
            String humidity = main.getString("humidity");
            String wind = jsonObject.getJSONObject("wind").getString("speed");
            String pressure = main.getString("pressure");

            settings.setCity(city);
            if(!settings.getCitiesChoice().contains(city)) { settings.getCitiesChoice().add(city); }
            settings.setDescription(description);
            settings.setIcon(icon);
            settings.setTemperature(temperature);
            settings.setHumidity(humidity);
            settings.setWind(wind);
            settings.setBarometer(pressure);

            settings.setForecasts(new ForecastLoader().parseDayTimesForecast());

        } catch (JSONException e) {
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
            e.printStackTrace();
        }
    }

    private String getWeatherIcon(JSONObject jsonObject) throws JSONException {
        JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
        String icon = "";

        long sunrise = jsonObject.getJSONObject("sys").getLong("sunrise") * 1000;
        long sunset = jsonObject.getJSONObject("sys").getLong("sunset") * 1000;
        int actualId = details.getInt("id");
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
