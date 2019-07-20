package com.shankaryadav.www.showweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Weather_Detail extends AppCompatActivity {

ImageView bck;
TextView city;
String citystr;
TextView cond;
String condstr;
TextView temp;
String tempstr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_weather__detail);

        bck = findViewById (R.id.back_image);
        city = findViewById (R.id.city_name);
        cond = findViewById (R.id.main_cond);
        temp = findViewById (R.id.city_temp);


        Bundle bundle = getIntent ().getExtras ();

        citystr = bundle.getString ("city");
        condstr = bundle.getString ("cond");
        tempstr = bundle.getString ("temp");

        city.setText (citystr);
        cond.setText (condstr);
        temp.setText (tempstr);



    }
}
