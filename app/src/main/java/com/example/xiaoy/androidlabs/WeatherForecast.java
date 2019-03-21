package com.example.xiaoy.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WeatherForecast extends AppCompatActivity {
    public static final String ACTIVITY = "WeatherForecast";
    public static final int PROGRESSSPEED = 10;
    public static final String VALUE = "Temperature : ";
    public static final String MIN = "Temperature min: ";
    public static final String MAX = "Temperature max: ";
    public static final String UVRATING = "UV rating: ";

    private ProgressBar progressBar;
    private ImageView weatherImage;
    //private TextView textWind;
    private TextView textCurrentTemp;
    private TextView textMinTemp;
    private TextView textMaxTemp;
    private TextView textUVRating;
    Bitmap bmpCurrentWeather;
    Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute();

        progressBar = (ProgressBar)findViewById(R.id.progressbar_weather);
        progressBar.setVisibility(View.VISIBLE);  //show the progress bar

        weatherImage = (ImageView)findViewById(R.id.imgCurrentWeather);
        textCurrentTemp = (TextView) findViewById(R.id.TextView_current_temp);
        textMinTemp = findViewById(R.id.TextView_MinTemp);
        textMaxTemp = findViewById(R.id.TextView_MaxTemp);
        textUVRating = findViewById(R.id.TextView_UV_Rating);
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>{
        //lab 7 extension
        private String strUrlHead = "http://api.openweathermap.org/data/2.5/weather?q=";
        private String strCity = "ottawa";
        private String strCountry = "ca";
        private String strAPPID = "APPID=7e943c97096a9784391a981c4d878b22";
        private String strMode = "xml";
        private String strUrlUVRating = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
        private String ns = null;

        @Override
        protected String doInBackground(String... strings) {
            String strURLTemperature = strUrlHead + strCity + "," + strCountry + "&" +
                    strAPPID + "&mode="+strMode+"&units=metric";

            try (InputStream input = HTTPUtils.downloadUrl(strURLTemperature)) {
                weather = parse(input);
                Log.i("",weather.getWeatherURL());

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            } catch (InterruptedException | JSONException e) {
                e.printStackTrace();
            }
            return "Finished";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i(ACTIVITY, "update:" + values[0]);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            //set image bitmap here:
            weatherImage.setImageBitmap(bmpCurrentWeather);
            textCurrentTemp.setText(WeatherForecast.VALUE+": "+weather.getCurrentTemp());
            textMinTemp.setText(WeatherForecast.MIN + ":"+ weather.getMinTemp());
            textMaxTemp.setText(WeatherForecast.MAX + ":"+weather.getMaxTemp());
            textUVRating.setText(WeatherForecast.UVRATING + ":"+weather.getUVRating());
            progressBar.setVisibility(View.INVISIBLE);
        }

        public Weather parse(InputStream in) throws XmlPullParserException, IOException, InterruptedException, JSONException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                return readTemp(parser);
            } finally {
                in.close();
            }
        }

        private Weather readTemp(XmlPullParser parser) throws XmlPullParserException, IOException, InterruptedException, JSONException {
            Weather weather = null;

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the temperature tag
                if (name.equals(Weather.TEMPSTART)) {
                    weather = new Weather();
                    weather.setCurrentTemp(parser.getAttributeValue(ns, Weather.VALUE));
                    publishProgress(25);
                    Thread.sleep(800/PROGRESSSPEED);
                    weather.setMinTemp(parser.getAttributeValue(ns, Weather.MIN));
                    publishProgress(50);
                    Thread.sleep(500/PROGRESSSPEED);
                    weather.setMaxTemp(parser.getAttributeValue(ns, Weather.MAX));
                    publishProgress(75);
                    Thread.sleep(500/PROGRESSSPEED);

                }
                // get icon
                if (name.equals(Weather.WEATHER)){
                    weather.setWeather(parser.getAttributeValue(ns, Weather.WEATHERICON));
                    //
                    if (!fileExistance(weather.getWeatherFile())) {
                        bmpCurrentWeather = HTTPUtils.downloadBMP(weather.getWeatherURL());
                        saveBMP(weather.getWeather(), bmpCurrentWeather);
                        Log.i(ACTIVITY,"Downloading " + weather.getWeatherURL());
                    }else {
                        FileInputStream fis = null;
                        try {
                            fis = openFileInput(weather.getWeatherFile());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        bmpCurrentWeather = BitmapFactory.decodeStream(fis);
                        Log.i(ACTIVITY,"Find the weather image.");
                    }

                    // get UV Rating
                    weather.setUVRating(String.valueOf(HTTPUtils.getJSONDouble(strUrlUVRating, Weather.UVRATING)));

                    publishProgress(100);
                    Thread.sleep(500/PROGRESSSPEED);
                    break;
                }
            }
            return weather;
        }

        private boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }


        private void saveBMP(String iconName, Bitmap image) throws IOException {
            FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
        }
    }
}
