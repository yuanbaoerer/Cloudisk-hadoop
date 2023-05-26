package cn.edu.shiep.entity;

import java.time.LocalDate;

/**
 * @Author yuanbao
 * @Date 2023/5/8
 * @Description
 */
public class WeatherDate {

    private LocalDate date;
    private String dayOfWeek;
    private String weather;
    private double temperature_lowest;
    private double temperature_highest;

    public WeatherDate() {
    }

    public WeatherDate(LocalDate date, String dayOfWeek, String weather, double temperature_lowest, double temperature_highest) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.weather = weather;
        this.temperature_lowest = temperature_lowest;
        this.temperature_highest = temperature_highest;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public double getTemperature_lowest() {
        return temperature_lowest;
    }

    public void setTemperature_lowest(double temperature_lowest) {
        this.temperature_lowest = temperature_lowest;
    }

    public double getTemperature_highest() {
        return temperature_highest;
    }

    public void setTemperature_highest(double temperature_highest) {
        this.temperature_highest = temperature_highest;
    }

    @Override
    public String toString() {
        return "WeatherDate{" +
                "date=" + date +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", weather='" + weather + '\'' +
                ", temperature_lowest=" + temperature_lowest +
                ", temperature_highest=" + temperature_highest +
                '}';
    }
}
