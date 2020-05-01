package com.example.weather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btn_loc;
    FusedLocationProviderClient client;
    LocationRequest request;
    LocationCallback callback;
    Location location;
    TextView  pressure, humidity,temperature,address,status, datetime;
//    Calendar calendar;
//    SimpleDateFormat dateFormat;
//    int date;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        windy = findViewById(R.id.wind);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        temperature = findViewById(R.id.temp);
        address = findViewById(R.id.address);
        status = findViewById(R.id.status);
        datetime=findViewById(R.id.updated_at);
        btn_loc = findViewById(R.id.btn_other_loc);



        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        request=new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(4000);
        request.setFastestInterval(2000);
        callback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location=locationResult.getLocations().get(0);
                Log.i("BHA",location.getLatitude() + " , " + location.getLongitude());
                Double lat = location.getLatitude();
                Double longi = location.getLongitude();
                convertToAddress(location);
                display(lat,longi);
                super.onLocationResult(locationResult);
            }
        };
        client.requestLocationUpdates(request,callback, Looper.myLooper());
        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Other_loc.class);
                startActivity(i);
                //finish();
            }
        });

    }

   private void display(Double lat, Double longi) {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+longi+"&appid=cd3addf289f64a7bb97ef91a7d140467";
        Log.i("BHA",url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject main = json.getJSONObject("main");
                    //JSONObject wind = json.getJSONObject("wind");
                    //JSONObject weather = json.getJSONObject("weather");
                    Float tempi = Float.parseFloat(main.getString("temp"));
                    String temp = String.valueOf(Math.round((tempi - 273.15) * 10) / 10.0);
                    Log.i("BHAV",temp);
                    String press = main.getString("pressure");
                    String hum = main.getString("humidity");
                   // String winds = wind.getString("speed");
                    //String weath = weather.getString("main");
                    temperature.setText(temp + "°C");
                    pressure.setText(press);
                    humidity.setText(hum);
//                    windy.setText(winds);
                    //status.setText(weath);
                   // calendar = Calendar.getInstance();
                 //   date = Calendar.DATE;
                 //   datetime.setText(date);
                    Long updatedAt = json.getLong("dt");
                    String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                    datetime.setText(updatedAtText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


  private void convertToAddress(Location location) {
      Geocoder geocoder = new Geocoder(getApplicationContext());
      List<Address> lAds = null;
      try {
          lAds = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
          Address ad = lAds.get(0);
          Log.i("BHA",ad.getSubLocality());
          address.setText(ad.getSubLocality());

      } catch (IOException e) {
          e.printStackTrace();
      }

  }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Warning")
                .setMessage("Do you Wish to exit?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("NO",null)
                .show();
    }

}
