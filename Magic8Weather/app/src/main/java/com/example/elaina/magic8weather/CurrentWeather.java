package com.example.elaina.magic8weather;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class CurrentWeather extends Fragment {

    Typeface weatherFont;
    TextView greeting;
    TextView userWelcome;

    TextView cityField;
    TextView updatedField;
    TextView currentTemp;
    TextView weatherIcon;


    Handler handler;

    public CurrentWeather() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        cityField = (TextView) rootView.findViewById(R.id.city_field);
        updatedField = (TextView) rootView.findViewById(R.id.last_updated);
        currentTemp = (TextView) rootView.findViewById(R.id.temp_field);
        weatherIcon = (TextView) rootView.findViewById(R.id.weather_icon);
        greeting = (TextView) rootView.findViewById(R.id.welcome_tag);
        userWelcome = (TextView) rootView.findViewById(R.id.user_welcome);

        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        updateWeatherData(new LocalData(getActivity()).getCity());


    }
    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = FetchCurrent.getJSON(getActivity(), city);
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
            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            currentTemp.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " \u2109");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("Magic8Weather", "Some field(s) not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
                greeting.setText("The near future looks pretty sunny!");
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
                greeting.setText("I think you'll see a clear night tonight.");
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    greeting.setText("I predict things to be a little stormy today.");
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    greeting.setText("You may be in for some light rain.");
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    greeting.setText("Is it just me or does it seem foggy today?");
                    //greeting.setTextColor(Color.YELLOW);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    greeting.setText("Things are looking cloudy today.");
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    greeting.setText("Grab your coat, you might see snow in the area.");
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    greeting.setText("Don't forget your umbrella. I'm sensing rain today.");
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    public void changeCity(String city){
        updateWeatherData(city);
    }

    public void changeName(String name) {
        userWelcome.setText(null);
        userWelcome.setText("Hello, " + name + ".");
//        userWelcome.invalidate();
    }
}
