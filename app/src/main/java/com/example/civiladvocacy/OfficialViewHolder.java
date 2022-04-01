package com.example.civiladvocacy;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {

    TextView official_title;
    TextView official_name;

    public OfficialViewHolder(View view){
        super(view);
        official_title = view.findViewById(R.id.official_title);
        official_name = view.findViewById(R.id.official_name);
    }

}