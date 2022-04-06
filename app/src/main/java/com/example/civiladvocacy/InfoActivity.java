package com.example.civiladvocacy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.ObjectInputStream;

public class InfoActivity extends AppCompatActivity {
    static Official currentOffcial;
    TextView official_position;
    TextView official_name;
    TextView official_party;
    ImageView official_photo;
    ImageView official_party_img;
    TextView official_address;
    TextView official_phone;
    TextView official_email;
    TextView official_website;
    static TextView titlebar;
    private Picasso picasso;
    private boolean noPicture = false;
    Button facebook;
    Button twitter;
    Button youtube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_info);

        official_position = findViewById(R.id.official_info_position);
        official_name = findViewById(R.id.official_info_name);
        official_party = findViewById(R.id.official_info_party);
        official_address = findViewById(R.id.official_info_address);
        official_phone = findViewById(R.id.official_info_phone);
        official_email = findViewById(R.id.official_info_email);
        official_website = findViewById(R.id.official_info_website);
        official_party_img = findViewById(R.id.official_info_party_img);
        official_photo = findViewById(R.id.official_info_photo);
        titlebar = findViewById(R.id.official_info_titlebar);
        facebook = findViewById(R.id.official_info_btn_face);
        twitter = findViewById(R.id.official_info_btn_twit);
        youtube = findViewById(R.id.official_info_btn_yt);
        picasso = Picasso.get();

        //sets the address
        setTitle(MainActivity.getAddress());

        //loads the official info that was clicked
        if(currentOffcial != null){

            // Find the root view to help change the background color
            View root = official_party.getRootView();

            //u gotta change the color of the background to match the party of the official
            if( currentOffcial.getParty().equalsIgnoreCase("(Democratic Party)") ){
                root.setBackgroundColor(getResources().getColor(R.color.blue, this.getTheme()));
                official_party_img.setImageResource(R.drawable.dem_logo);
            }
            else if (currentOffcial.getParty().equalsIgnoreCase("(Republican Party)")){
                root.setBackgroundColor(getResources().getColor(R.color.red, this.getTheme()));
                official_party_img.setImageResource(R.drawable.rep_logo);
            }
            else{
                //set to black if they're some other third party
                root.setBackgroundColor(getResources().getColor(R.color.black, this.getTheme()));
                official_party_img.setVisibility(View.GONE);
            }

            loadOfficial();
        }
        else {
            //do nothing for now
        }


    }

    //loads the information for an official
    public void loadOfficial(){

            //this sets all the information but we have to check if any of it is not there so it can be removed
            official_name.setText(currentOffcial.getName());
            official_position.setText(currentOffcial.getPosition());
            official_party.setText(currentOffcial.getParty());
            //have to make sure they have the attribute and if not, remove the textview
            if(currentOffcial.getPhone().equals("Unknown")){
                official_phone.setVisibility(View.GONE);
            }else{
                official_phone.setText(new StringBuilder().append(getString(R.string.phone)).append(" ").append(currentOffcial.getPhone()).toString());
                Linkify.addLinks(official_phone, Linkify.ALL);
                official_phone.setLinkTextColor(getResources().getColor(R.color.white));
            }

            //have to make sure they have the attribute and if not, remove the textview
            if(currentOffcial.getEmail().equals("Unknown")){
                official_email.setVisibility(View.GONE);
            }
            else{
                official_email.setText(new StringBuilder().append(getString(R.string.email)).append(" ").append(currentOffcial.getEmail()).toString());
                Linkify.addLinks(official_email, Linkify.ALL);
                official_email.setLinkTextColor(getResources().getColor(R.color.white));
            }

            //have to make sure they have the attribute and if not, remove the textview
            if(currentOffcial.getAddress().equals("Unknown")){
                official_address.setVisibility(View.GONE);
            }
            else{
                official_address.setText(new StringBuilder().append(getString(R.string.address)).append(" ").append(currentOffcial.getAddress()).toString());
                Linkify.addLinks(official_address, Linkify.ALL);
                //the address will sometimes need to be substring
                //this makes an underline for the textview
                //official_address.setPaintFlags(official_address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                official_address.setLinkTextColor(getResources().getColor(R.color.white));
            }

            //have to make sure they have the attribute and if not, remove the textview
            if(currentOffcial.getWebsite().equals("Unknown")){
                official_website.setVisibility(View.GONE);
            }
            else{
                official_website.setText(new StringBuilder().append(getString(R.string.website)).append(" ").append(currentOffcial.getWebsite()).toString());
                Linkify.addLinks(official_website, Linkify.ALL);
                official_website.setLinkTextColor(getResources().getColor(R.color.white));
            }

            //this is a flag that will be set if the official has no picture, that way no photo activity intent opens
            if(currentOffcial.getPhotoURL().equalsIgnoreCase("Unknown")){
                noPicture = true;
            }

            //checks that the official has that social media, if not it sets the visibility to zero so the
            //button does not appear and is unusable
            if (currentOffcial.getSocialLink()[0] == null){
                facebook.setVisibility(View.GONE);
            }
            if(currentOffcial.getSocialLink()[1] == null){
                twitter.setVisibility(View.GONE);
            }
            if(currentOffcial.getSocialLink()[2] == null){
                youtube.setVisibility(View.GONE);
            }

            //loads image using picasso!
            picasso.load(currentOffcial.getPhotoURL())
                .error(R.drawable.missing)
                .placeholder(R.drawable.placeholder)
                .into(official_photo);

    }

    //sets the information for the official (usually called from the Main class)
    public static void setCurrentOfficial(Official o){
        currentOffcial = o;
    }

    //sends the photo information when clicked
    public void onPhotoClick(View view) {
        if(!noPicture){
            //make sure to set the current official before starting the intent
            PhotoActivity.setCurrent(currentOffcial);
            Intent intent = new Intent(this, PhotoActivity.class);
            startActivity(intent);
        }
    }

    //onclick for the party image
    public void onClickParty(View view){
        String url;
        Intent intent = new Intent();

        if( currentOffcial.getParty().equalsIgnoreCase("(Democratic Party)") ){
            url = "https://democrats.org";
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
        }
        else {
            url = "https://www.gop.com";
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
        }
        //pass the url to intent data
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    //facebook is the first link in the array (index 0)
    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + currentOffcial.getSocialLink()[0].substring( currentOffcial.getSocialLink()[0].indexOf(",")+1 );;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                ObjectInputStream.GetField channels = null;
                urlToUse = "fb://page/" + channels.get("Facebook", 0);
            }
        } catch (PackageManager.NameNotFoundException | IOException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    //social media click intents
    //twitter is the second link in the array (index 1)
    public void twitterClicked(View v) {
        Intent intent = null;
        String name = currentOffcial.getSocialLink()[1].substring( currentOffcial.getSocialLink()[1].indexOf(",")+1 );
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    //youtube is the third link in the array (index 2)
    public void youTubeClicked(View v) {
        String name = currentOffcial.getSocialLink()[2].substring( currentOffcial.getSocialLink()[2].indexOf(",")+1 );
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void onClickAddress(View view){
        Intent intent = new Intent();
        String address = currentOffcial.getAddress();
        String url = currentOffcial.getAddress();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    //just makes sure the activity closes & info removed
    @Override
    public void onBackPressed(){
        currentOffcial = null;
        finish();
    }

    //allows other activities to get the address
    public static String getAddress(){
        return titlebar.getText().toString();
    }
    //set title of the activity to the address pulled from the JSON
    public void setTitle(String s){
        titlebar.setText(s);
    }

}
