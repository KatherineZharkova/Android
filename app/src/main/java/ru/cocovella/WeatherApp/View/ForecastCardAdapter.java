package ru.cocovella.WeatherApp.View;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import ru.cocovella.WeatherApp.Model.ForecastServer;
import ru.cocovella.WeatherApp.R;


public class ForecastCardAdapter extends RecyclerView.Adapter<ForecastCardAdapter.Item> {
    private ArrayList<ForecastServer.Forecast> forecasts;

    ForecastCardAdapter(ArrayList<ForecastServer.Forecast> forecasts) {
        if (forecasts != null) {
            this.forecasts = forecasts;
        }
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        ForecastServer.Forecast f = forecasts.get(position);
        holder.setWhen(f.getDayTime());
        holder.setTemperature(f.getTemperature());
        holder.setIcon(f.getIcon());
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }



    class Item extends RecyclerView.ViewHolder {
        private TextView when;
        private TextView temperature;
        private TextView icon;

        Item(@NonNull View view) {
            super(view);
            when = view.findViewById(R.id.when);
            temperature = view.findViewById(R.id.temperature);
            icon = view.findViewById(R.id.icon);
        }

        void setWhen(String dayTime){
            this.when.setText(dayTime);
        }

        void setTemperature(int temperature) {
            String result = temperature + "°C";
            this.temperature.setText(result);
        }

        void setIcon(String icon) {
            this.icon.setTypeface(Typeface.createFromAsset(this.icon.getContext().getAssets(), "fonts/weather.ttf"));
            this.icon.setText(icon);
        }

    }
}
