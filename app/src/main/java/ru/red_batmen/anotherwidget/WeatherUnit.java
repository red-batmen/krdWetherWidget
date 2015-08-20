package ru.red_batmen.anotherwidget;

/**
 * Created by red on 09.07.15.
 */
public class WeatherUnit {

    private String nowWeatherImage;
    private String nowWeatherHumidity;
    private String nowWeatherDegree;
    private String nowWeatherSign = "+";

    private String tomorrowWeatherImage;
    private String tomorrowWeatherHumidity;
    private String tomorrowWeatherDegree;
    private String tomorrowWeatherSign = "+";

    private String afterTomorrowWeatherImage;
    private String afterTomorrowWeatherHumidity;
    private String afterTomorrowWeatherDegree;
    private String afterTomorrowWeatherSign = "+";

    public boolean isParsed() {
        return isParsed;
    }

    public void setIsParsed(boolean isParsed) {
        this.isParsed = isParsed;
    }

    private  boolean isParsed = false;

    public String getAfterTomorrowWeatherSign() {
        return afterTomorrowWeatherSign;
    }

    public void setAfterTomorrowWeatherSign(String afterTomorrowWeatherSign) {
        this.afterTomorrowWeatherSign = afterTomorrowWeatherSign;
    }

    public String getTomorrowWeatherSign() {
        return tomorrowWeatherSign;
    }

    public void setTomorrowWeatherSign(String tomorrowWeatherSign) {
        this.tomorrowWeatherSign = tomorrowWeatherSign;
    }

    public String getTomorrowWeatherImage() {
        return tomorrowWeatherImage;
    }

    public void setTomorrowWeatherImage(String tomorrowWeatherImage) {
        this.tomorrowWeatherImage = tomorrowWeatherImage;
    }

    public String getTomorrowWeatherHumidity() {
        return tomorrowWeatherHumidity;
    }

    public void setTomorrowWeatherHumidity(String tomorrowWeatherHumidity) {
        this.tomorrowWeatherHumidity = tomorrowWeatherHumidity;
    }

    public String getTomorrowWeatherDegree() {
        return tomorrowWeatherDegree;
    }

    public void setTomorrowWeatherDegree(String tomorrowWeatherDegree) {
        this.tomorrowWeatherDegree = tomorrowWeatherDegree;
    }

    public String getAfterTomorrowWeatherImage() {
        return afterTomorrowWeatherImage;
    }

    public void setAfterTomorrowWeatherImage(String afterTomorrowWeatherImage) {
        this.afterTomorrowWeatherImage = afterTomorrowWeatherImage;
    }

    public String getAfterTomorrowWeatherHumidity() {
        return afterTomorrowWeatherHumidity;
    }

    public void setAfterTomorrowWeatherHumidity(String afterTomorrowWeatherHumidity) {
        this.afterTomorrowWeatherHumidity = afterTomorrowWeatherHumidity;
    }

    public String getAfterTomorrowWeatherDegree() {
        return afterTomorrowWeatherDegree;
    }

    public void setAfterTomorrowWeatherDegree(String afterTomorrowWeatherDegree) {
        this.afterTomorrowWeatherDegree = afterTomorrowWeatherDegree;
    }

    public String getNowWeatherSign() {
        return nowWeatherSign;
    }

    public void setNowWeatherSign(String weatherSign) {
        this.nowWeatherSign = weatherSign;
    }

    public String getNowWeatherImage() {
        return nowWeatherImage;
    }

    public void setNowWeatherImage(String weatherImage) {
        this.nowWeatherImage = weatherImage;
    }

    public String getNowWeatherHumidity() {
        return nowWeatherHumidity;
    }

    public void setNowWeatherHumidity(String weatherHumidity) {
        this.nowWeatherHumidity = weatherHumidity;
    }

    public String getNowWeatherDegree() {
        return nowWeatherDegree;
    }

    public String getNowWeatherDegreeUnsigned() {
        return nowWeatherDegree.replace("-", "");
    }

    public String getTomorrowWeatherDegreeUnsigned() {
        return tomorrowWeatherDegree.replace("-", "");
    }

    public String getAfterTomorrowWeatherDegreeUnsigned() {
        return afterTomorrowWeatherDegree.replace("-", "");
    }

    public void setNowWeatherDegree(String weatherDegree) {
        this.nowWeatherDegree = weatherDegree;
    }
}
