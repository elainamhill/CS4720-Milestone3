package com.example.elaina.magic8weather;

/**
 * Elaina Hill (emh2hb)
 *
 * Project Sources:
 * Used code snippets from https://www.youtube.com/watch?v=Y7JTkXoN8OE
 * and https://code.tutsplus.com/tutorials/create-a-weather-app-on-android--cms-21587
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView userwelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView magic8 = (ImageView) findViewById(R.id.magic8_imageView);
        magic8.setImageResource(R.drawable.magic8ball);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.weather_frag, new CurrentWeather())
                    .commit();
        }

        String ld = new LocalData(this).getName();
        new LocalData(this).setName(ld);

        userwelcome = (TextView) findViewById(R.id.user_welcome);
        userwelcome.setText("Hello " + ld);
        userwelcome.invalidate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.change_city){
            showInputDialog();
        }

        if(item.getItemId() == R.id.change_name){
            showInputDialog2();
        }
        return false;
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select City");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    private void showInputDialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Username");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeName(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city){
        CurrentWeather wf = (CurrentWeather)getSupportFragmentManager()
                .findFragmentById(R.id.weather_frag);
        wf.changeCity(city);
        new LocalData(this).setCity(city);


    }

    public void changeName(String name){
        CurrentWeather wf = (CurrentWeather)getSupportFragmentManager()
                .findFragmentById(R.id.weather_frag);
        userwelcome.setText(null);
        wf.changeName(name);
        new LocalData(this).setName(name);



    }

    public void getForecast(View v){
        startActivity(new Intent(MainActivity.this, Main2Activity.class));
    }

    public void getTomorrow(View v){
        startActivity(new Intent(MainActivity.this, Main3Activity.class));
    }

}
