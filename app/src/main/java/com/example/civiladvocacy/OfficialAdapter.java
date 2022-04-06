package com.example.civiladvocacy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {
    private ArrayList<Official> officials = new ArrayList<>();
    private final MainActivity mainAct;

    public OfficialAdapter(ArrayList<Official> o, MainActivity m){
        this.officials = o;
        this.mainAct = m;
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gov_offical_item, parent, false);
        itemView.setOnClickListener(mainAct);
        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {
        Official o = officials.get(position);
        holder.official_title.setText( o.getPosition() );
        String temp = o.getName() + " " + o.getParty();
        holder.official_name.setText( temp );
    }


    @Override
    public int getItemCount() {
        return officials.size();
    }
}
