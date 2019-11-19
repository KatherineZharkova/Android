package ru.cocovella.WeatherApp.View;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Random;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;


public class ForecastCardAdapter extends RecyclerView.Adapter<ForecastCardAdapter.Item> {

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
        holder.setWhen(position);
        holder.setTemperature(position);
        holder.setIcon();
    }

    @Override
    public int getItemCount() {
        return 7;
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

        void setWhen(int position){
            ArrayList<String> dayTime = new ArrayList<>();
            dayTime.add("06:00");
            dayTime.add("09:00");
            dayTime.add("12:00");
            dayTime.add("15:00");
            dayTime.add("18:00");
            dayTime.add("21:00");
            dayTime.add("24:00");
            when.setText(dayTime.get(position));
        }

        void setTemperature(int position) {
            int currentTemperature = Integer.parseInt(Settings.getInstance().getTemperature());
            int[] changes = {-3, -1, 3, 5, 4, 1, 0};
            String result = currentTemperature + changes[position] + "°C";
            temperature.setText(result);
        }

        void setIcon() {
            Typeface weatherFont = Typeface.createFromAsset(icon.getContext().getAssets(), "fonts/weather.ttf");
            icon.setTypeface(weatherFont);
            String[] icons = {"\uF0C8", "\uF0C5", "\uF075", "\uF071", "\uF056", "\uF074", "\uF010"};
            icon.setText(icons[new Random().nextInt(icons.length)]);
        }


    }
}
