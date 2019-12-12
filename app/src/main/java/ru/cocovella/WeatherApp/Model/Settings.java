package ru.cocovella.WeatherApp.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Settings implements Keys, Observable{
    private static Settings instance;
    private String city;
    private String description;
    private String icon;
    private int temperature;
    private String humidity;
    private ArrayList<String> citiesChoice;
    private String wind;
    private String barometer;
    private int serverResultCode;
    private ArrayList<DataParser.Forecast> forecasts = new ArrayList<>();
    private ArrayList<Observer> observers = new ArrayList<>();


    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public String getCity() {
        return city;
    }

    void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTemperature() {
        return temperature;
    }

    void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public ArrayList<String> getCitiesChoice() {
        Collections.sort(citiesChoice);
        return citiesChoice;
    }

    public void setCitiesChoice(String[] array) {
        if (citiesChoice == null) {
            citiesChoice = new ArrayList<>(Arrays.asList(array));
        }
    }

    public String getHumidity() {
        return humidity;
    }

    void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    void setWind(String wind) {
        this.wind = wind;
    }

    public String getBarometer() {
        return barometer;
    }

    void setBarometer(String barometer) {
        this.barometer = barometer;
    }

    public int getServerResultCode() {
        return serverResultCode;
    }

    void setServerResultCode(int resultCode) {
        this.serverResultCode = resultCode;
        notifyObservers();
    }

    public ArrayList<DataParser.Forecast> getForecasts() {
        return forecasts;
    }

    void setForecasts(ArrayList<DataParser.Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

}
