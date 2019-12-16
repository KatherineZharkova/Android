package ru.cocovella.WeatherApp.Model;

import java.util.ArrayList;
import java.util.Collections;


public class Settings implements Keys, Observable{
    private static Settings instance;
    private ArrayList<String> citiesChoice;
    private String city;
    private String description;
    private String icon;
    private double temperature;
    private int humidity;
    private double wind;
    private int barometer;
    private int serverResultCode;
    private ArrayList<DataParser.Forecast> hoursForecasts = new ArrayList<>();
    private ArrayList<DataParser.Forecast> daysForecasts = new ArrayList<>();
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

    public double getTemperature() {
        return temperature;
    }

    void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public ArrayList<String> getCitiesChoice() {
        Collections.sort(citiesChoice);
        return citiesChoice;
    }

    public void setCitiesChoice(ArrayList<String> array) {
        if (citiesChoice == null) {
            citiesChoice = array;
        }
    }

    public int getHumidity() {
        return humidity;
    }

    void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWind() {
        return wind;
    }

    void setWind(double wind) {
        this.wind = wind;
    }

    public int getBarometer() {
        return barometer;
    }

    void setBarometer(int barometer) {
        this.barometer = barometer;
    }

    public int getServerResultCode() {
        return serverResultCode;
    }

    void setServerResultCode(int resultCode) {
        this.serverResultCode = resultCode;
        notifyObservers();
    }

    public ArrayList<DataParser.Forecast> getHoursForecasts() {
        return hoursForecasts;
    }

    void setHoursForecasts(ArrayList<DataParser.Forecast> hoursForecasts) {
        this.hoursForecasts = hoursForecasts;
    }

    public ArrayList<DataParser.Forecast> getDaysForecasts() {
        return daysForecasts;
    }

    void setDaysForecasts(ArrayList<DataParser.Forecast> daysForecasts) {
        this.daysForecasts = daysForecasts;
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
