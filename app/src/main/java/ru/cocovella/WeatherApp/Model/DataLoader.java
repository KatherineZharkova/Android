package ru.cocovella.WeatherApp.Model;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DataLoader implements Keys {
    private Settings settings = Settings.getInstance();
    private static final String WEATHER_API_KEY = Keys.API_KEY1;
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric";
    private static final String KEY = "x-api-key";
    private static HttpURLConnection connection;


    public JSONObject load(String city) {
        settings.setServerResultCode(CONFIRMATION_WAIT);
        JSONObject jsonObject = null;

        try {
            URL url = new URL(String.format(WEATHER_API_URL, city));
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY, WEATHER_API_KEY);
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;
            while ((tempVariable = reader.readLine()) != null) { rawData.append(tempVariable).append("\n"); }
            reader.close();

            jsonObject = new JSONObject(rawData.toString());
            Log.d(LOG_TAG, "json: " + jsonObject.toString());

        } catch (Exception e) {
            settings.setServerResultCode(CONFIRMATION_ERROR);
            e.printStackTrace();

        } finally { if (connection != null) connection.disconnect(); }

        return jsonObject;
    }
}
