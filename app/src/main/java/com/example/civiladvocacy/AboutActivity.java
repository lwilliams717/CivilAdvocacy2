package com.example.civiladvocacy;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    TextView googleApiLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        googleApiLink = findViewById(R.id.about_api);
        Linkify.addLinks(googleApiLink, Linkify.ALL);
        googleApiLink.setLinkTextColor(getResources().getColor(R.color.light_blue));

    }

}
