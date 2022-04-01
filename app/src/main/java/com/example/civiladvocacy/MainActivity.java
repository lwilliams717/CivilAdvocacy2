package com.example.civiladvocacy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.IDNA;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static ArrayList<Official> officials = new ArrayList<>();
    static RecyclerView officals_recycler;
    private String api_key = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyBcp2fjH0dBwddUaaF5bFx0bxMp0ZnBa0c&address=";
    private static String address = "1263%20Pacific%20Ave.%20Kansas%20City%20";
    private String state = "KS";
    private String API_link = "";
    private boolean network = true;
    public boolean locErr = false;

    static TextView titlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the main activity layout recycler
        officals_recycler = findViewById(R.id.main_recycler);
        titlebar = findViewById(R.id.main_titlebar);
        titlebar.setMovementMethod(new ScrollingMovementMethod());

        //sets up the recycler
        loadRecycler();
        setTitle(getString(R.string.enter_loc));
        officials.clear();

        //makes sure the user has a network connection
        network = hasNetworkConnection();
        if (!network){
            setTitle(getString(R.string.no_network));
        }

    }

    //method to check network connection
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    //inflates the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //when they click on an item in the main menu actionbar
    public void onClickMainMenuItem(MenuItem i){
        if(network){
            if(i.getItemId() == R.id.about_menu) {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
            }
            else if (i.getItemId() == R.id.loaction_menu) {
                startLocationDialog();
            }
        }
        else {
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        }

    }


    public void startLocationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et1 = new EditText(this);
        et1.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setMessage( getString(R.string.new_loc_dialog) );
        builder.setView(et1);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //need to verify that the user enters a valid city name
                String ans = et1.getText().toString();

                if(ans.isEmpty() || ans == null){
                    Toast.makeText(MainActivity.this, R.string.invalid, Toast.LENGTH_SHORT).show();
                }
                else {
                    //clear the array before trying to load in more information
                    officials.clear();
                    //!!!!!!!!!!!!!!
                    //this is where you have to add the location! call JSON object method
                    buildURL(ans);
                    OfficialDownloader.downloadOfficials(MainActivity.this , API_link);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //this onclick is if they click an item in the recycler
    @Override
    public void onClick(View view) {
        //!!!!!!!!!!!!!!!
        //just to check that it works. this will need to load the official's information into the INFO ACTIVITY!!
        //Toast.makeText(this, "on click works", Toast.LENGTH_SHORT).show();

        //grabs the view that was clicked
        int index = officals_recycler.getChildLayoutPosition(view);

        //sets the current info activity to the item that was clicked
        InfoActivity.setCurrentOfficial( officials.get(index) );

        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }


    //builds the API URL
    public void buildURL(String a) {
        address = a;
        //remember to replace spaces in address with %20
        address = address.replace(" ", "%20");
        //changes the state name to the inital if not used
        changeStateToInitial(address);
        //make sure to replace the North with N
        address = address.replace("North", "N");
        //make sure to replace the South with S
        address = address.replace("South", "S");
        //make sure to replace the West with W
        address = address.replace("West", "W");
        //make sure to replace the East with E
        address = address.replace("East", "E");

        API_link = api_key + address;
    }

    public void addOfficalArray(Official o){
        officials.add(o);
    }

    public void changeStateToInitial(String s){
        String [] states = {"Alabama", "Alaska", "Alberta", "American Samoa", "Arizona", "Arkansas", "Armed Forces AE", "Armed Forces Americas", "Armed Forces Pacific", "British Columbia", "California", "Colorado", "Connecticut", "Delaware", "District Of Columbia", "Florida", "Georgia", "Guam", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Manitoba", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Brunswick", "New Hampshire", "New Jersey", "New Mexico", "New York", "Newfoundland", "North Carolina", "North Dakota", "Northwest Territories", "Nova Scotia", "Nunavut", "Ohio", "Oklahoma", "Ontario", "Oregon", "Pennsylvania", "Prince Edward Island", "Puerto Rico", "Quebec", "Rhode Island", "Saskatchewan", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virgin Islands", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming", "Yukon Territory"};
        String [] initials = {"AL", "AK", "AB", "AS", "AZ", "AR", "AE", "AA", "AP", "BC", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "GU", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MB", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NB", "NH", "NJ", "NM", "NY", "NF", "NC", "ND", "NT", "NS", "NU", "OH", "OK", "ON", "OR", "PA", "PE", "PR", "QC", "RI", "SK", "SC", "SD", "TN", "TX", "UT", "VT", "VI", "VA", "WA", "WV", "WI", "WY", "YT"};

        //checks if the address has a state that matches one of the keys and replaces it with an appropriate initial
        for(int i= 0; i < states.length; i++) {
            if(s.equals(states[i])){
                s = s.replace(states[i], initials[i]);
            }
        }

    }

    //set title of the activity to the address pulled from the JSON
    public void setTitle(String s){
        titlebar.setText(s);
    }

    //helps loading or reloading the recycler
    public void loadRecycler(){
        officals_recycler = findViewById(R.id.main_recycler);
        OfficialAdapter officialAdapter = new OfficialAdapter(officials, this);
        officals_recycler.setAdapter(officialAdapter);
        officals_recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    //gets the address
    public static String getAddress(){
        return titlebar.getText().toString();
    }

    //this function gets called whenever the volley request throws an error, meaning the user entered a location
    //that could not be called with the API
    public void locationError(){
        //sets the flag variable to let the main activity know that the location was incorrect
        locErr = true;
        //inform the user the address was invalid
        Toast.makeText( this, getString(R.string.invalid), Toast.LENGTH_LONG).show();
    }


}