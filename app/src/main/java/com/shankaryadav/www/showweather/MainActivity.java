package com.shankaryadav.www.showweather;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchQueryChangeListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener;
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MainActivity extends AppCompatActivity implements WeatherAdapter.WeatherAdapterListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    RecyclerView recyclerView;

    WeatherAdapter weatherAdapter;

    RequestQueue requestQueue;

    List<City_pojo> city_pojoList;

    ProgressBar progressBar;

    List<String> list;

    PersistentSearchView persistentSearchView;


    private TextView tvEmptyView;

    protected Handler handler;


//    private SearchView searchView;

    // to check if we are connected to Network
    boolean isConnected = true;

    // to check if we are monitoring Network
    private boolean monitoringConnectivity = false;

    private ConnectivityManager.NetworkCallback connectivityCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;
            Toast.makeText (MainActivity.this, "INTERNET CONNECTED", Toast.LENGTH_SHORT).show ();
        }

        @Override
        public void onLost(Network network) {
            isConnected = false;
            Toast.makeText (MainActivity.this, "INTERNET LOST", Toast.LENGTH_SHORT).show ();
        }
    };



  //     https://api.openweathermap.org/data/2.5/forecast?q=jaipur&units=metric&appid=7303ac314aa85ff51a344cce76f48bae

    static final String URL = "https://api.openweathermap.org/data/2.5/forecast?q=";

    static final String BASE_UNSPLASH_URL = "https://api.unsplash.com/photos/?client_id=";

    static  final String API = "&units=metric&appid=7303ac314aa85ff51a344cce76f48bae";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        getSupportActionBar().setTitle(R.string.toolbar_title);

        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        recyclerView = findViewById (R.id.recycler_for_city);


        handler = new Handler();

        setPersistenceSearchview();

        city_pojoList = new ArrayList<> ();

        list = Arrays.asList (getResources ().getStringArray (R.array.india_top_places));


        loadData ();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager (new LinearLayoutManager (this));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);

        recyclerView.setItemAnimator(itemAnimator);

        weatherAdapter = new WeatherAdapter (city_pojoList, this, this,recyclerView);
        whiteNotificationBar(recyclerView);
        recyclerView.setAdapter (weatherAdapter);



//        if (city_pojoList.isEmpty ()){
//            recyclerView.setVisibility (View.GONE);
//            tvEmptyView.setVisibility (View.VISIBLE);
//        }else {
//            recyclerView.setVisibility (View.VISIBLE);
//            tvEmptyView.setVisibility (View.GONE);
//        }


    }

    // load initial data
    private void loadData() {

        for (int i = 0; i < list.size (); i++) {
            String url = URL + list.get (i) + API;
            loadurl (url);

        }



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


//            Log.i ("TAG----","--------///// "+ city_pojoList.get (0).getCity_name ());


        } catch (JSONException e) {
            e.printStackTrace ();
        }

    }



    private void setPersistenceSearchview() {


        persistentSearchView = findViewById (R.id.persistentSearchView);

        persistentSearchView.setOnLeftBtnClickListener(new View.OnClickListener () {

            @Override
            public void onClick(View view) {
                // Handle the left button click
            }

        });

        persistentSearchView.setOnClearInputBtnClickListener(new View.OnClickListener () {

            @Override
            public void onClick(View view) {
                // Handle the clear input button click
            }

        });

        // Setting a delegate for the voice recognition input
        persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate (this));

        persistentSearchView.setOnSearchConfirmedListener(new OnSearchConfirmedListener () {

            @Override
            public void onSearchConfirmed(PersistentSearchView searchView, String query) {
                // Handle a search confirmation. This is the place where you'd
                // want to perform a search against your data provider.
            }

        });

        // Disabling the suggestions since they are unused in
        // the simple implementation
        persistentSearchView.setOnSearchQueryChangeListener(new OnSearchQueryChangeListener () {

            @Override
            public void onSearchQueryChanged(PersistentSearchView searchView, String oldQuery, String newQuery) {
                // Handle a search query change. This is the place where you'd
                // want load new suggestions based on the newQuery parameter.

                if (weatherAdapter != null) weatherAdapter.getFilter ().filter (newQuery);


            }

        });

        persistentSearchView.setOnSuggestionChangeListener(new OnSuggestionChangeListener () {

            @Override
            public void onSuggestionPicked(SuggestionItem suggestion) {
                // Handle a suggestion pick event. This is the place where you'd
                // want to perform a search against your data provider.


            }

            @Override
            public void onSuggestionRemoved(SuggestionItem suggestion) {
                // Handle a suggestion remove event. This is the place where
                // you'd want to remove the suggestion from your data provider.


            }

        });

    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater ().inflate (R.menu.menu,menu);
//
//        SearchManager searchManager = (SearchManager) getSystemService (Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem (R.id.action_search)
//                .getActionView ();
//        searchView.setSearchableInfo (searchManager
//        .getSearchableInfo (getComponentName ()));
//
//        searchView.setMaxWidth (Integer.MAX_VALUE);
//
//        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener () {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
////               if (weatherAdapter != null) weatherAdapter.getFilter ().filter (query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////               if (weatherAdapter != null) weatherAdapter.getFilter ().filter (newText);
//                return true;
//            }
//        });
//
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId ();
//
//        if (id == R.id.action_search){
//            return  true;
//        }
//
//        return super.onOptionsItemSelected (item);
//    }

    @Override
    public void onContactSelected(City_pojo city_pojo) {
        Toast.makeText (this, "hello world", Toast.LENGTH_SHORT).show ();
    }

//
//    @Override
//    public void onBackPressed() {
//        // close search view on back button pressed
//        if (!searchView.isIconified()) {
//            searchView.setIconified(true);
//            return;
//        }
//
//        super.onBackPressed();
//
//    }
//

    private void whiteNotificationBar(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }

    }

//    private boolean checkInternetConnection(){
//        ConnectivityManager cm =
//                (ConnectivityManager)getApplicationContext ().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
//
//        return isConnected;
//    }


    // Method to check network connectivity in Main Activity
    private void checkConnectivity() {
        // here we are getting the connectivity service from connectivity manager
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);

        // Getting network Info
        // give Network Access Permission in Manifest
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // isConnected is a boolean variable
        // here we check if network is connected or is getting connected
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (!isConnected) {
            // SHOW ANY ACTION YOU WANT TO SHOW
            // WHEN WE ARE NOT CONNECTED TO INTERNET/NETWORK
            Toast.makeText (MainActivity.this, " NO NETWORK!", Toast.LENGTH_SHORT).show ();


// if Network is not connected we will register a network callback to  monitor network
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(), connectivityCallback);
            monitoringConnectivity = true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnectivity();




        List<String> searchQueries = null;

        // Fetching the search queries from the data provider
//        if(persistentSearchView.isInputQueryEmpty) {
//            searchQueries = ad.getInitialSearchQueries();
//        } else {
//            searchQueries = mDataProvider.getSuggestionsForQuery(persistentSearchView.inputQuery);
//        }

        // Converting them to recent suggestions and setting them to the widget
//        persistentSearchView.setSuggestions(SuggestionCreationUtil.asRecentSearchSuggestions(searchQueries), false);
    }


    @Override
    protected void onPause() {
        // if network is being moniterd then we will unregister the network callback
        if (monitoringConnectivity) {
            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(connectivityCallback);
            monitoringConnectivity = false;
        }
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);

        // Calling the voice recognition delegate to properly handle voice input results
        VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data);
    }
}
