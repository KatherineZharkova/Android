package ru.cocovella.WeatherApp.Model;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.cocovella.WeatherApp.Model.ForecastModel.WeatherModel;


public class DataLoader implements Keys {
    private Settings settings = Settings.getInstance();
    private static final String WEATHER_API_KEY = Keys.API_KEY1;
    //    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric";


    interface OpenWeather{
        @GET("data/2.5/forecast")
        Call<WeatherModel> getWeather(@Query("q")String q, @Query("units")String units, @Query("appid") String key);
    }

    public WeatherModel load(String city) throws Exception {
        settings.setServerResultCode(CONFIRMATION_WAIT);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OpenWeather openWeather = retrofit.create(OpenWeather.class);

        Response<WeatherModel> response = openWeather.getWeather(city, "metric", WEATHER_API_KEY).execute();

        Log.d(LOG_TAG, "ResponseCode: " + response.code());

        if (response.isSuccessful()) {
        settings.setServerResultCode(CONFIRMATION_WAIT);
            return response.body();
        } else {
            settings.setServerResultCode(CONFIRMATION_ERROR);
            assert response.errorBody() != null;
            throw new Exception(response.errorBody().string(), null);
        }
    }

}
