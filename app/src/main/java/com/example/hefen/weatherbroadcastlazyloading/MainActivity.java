package com.example.hefen.weatherbroadcastlazyloading;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewWeather;
    MyAdapter adapter;
    List<Weather> weatherList;
    String tag_json_obj = "json_obj_req";
    String url = "https://query.yahooapis.com/v1/public/yql?q=select+%2A+from+weather.forecast+where+woeid%3D1100661&format=json";
    int weather_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherList = new ArrayList<>();

        recyclerViewWeather = findViewById(R.id.recyclerView);
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerViewWeather.setHasFixedSize(true);

        fetch();

        Log.i("mylog", "list length " + weatherList.size());
        adapter = new MyAdapter(recyclerViewWeather,MainActivity.this, weatherList);
        recyclerViewWeather.setAdapter(adapter);

        adapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if (weatherList.size() < weather_num) {
                    //Log.i("mylog", "listsize " + weatherList.size() + " totalnum " + weather_num);
                    weatherList.add(null);
                    adapter.notifyItemInserted(weatherList.size() - 1);
                    Log.i("mylog", "notify insert");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            weatherList.remove(weatherList.size() - 1);
                            //adapter.notifyItemRemoved(weatherList.size());
                            //Log.i("mylog", "notify remove");
                            fetch();
                        }
                    }, 5000);
                } else {
                    Toast.makeText(MainActivity.this, "Load data completed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void fetch() {
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            parse(jsonObject);
                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                            Log.i("mylog", "notify set change");
                        }
                        catch(JSONException e) {
                            Toast.makeText(MainActivity.this, "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this, "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleyApplication.getInstance().getRequestQueue().add(request);
    }

    private void parse(JSONObject json) throws JSONException {
        //Log.i("mylog", json.toString());
        JSONObject jsonObjectQuery = json.getJSONObject("query");
        JSONObject jsonObjectResults = jsonObjectQuery.getJSONObject("results");
        //Log.i("mylog", jsonObjectResults.toString());
        JSONObject jsonObjectChannel = jsonObjectResults.getJSONObject("channel");
        //Log.i("mylog", jsonObjectChannel.toString());
        JSONObject jsonObjectItem = jsonObjectChannel.getJSONObject("item");
        //Log.i("mylog", jsonObjectItem.toString());
        JSONArray jsonArrayForecast = jsonObjectItem.getJSONArray("forecast");
        //Log.i("mylog", jsonArrayForecast.toString());
        //Log.i("mylog", "" + jsonArrayForecast.length());
        weather_num = jsonArrayForecast.length();
        int start = weatherList.size();
        int end = start + 4;
        end = end <= weather_num ? end : weather_num;
        //for(int i =0; i < jsonArrayForecast.length(); i++) {
        for(int i = start; i < end; i++) {
            JSONObject jsonObject = jsonArrayForecast.getJSONObject(i);
            //Log.i("mylog", jsonObject.toString());
            String code = jsonObject.getString("code");
            //Log.i("mylog", code);
            String date = jsonObject.getString("date");
            String day = jsonObject.getString("day");
            String heigh = jsonObject.getString("high");
            String low = jsonObject.getString("low");
            String text = jsonObject.getString("text");

            //Log.i("mylog", code + " " + date + " " + day);
            Weather weather = new Weather(code, date, day, heigh, low, text);
            weatherList.add(weather);
        }
        Log.i("mylog", "done parsing");
    }

}
