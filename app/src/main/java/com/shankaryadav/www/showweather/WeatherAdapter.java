package com.shankaryadav.www.showweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    List<City_pojo> list;

    Context context;

    public WeatherAdapter(List<City_pojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());

        View v = inflater.inflate (R.layout.item_for_city,viewGroup,false);

        return new WeatherViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder weatherViewHolder, int i) {





        String centchar = list.get (i).getCity_temp ()+"°C";
        weatherViewHolder.temp.setText (centchar);

        weatherViewHolder.city.setText (list.get (i).getCity_name ().toUpperCase ());
        weatherViewHolder.cond.setText (list.get (i).getMain_cond ().toUpperCase ());
     //   weatherViewHolder.temp.setText (centchar);



    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class WeatherViewHolder extends  RecyclerView.ViewHolder{

        ImageView back;
        TextView cond,city,temp;

        public WeatherViewHolder(@NonNull View itemView) {
            super (itemView);

            back = itemView.findViewById (R.id.city_back);
            cond = itemView.findViewById (R.id.text_cond);
            city = itemView.findViewById (R.id.text_city_name);
            temp = itemView.findViewById (R.id.text_city_temp);

            itemView.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition ();

                    Bundle bundle = new Bundle ();

                    bundle.putString ("cond",list.get (pos).getMain_cond ().toUpperCase ());
                    bundle.putString ("temp",list.get (pos).getCity_temp ()+"°C");
                    bundle.putString ("city",list.get (pos).getCity_name ().toUpperCase ());

                    Intent intent = new Intent (context.getApplicationContext (),Weather_Detail.class);
                    intent.putExtras (bundle);
                    context.startActivity (intent);
                }
            });
        }
    }
}
