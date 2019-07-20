package com.shankaryadav.www.showweather;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

   SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;

    WeatherAdapter weatherAdapter;

    RequestQueue requestQueue;

    List<City_pojo> city_pojoList;


    List<String> list;


  //     https://api.openweathermap.org/data/2.5/forecast?q=jaipur&units=metric&appid=7303ac314aa85ff51a344cce76f48bae

    static final String URL = "https://api.openweathermap.org/data/2.5/forecast?q=";

    static  final String API = "&units=metric&appid=7303ac314aa85ff51a344cce76f48bae";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        swipeRefreshLayout= findViewById (R.id.swipe_container);


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        city_pojoList = new ArrayList<> ();

        list = Arrays.asList (getResources ().getStringArray (R.array.india_top_places));

        recyclerView = findViewById (R.id.recycler_for_city);
        recyclerView.setLayoutManager (new LinearLayoutManager (this));






                // Fetching data from server

               allcityWeather ();





    }

    public void allcityWeather(){

        swipeRefreshLayout.setRefreshing(true);
        for (int i = 0; i < list.size (); i++) {

            String url = URL + list.get (i) + API;
            loadurl (url);

            //

            if (list.size () == city_pojoList.size ()){
                swipeRefreshLayout.setRefreshing (false);
            }

        }
    }



    @Override
    public void onRefresh() {
        allcityWeather ();
    }




    public void loadurl(String url) {



        JsonObjectRequest req = new JsonObjectRequest (Request.Method.GET, url,
                null, new Response.Listener<JSONObject> () {

            @Override
            public void onResponse(JSONObject response) {

                getValue (response);
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
        swipeRefreshLayout.setRefreshing (false);
            }
        });

        requestQueue = Volley.newRequestQueue (this);
        requestQueue.add (req);

    }
        public void getValue (JSONObject response){


            JSONObject jsonObject = null;


            try {

                JSONArray jsonArray = response.getJSONArray ("list");

                    City_pojo city_pojo = new City_pojo ();





                    city_pojo.setCity_temp (jsonArray.getJSONObject (0).getJSONObject ("main").getString ("temp"));

                    city_pojo.setCity_name (response.getJSONObject ("city").getString ("name"));

                    city_pojo.setMain_cond (jsonArray.getJSONObject (0).getJSONArray ("weather").getJSONObject (0).getString ("main"));
              //  Toast.makeText (this, "hfhfhfh", Toast.LENGTH_SHORT).show ();

                    city_pojoList.add (city_pojo);

                } catch (JSONException e) {
                e.printStackTrace ();
            }


//
//



                recyclerView.setAdapter (new WeatherAdapter (city_pojoList, this));

            }



}
