package com.shankaryadav.www.showweather;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> implements Filterable {




    List<City_pojo> list;
    List<City_pojo> filteredlist;

    Context context;

    private WeatherAdapterListener listener;

    public WeatherAdapter(List<City_pojo> list, Context context, WeatherAdapterListener listener,RecyclerView recyclerView) {

        this.list = list;
        this.context = context;
        this.listener = listener;
        this.filteredlist = list;



    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {




            LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());

            View v = inflater.inflate (R.layout.item_for_city,viewGroup,false);


        return new WeatherViewHolder (v);

    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int i) {





            String centchar = filteredlist.get (i).getCity_temp ()+"°C";
            ((WeatherViewHolder)   holder).temp.setText (centchar);


            ((WeatherViewHolder) holder).city.setText (filteredlist.get (i).getCity_name ().toUpperCase ());
            ((WeatherViewHolder )  holder).cond.setText (filteredlist.get (i).getMain_cond ().toUpperCase ());
            //   weatherViewHolder.temp.setText (centchar);


            Glide.with (context).load (R.drawable.chandigarh).centerCrop ().into (((WeatherViewHolder)holder).back);



    }




    @Override
    public int getItemCount() {
        return filteredlist.size ();
    }



    @Override
    public Filter getFilter() {
        return new Filter () {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString ();

                if (charString.isEmpty ()){
                    filteredlist = list;
                }else{
                    List<City_pojo> newfilterList = new ArrayList<> ();
                    for (City_pojo city_pojo : list){
                        if (city_pojo.getCity_name ().toLowerCase ().contains (charString.toLowerCase ())){
                            newfilterList.add (city_pojo);
                        }
                    }
                    filteredlist = newfilterList;
                }

                FilterResults filterResults = new FilterResults ();
                filterResults.values = filteredlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredlist = (ArrayList<City_pojo>) results.values;
                notifyDataSetChanged ();

            }
        };
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

//            itemView.setOnClickListener (new View.OnClickListener () {
//                @Override
//                public void onClick(View v) {
//
//                    int pos = getAdapterPosition ();
//
//                    Bundle bundle = new Bundle ();
//
//                    bundle.putString ("cond",list.get (pos).getMain_cond ().toUpperCase ());
//                    bundle.putString ("temp",list.get (pos).getCity_temp ()+"°C");
//                    bundle.putString ("city",list.get (pos).getCity_name ().toUpperCase ());
//
//                    Intent intent = new Intent (context.getApplicationContext (),Weather_Detail.class);
//                    intent.putExtras (bundle);
//                    context.startActivity (intent);
//                }
//            });
    }




    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public interface WeatherAdapterListener{
        void onContactSelected(City_pojo city_pojo);
    }

}
