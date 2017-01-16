package com.example.elaina.magic8weather;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Forecast5 extends Fragment {

    TextView humidity;
    TextView pressure;

    TextView cityField;
    TextView detailsField;
    TextView currentTemp;
    TextView minTemp;
    TextView maxTemp;

    Handler handler;

    public Forecast5() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main2, container, false);
        cityField = (TextView) rootView.findViewById(R.id.city_tag);
        detailsField = (TextView) rootView.findViewById(R.id.detail_weather);
        currentTemp = (TextView) rootView.findViewById(R.id.current_temp);
        minTemp = (TextView) rootView.findViewById(R.id.min_temp);
        maxTemp = (TextView) rootView.findViewById(R.id.max_temp);
        humidity = (TextView) rootView.findViewById(R.id.humidity_tag);
        pressure = (TextView) rootView.findViewById(R.id.pressure_tag);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWeatherData(new LocalData(getActivity()).getCity());
    }
    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = FetchForecast5.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){

        try {
            cityField.setText(json.getJSONObject("city").getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("city").getString("country"));

            JSONObject forecast1 = json.getJSONArray("list").getJSONObject(0);
            JSONObject details = forecast1.getJSONArray("weather").getJSONObject(0);
            JSONObject main = forecast1.getJSONObject("main");
            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US));

            currentTemp.setText( "Current Temperature: " +
                    String.format("%.2f", main.getDouble("temp"))+ " \u2109");

            minTemp.setText( "Minimum Temperature: " +
                    String.format("%.2f", main.getDouble("temp_min"))+ " \u2109");

            maxTemp.setText( "Maximum Temperature: " +
                    String.format("%.2f", main.getDouble("temp_max"))+ " \u2109");

            humidity.setText("Humidity: " + main.getString("humidity") + "%");

            pressure.setText("Pressure: " + main.getString("pressure") + " hPa");



        }catch(Exception e){
            Log.e("Magic8Weather", "Some field(s) not found in the JSON data");
        }
    }


    public void changeCity(String city){
        updateWeatherData(city);
    }

}
