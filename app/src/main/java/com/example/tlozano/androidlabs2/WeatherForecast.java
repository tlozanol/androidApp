package com.example.tlozano.androidlabs2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.asus.androidlabs.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends Activity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";
    ProgressBar loadingBar;
    TextView current;
    TextView min;
    TextView max;
    ImageView weatherpic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "-- open weather forecast");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        Log.i(ACTIVITY_NAME, "-- Instatiate variables to activity");
        loadingBar = (ProgressBar) findViewById(R.id.progressWeather);
        current = (TextView) findViewById(R.id.current_temp);
        min = (TextView) findViewById(R.id.min_temp);
        max = (TextView) findViewById(R.id.max_temp);
        weatherpic = (ImageView) findViewById(R.id.weather);

        ForecastQuery fquery = new ForecastQuery();
        String URLLink = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
        fquery.execute(URLLink);

    }

    private Bitmap getImage(URL url){
        HttpURLConnection connection = null;

        Log.i(ACTIVITY_NAME, "-- getting image from the internet");
        try{
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200){
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else {
                return null;
            }
        } catch (Exception e){
            return null;
        } finally{
            if (connection != null){
                connection.disconnect();
            }
        }
    }

    private boolean fileExistance(String fName){
        Log.i(ACTIVITY_NAME, "-- Confirm that file exist");
        File file = getBaseContext().getFileStreamPath(fName);
        return file.exists();
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        public String minTemp;
        public String maxTemp;
        public String currentTemp;
        public String iconName;
        public Bitmap pictureW;

        URL url;
        HttpURLConnection urlConnection;
        InputStream connection;
        XmlPullParserFactory parser;
        XmlPullParser pullParser;


        @Override
        protected String doInBackground(String... strings) {
            Log.i(ACTIVITY_NAME, "-- in doInBackground");

            try{
                Log.i(ACTIVITY_NAME, "-- Test1");
                url = new URL(strings[0]);
                Log.i(ACTIVITY_NAME, "-- Test2");
                urlConnection = (HttpURLConnection) url.openConnection();
                /*urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);*/
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                Log.i(ACTIVITY_NAME, "-- Test3");
                connection = urlConnection.getInputStream();

                Log.i(ACTIVITY_NAME, "-- Creating data to load");
                parser = XmlPullParserFactory.newInstance();
                parser.setNamespaceAware(false);
                pullParser = parser.newPullParser();
                pullParser.setInput(connection, "UTF-8");



                while(pullParser.next() != XmlPullParser.END_DOCUMENT){

                    if (pullParser.getEventType() != XmlPullParser.START_TAG){
                        continue;
                    }

                    Log.i(ACTIVITY_NAME, "-- Loading data from XML");
                    if (pullParser.getName().equals("temperature")){
                        Log.i(ACTIVITY_NAME, "-- Loading value");
                        currentTemp = pullParser.getAttributeValue(null, "value");

                        publishProgress(25);
                        Log.i(ACTIVITY_NAME, "-- Loading min");
                        minTemp = pullParser.getAttributeValue(null, "min");

                        publishProgress(50);
                        Log.i(ACTIVITY_NAME, "-- Loading max");
                        maxTemp = pullParser.getAttributeValue(null, "max");

                        publishProgress(75);
                        Log.i(ACTIVITY_NAME, "-- Finished Loading data from XML");
                    }

                    if (pullParser.getName().equals("weather")){
                        iconName = pullParser.getAttributeValue(null, "icon");
                        String picFile = iconName + ".png";

                        if(fileExistance(picFile)){
                            FileInputStream input = null;
                            try {
                                input = new FileInputStream(getBaseContext().getFileStreamPath(picFile));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pictureW = BitmapFactory.decodeStream(input);

                        } else{
                            URL picURL = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                            pictureW = getImage(picURL);
                            FileOutputStream outStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            pictureW.compress(Bitmap.CompressFormat.PNG, 80, outStream);
                            outStream.flush();
                            outStream.close();
                            Log.i(ACTIVITY_NAME, "-- Image loaded");
                        }
                        publishProgress(100);
                    }
                }

            } catch (IOException e){
                e.printStackTrace();
            } catch (XmlPullParserException d){
                Log.i(ACTIVITY_NAME, "-- Pull Parser Exception");
            }

            return null;
        }

        protected void onProgressUpdate(Integer ...value){
            Log.i(ACTIVITY_NAME, "-- in onProgressUpdate");
            loadingBar.setVisibility(View.VISIBLE);
            loadingBar.setProgress(value[0]);
        }

        protected void onPostExecute(String result){
            Log.i(ACTIVITY_NAME, "-- in onPostExecute");

            String degree = Character.toString((char) 0x00B0);
            current.setText(current.getText() + currentTemp + degree + "C");
            min.setText(min.getText() + minTemp + degree + "C");
            max.setText(max.getText() + maxTemp + degree + "C");

            weatherpic.setImageBitmap(pictureW);

            loadingBar.setVisibility(View.INVISIBLE);
        }
    }
}
