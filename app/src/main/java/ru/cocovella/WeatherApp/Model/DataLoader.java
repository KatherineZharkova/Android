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
    private Settings settings;
    private Response<WeatherModel> response;
    private String cityName;
    private Retrofit retrofit;


    public DataLoader(String cityName) {
        this.cityName = cityName;
        settings = Settings.getInstance();
        initRetrofit();
        load();
    }


    interface OpenWeather{
        @GET("data/2.5/forecast")
        Call<WeatherModel> getWeather(@Query("q")String q, @Query("units")String units, @Query("appid") String key);
//        http://api.openweathermap.org/data/2.5/forecast?q=Moscow,Ru&units=metric&appid=15187eae9316fbcbc4a42dc59d95169d
    }

    private void load() {
        settings.setServerResultCode(CONFIRMATION_WAIT);
        try {
            new DataParser(getData());
        } catch (Exception e) {
            e.printStackTrace();
//            settings.setServerResultCode(CONFIRMATION_ERROR);
            Log.e(LOG_TAG, "ResponseCode: Exception on DataLoader.load()");
        }
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private WeatherModel getData() throws IOException {
        OpenWeather openWeather = retrofit.create(OpenWeather.class);
        int apiRequests = settings.getApiRequestCounter();
        if (response != null && response.errorBody() != null) { response.errorBody().close(); }
        response = openWeather.getWeather(cityName, "metric",
                apiRequests % 2 == 0 ? API_KEY2 : API_KEY1).execute();
        settings.setApiRequestCounter(apiRequests+1);

        if(response.isSuccessful()) {
            return response.body();
        } else {
            settings.setServerResultCode(response.code());
            Log.e(LOG_TAG, "ResponseCode: failure on DataLoader.getData() " + response.message());
            assert response.errorBody() != null;
            Log.d(LOG_TAG, response.errorBody().toString());
            response.errorBody().string();
            return null;
        }
    }

}
