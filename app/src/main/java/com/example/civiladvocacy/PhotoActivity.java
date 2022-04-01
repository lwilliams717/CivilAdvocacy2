package com.example.civiladvocacy;

import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    TextView titlebar;
    TextView position;
    TextView name;
    ImageView photo;
    private Picasso picasso;
    static Official current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        titlebar = findViewById(R.id.photo_titlebar);
        position = findViewById(R.id.photo_position);
        name = findViewById(R.id.photo_name);
        photo = findViewById(R.id.photo_img);
        picasso = Picasso.get();

        loadData();

    }

    public static void setCurrent( Official o ){
        current = o;
    }

    public void loadData(){
        titlebar.setText( InfoActivity.getAddress() );
        position.setText(current.getPosition());
        name.setText(current.getName());

        // Find the root view to help change the background color
        View root = position.getRootView();

        //u gotta change the color of the background to match the party of the official
        if( current.getParty().equalsIgnoreCase("(Democratic Party)") ){
            root.setBackgroundColor(getResources().getColor(R.color.blue, this.getTheme()));
        }
        else if (current.getParty().equalsIgnoreCase("(Republican Party)")){
            root.setBackgroundColor(getResources().getColor(R.color.red, this.getTheme()));
        }
        else {
            //set to black if they're some other third party
            root.setBackgroundColor(getResources().getColor(R.color.black, this.getTheme()));
        }

        //loads image using picasso!
        picasso.load(current.getPhotoURL())
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(photo);
    }



    //just makes sure the activity closes
    @Override
    public void onBackPressed(){
        finish();
    }
}
