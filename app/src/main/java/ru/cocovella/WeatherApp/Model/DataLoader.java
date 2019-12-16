package ru.cocovella.WeatherApp.Model;

import android.util.Log;

import java.io.IOException;

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
    private OpenWeather openWeather;
    private Response<WeatherModel> response;
    private String cityName;


    interface OpenWeather{
        @GET("data/2.5/forecast")
        Call<WeatherModel> getWeather(@Query("q")String q, @Query("units")String units, @Query("appid") String key);
//        http://api.openweathermap.org/data/2.5/forecast?q=Moscow,Ru&units=metric&appid=15187eae9316fbcbc4a42dc59d95169d
    }

    public void load(String city) {
        settings.setServerResultCode(CONFIRMATION_WAIT);
        cityName = city;

        try {
            initRetrofit();
            new DataParser(getData());
        } catch (Exception e) {
            e.printStackTrace();
            settings.setServerResultCode(CONFIRMATION_ERROR);
        }

    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    private WeatherModel getData() throws Exception {
        try {
            response = openWeather.getWeather(cityName, "metric", WEATHER_API_KEY).execute();
        } catch (IOException e) {
            Log.d(LOG_TAG, "ResponseCode: IOException");
        }

        if(response.isSuccessful()) {
            settings.setServerResultCode(Keys.CONFIRMATION_OK);
            Log.d(LOG_TAG, "ResponseCode: success " + response.code());
            return response.body();
        } else {
            settings.setServerResultCode(CONFIRMATION_ERROR);
            Log.d(LOG_TAG, "ResponseCode: failure " + response.code());
            assert response.errorBody() != null;
            throw new Exception(response.errorBody().string(), null);
        }
    }

}
