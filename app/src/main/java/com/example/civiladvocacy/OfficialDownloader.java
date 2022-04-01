package com.example.civiladvocacy;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OfficialDownloader {
    private static final String TAG = "url";
    private static String urlToUse;
    static MainActivity mainAct;
    static RequestQueue queue;


    public static void downloadOfficials( MainActivity m, String url ){
        urlToUse = url;
        mainAct = m;
        queue = Volley.newRequestQueue(mainAct);


        Log.d(TAG, "downloadOfficials: " + urlToUse);

        Response.Listener<JSONObject> listener =
                response -> parseJSON_Main(response.toString());

        Response.ErrorListener error =
                error1 -> mainAct.locationError();

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    private static void parseJSON_Main(String s) {
        try {
            //start parsing the JSON
            JSONObject jObjMain = new JSONObject(s);
            //normalized input grabs the address
            JSONObject normalized = jObjMain.getJSONObject("normalizedInput");
            String address = normalized.getString("line1") + " " + normalized.get("city") + " " + normalized.get("state") + " " + normalized.get("zip");

            //big array with all the US positions and the index u can find more info abt the official
            JSONArray officeIndex = jObjMain.getJSONArray("offices");

            //all the info on each official like name and website and stuff
            JSONArray officialsArr = jObjMain.getJSONArray("officials");

            for (int i = 0; i < officeIndex.length(); i++){

                JSONObject office = officeIndex.getJSONObject(i);

                //sounds misleading but the name is actually the name of the office position
                String position = office.getString("name");


                //the array to get the officials index for the officials array
                JSONArray officialIndicesArr = office.getJSONArray("officialIndices");

                for (int j = 0; j < officialIndicesArr.length(); j++){
                    int index = officialIndicesArr.getInt(j);

                    JSONObject official = officialsArr.getJSONObject(index);
                    //name of the official
                    String name = official.getString("name");

                    //gets the party info
                    //apparently this value could be ommited so u gotta check if its even there first ... annoying
                    String party;
                    if(official.has("party")){
                        party = official.getString("party");
                    }
                    else{
                        party = "Unknown";
                    }

                    //phone number is store in an array

                    String phone;
                    if( official.has("phones") ){
                        JSONArray phoneArr = official.getJSONArray("phones");
                        //gets the first instance of the phone number
                        phone = phoneArr.getString(0);
                    }
                    else{
                        phone = "Unknown";
                    }

                    //gets the email from the official (official's emails are store in JSONArrays
                    String email;
                    if( official.has("emails") ){
                        JSONArray phoneArr = official.getJSONArray("emails");
                        //gets the first instance of the phone number
                        email = phoneArr.getString(0);
                    }
                    else{
                        email = "Unknown";
                    }

                    //address is stored in an array
                    String official_addr;
                    if(official.has("address")){
                        JSONArray addressArr = official.getJSONArray("address");
                        //usually only one object in the array that contains all the values for the address
                        JSONObject addressOb = addressArr.getJSONObject(0);
                        //building the address
                        official_addr = addressOb.getString("line1") + " " + addressOb.getString("city") + " " + addressOb.getString("state") + " " + addressOb.getString("zip");
                    }
                    else{
                        official_addr = "Unknown";
                    }

                    //there's multiple urls and they're store in an array
                    String website;
                    if(official.has("urls")){
                        JSONArray urls = official.getJSONArray("urls");
                        //just grabbing the first url from the array
                        website = urls.getString(0);
                    }
                    else {
                        website = "Unknown";
                    }

                    //takes the url for the official's photo
                    String photo;
                    if(official.has("photoUrl")){
                        photo = official.getString("photoUrl");
                    }
                    else{
                        photo = "Unknown";
                    }

                    //make an array of all the social media handles
                    String [] social = new String [3];
                    if(official.has("channels")){
                        JSONArray channels = official.getJSONArray("channels");
                        for(int k = 0; k < channels.length(); k++){
                            JSONObject media = channels.getJSONObject(k);
                            String type = media.getString("type");
                            if(type.equalsIgnoreCase("facebook")) {
                            //grabs both the type and the user id and places them in the array separated by a comma
                                String str = type + "," + media.getString("id");
                                social[0] = str;
                            }
                            else if (type.equalsIgnoreCase("twitter")) {
                                String str = type + "," + media.getString("id");
                                social[1] = str;
                            }
                            else if(type.equalsIgnoreCase("youtube")){
                                String str = type + "," + media.getString("id");
                                social[2] = str;
                            }

                        }
                    }
                    else{
                        for (int k = 0; k < 3; k++){
                            social[k] = null;
                        }
                    }


                    Official o = new Official(position, name, party, phone, email, official_addr , website, photo, social);

                    mainAct.addOfficalArray(o);

                }

            }

            //sets the address
            mainAct.setTitle(address);
            mainAct.loadRecycler();

            //shows a message when the location add was successful
            Toast.makeText(mainAct, mainAct.getString(R.string.new_loc), Toast.LENGTH_SHORT).show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
