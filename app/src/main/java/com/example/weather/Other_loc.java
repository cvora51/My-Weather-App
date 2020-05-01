package com.example.weather;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;
public class Other_loc extends AppCompatActivity {
    EditText edt_city;
    Button btn_weather;
    TextView txt,pressure, humidity,temperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_loc);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        temperature = findViewById(R.id.temp);
        edt_city=findViewById(R.id.city);
        btn_weather=findViewById(R.id.weather_btn);
//        txt=findViewById(R.id.tv);

        btn_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = edt_city.getText().toString().trim();
                if(s1.equals(""))
                    edt_city.setError("Please enter a city");
                else
                    getWeather(s1);
            }
        });
    }

    private void getWeather(String s1) {
        String URL = "https://api.openweathermap.org/data/2.5/weather?q="+s1+"&units=metric&appid=3834ceb018829755f7d8a3f66f9a089a";
        StringRequest request =new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CHIN",response);
                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject main = json.getJSONObject("main");
                    Float tempi = Float.parseFloat(main.getString("temp"));
//                    String temp = String.valueOf(Math.round((tempi - 273.15) * 10) / 10.0);
//                    String temp = main.getString("temperature");
                    String temp = main.getString("temp") ;
                    Log.i("BHAV",temp);
                    String press = main.getString("pressure");
                    String hum = main.getString("humidity");
                    // String winds = wind.getString("speed");
                    //String weath = weather.getString("main");
                    temperature.setText(temp + "°C");
                    pressure.setText(press);
                    humidity.setText(hum);
//                    String temp = main.getString("temp");
//                    String humidity = main.getString("humidity");
//                    String pressure = main.getString("pressure");

//                    txt.setText("TEMP: " + temperature + "\nHUMIDITY: " + humidity +"\nPRESSURE: " + pressure);
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


}
