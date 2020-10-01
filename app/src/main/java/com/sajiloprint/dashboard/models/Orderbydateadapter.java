package com.sajiloprint.dashboard.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.beingdev.magicprint.models.GenericProductModel;
//import com.beingdev.magicprint.prodcutscategory.Calendars;
//import com.bumptech.glide.Glide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajiloprint.dashboard.AddCardview;
import com.sajiloprint.dashboard.ItemsList;
import com.sajiloprint.dashboard.Orderitems;
import com.sajiloprint.dashboard.R;

import java.util.ArrayList;
import java.util.List;

public class Orderbydateadapter extends RecyclerView.Adapter<Orderbydateadapter.SearchViewHolder> {
    Context context;
    List<String> datelist;


   // ArrayList<GenericProductModel> ItemList;
    ArrayList<String> PrintList;


    static class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView carddate;
        LinearLayout datell;



        public SearchViewHolder( View itemView) {
            super(itemView);
            carddate = itemView.findViewById(R.id.cart_prtitle);
            datell = itemView.findViewById(R.id.datell);
        }


    }

    public Orderbydateadapter(Context context, List<String> datelist) {
        this.context = context;
        this.datelist = datelist;
    }


    @Override
    public Orderbydateadapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {

        holder.carddate.setText(datelist.get(position));


                holder.datell.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(context, ItemsList.class);
               intent.putExtra("date", datelist.get(position));
               context.startActivity(intent);

           }

       });

    }



    @Override
    public int getItemCount() {
        return datelist.size();

    }

}
