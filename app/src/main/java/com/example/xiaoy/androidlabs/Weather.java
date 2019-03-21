package com.example.xiaoy.androidlabs;

public class Weather {
    public static final String TEMPSTART = "temperature";
    public static final String VALUE = "value";
    public static final String MIN = "min";
    public static final String MAX = "max";
    public static final String WEATHER = "weather";
    public static final String WEATHERICON = "icon";
    public static final String UVRATING = "value";

    private String strCurrentTemp;
    private String strMinTemp;
    private String strMaxTemp;
    private String strUVRating;
    private String strWeather;
    private String strWeatherURL;

    public Weather(){

    }

    public Weather(String strCurrentTemp, String strMinTemp, String strMaxTemp){
        setCurrentTemp(strCurrentTemp);
        setMinTemp(strMinTemp);
        setMaxTemp(strMaxTemp);
    }

    public String getCurrentTemp() {
        return strCurrentTemp;
    }

    public void setCurrentTemp(String strCurrentTemp) {
        this.strCurrentTemp = strCurrentTemp;
    }

    public String getMinTemp() {
        return strMinTemp;
    }

    public void setMinTemp(String strMinTemp) {
        this.strMinTemp = strMinTemp;
    }

    public String getMaxTemp() {
        return strMaxTemp;
    }

    public void setMaxTemp(String strMaxTemp) {
        this.strMaxTemp = strMaxTemp;
    }

    public String getUVRating() {
        return strUVRating;
    }

    public void setUVRating(String strUVRating) {
        this.strUVRating = strUVRating;
    }

    public String getWeather() {
        return strWeather;
    }

    public String getWeatherFile() {
        return strWeather+".png";
    }

    public void setWeather(String strWeather) {
        this.strWeather = strWeather;
        setWeatherURL("http://openweathermap.org/img/w/" + strWeather + ".png");
    }

    public String getWeatherURL() {
        return strWeatherURL;
    }

    private void setWeatherURL(String strWeatherURL) {
        this.strWeatherURL = strWeatherURL;
    }
}
