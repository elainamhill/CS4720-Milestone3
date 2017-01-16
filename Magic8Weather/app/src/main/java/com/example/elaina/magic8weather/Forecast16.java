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

public class Forecast16 extends Fragment {

    TextView humidity;
    TextView pressure;

    TextView header;
    TextView cityField;
    TextView detailsField;
    TextView dayTemp;
    TextView eveTemp;
    TextView nightTemp;

    Handler handler;

    public Forecast16() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main3, container, false);
        cityField = (TextView) rootView.findViewById(R.id.city_box);
        detailsField = (TextView) rootView.findViewById(R.id.description_tag);
        dayTemp = (TextView) rootView.findViewById(R.id.day_temp);
        eveTemp = (TextView) rootView.findViewById(R.id.eve_temp);
        nightTemp = (TextView) rootView.findViewById(R.id.night_temp);
        header = (TextView) rootView.findViewById(R.id.forecast16_tag);
//        humidity = (TextView) rootView.findViewById(R.id.humidity_tag);
//        pressure = (TextView) rootView.findViewById(R.id.pressure_tag);

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
                final JSONObject json = FetchForecast16.getJSON(getActivity(), city);
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

            JSONObject forecast1 = json.getJSONArray("list").getJSONObject(1);
            JSONObject details = forecast1.getJSONArray("weather").getJSONObject(0);
            JSONObject temp = forecast1.getJSONObject("temp");
            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US));

            dayTemp.setText( "Day Temperature: " +
                    String.format("%.2f", temp.getDouble("day"))+ " \u2109");

            eveTemp.setText( "Evening Temperature: " +
                    String.format("%.2f", temp.getDouble("eve"))+ " \u2109");

            nightTemp.setText( "Night Temperature: " +
                    String.format("%.2f", temp.getDouble("night"))+ " \u2109");

            header.setText("Here's my best guess as to what the future holds for tomorrow...");

//            humidity.setText("Humidity: " + main.getString("humidity") + "%");
//
//            pressure.setText("Pressure: " + main.getString("pressure") + " hPa");



        }catch(Exception e){
            Log.e("Magic8Weather", "Some field(s) not found in the JSON data");
        }
    }


    public void changeCity(String city){
        updateWeatherData(city);
    }

}
