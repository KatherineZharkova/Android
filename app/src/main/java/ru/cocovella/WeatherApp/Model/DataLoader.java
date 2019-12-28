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
    private Response<WeatherModel> response;
    private String cityName;
    private String lat;
    private String lon;
    private Retrofit retrofit;


    public DataLoader(String cityName) {
        this.cityName = cityName;
        initRetrofit();
        load();
    }

    public DataLoader(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
        initRetrofit();
        load();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    interface OpenWeatherCity {
        @GET("data/2.5/forecast")
        Call<WeatherModel> getWeather(@Query("q") String q, @Query("units") String units, @Query("appid") String key);
        // http://api.openweathermap.org/data/2.5/forecast?q=Moscow,Ru&units=metric&appid=15187eae9316fbcbc4a42dc59d95169d
    }

    interface OpenWeatherCoord{
        @GET("data/2.5/forecast")
        Call<WeatherModel> getWeather(@Query("lat")String lat, @Query("lon")String lon, @Query("units")String units, @Query("appid") String key);
        // http://api.openweathermap.org/data/2.5/forecast?lat=55.751244&lon=37.618423&units=metric&appid=15187eae9316fbcbc4a42dc59d95169d
    }

    private void load() {
        Settings.getInstance().setServerResultCode(CONFIRMATION_WAIT);
        try {
            new DataParser(getData());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "ResponseCode: Exception on DataLoader.load()");
        }
    }

    private WeatherModel getData() throws IOException {
//        OpenWeather openWeather = retrofit.create(OpenWeather.class);
        int apiRequests = Settings.getInstance().getApiRequestCounter();
        if (response != null && response.errorBody() != null) { response.errorBody().close(); }

        String key = apiRequests % 2 == 0 ? API_KEY2 : API_KEY1;

        response = (cityName == null ?
                retrofit.create(OpenWeatherCoord.class).getWeather(lat, lon, "metric", key) :
                retrofit.create(OpenWeatherCity.class).getWeather(cityName, "metric", key))
                .execute();

        if(response.isSuccessful()) {
            return response.body();
        } else {
            Settings.getInstance().setServerResultCode(response.code());
            Log.e(LOG_TAG, "ResponseCode: failure on DataLoader.getData() " + response.message());
            assert response.errorBody() != null;
            Log.d(LOG_TAG, response.errorBody().toString());
            response.errorBody().string();
            return null;
        }
    }

}
