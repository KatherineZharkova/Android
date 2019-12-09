package ru.cocovella.WeatherApp.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ru.cocovella.WeatherApp.R;
import static java.util.Objects.requireNonNull;


public class SensorsFragment extends Fragment {
    private TextView temperatureTV;
    private TextView humidityTV;
    private Sensor temperatureSensor;
    private Sensor humiditySensor;
    private SensorManager sensorManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensors, container, false);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        temperatureTV = view.findViewById(R.id.temperatureSensor);
        humidityTV = view.findViewById(R.id.humiditySensor);

        sensorManager = (SensorManager) requireNonNull(getActivity()).getSystemService(Context.SENSOR_SERVICE);
        temperatureSensor = requireNonNull(sensorManager).getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (temperatureSensor == null) {
            temperatureTV.setText("No temperature sensor found");
        }
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (humiditySensor == null) {
            humidityTV.setText("No humidity sensor found");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(temperatureListener, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(humidityListener, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener temperatureListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            StringBuilder temperature = new StringBuilder();
            temperature.append("Temperature ").append(event.values[0]).append("°C");
            temperatureTV.setText(temperature);
            sensorManager.unregisterListener(temperatureListener, temperatureSensor);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private SensorEventListener humidityListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            StringBuilder humidity = new StringBuilder();
            humidity.append("Humidity ").append(event.values[0]).append("%");
            humidityTV.setText(humidity);
            sensorManager.unregisterListener(humidityListener, humiditySensor);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
