package ru.cocovella.WeatherApp.Model;

import java.util.ArrayList;
import ru.cocovella.WeatherApp.R;


public class Settings implements Tags, Observable{
    private ArrayList<Observer> observers = new ArrayList<>();
    private static Settings instance;
    private String city;
    private String description;
    private String temperature;
    private String humidity;
    private boolean isHumidityCB;
    private String wind;
    private boolean isWindCB;
    private String barometer;
    private boolean isBarometerCB;
    private int radioCityID;
    private int themeID;
    private int serverResultCode;
    private String message;

    private Settings() {
        city = "";
        themeID = R.style.AppTheme;
        serverResultCode = 0;
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getTemperature() {
        return temperature + "°C";
    }

    void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public boolean isHumidityCB() {
        return isHumidityCB;
    }

    public void setHumidityCB(boolean humidityCB) {
        isHumidityCB = humidityCB;
    }

    public String getWind() {
        return wind;
    }

    void setWind(String wind) {
        this.wind = wind;
    }

    public boolean isWindCB() {
        return isWindCB;
    }

    public void setWindCB(boolean windCB) {
        isWindCB = windCB;
    }

    public String getBarometer() {
        return barometer;
    }

    void setBarometer(String barometer) {
        this.barometer = barometer;
    }

    public boolean isBarometerCB() {
        return isBarometerCB;
    }

    public void setBarometerCB(boolean barometerCB) {
        isBarometerCB = barometerCB;
    }

    public int getRadioCityID() {
        return radioCityID;
    }

    public void setRadioCityID(int radioCityID) {
        this.radioCityID = radioCityID;
    }

    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getServerResultCode() {
        return serverResultCode == CONFIRMATION_CODE;
    }

    void setServerResultCode(int resultCode) {
        this.serverResultCode = resultCode;
        notifyObservers();
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
