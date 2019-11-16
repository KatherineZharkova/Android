package ru.cocovella.WeatherApp.Model;

public interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();

}
