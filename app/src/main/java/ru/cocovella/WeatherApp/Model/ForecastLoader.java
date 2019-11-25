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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ForecastLoader implements Keys {
    private Settings settings = Settings.getInstance();
    private static final String WEATHER_API_KEY = Keys.API_KEY1;
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric";
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

            settings.setForecasts(parseDayTimesForecast(jsonObject));

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

    private ArrayList<Forecast> parseDayTimesForecast(JSONObject jsonObject) throws JSONException {
        ArrayList<Forecast> forecasts = new ArrayList<>();
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.UK);

        for (int i = 1; i <= 8; i++) {
            JSONObject list = jsonObject.getJSONArray("list").getJSONObject(i);
            JSONObject cityObject = jsonObject.getJSONObject("city");
            JSONObject weather = list.getJSONArray("weather").getJSONObject(0);

            String time = dateFormat.format(new Date(list.getLong("dt") * 1000));
            String icon = getWeatherIcon(cityObject, weather);
            int temperature = list.getJSONObject("main").getInt("temp");

            forecasts.add(new Forecast(time, icon, temperature));
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
