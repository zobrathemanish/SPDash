package com.sajiloprint.dashboard.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sajiloprint.dashboard.Orders;
import com.sajiloprint.dashboard.R;

import java.util.ArrayList;
import java.util.List;

public class Orderbydateadapter extends RecyclerView.Adapter<Orderbydateadapter.SearchViewHolder> {
    Context context;
    List<String> datelist;
    String shopmobile;


   // ArrayList<GenericProductModel> ItemList;
    ArrayList<String> PrintList;


    static class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView carddate;
        LinearLayout datell;
        ImageView deletecard;


        public SearchViewHolder( View itemView) {
            super(itemView);
            carddate = itemView.findViewById(R.id.cart_prtitle);
            datell = itemView.findViewById(R.id.datell);
            deletecard = itemView.findViewById(R.id.deletecard);
        }


    }

    public Orderbydateadapter(Context context, List<String> datelist, String shopmobile) {
        this.context = context;
        this.datelist = datelist;
        this.shopmobile = shopmobile;
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

                holder.deletecard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteorder(datelist.get(position),shopmobile);
                    }
                });

    }



    @Override
    public int getItemCount() {
        return datelist.size();

    }

    private void deleteorder(String date, String shopmobile){

        //Getting reference to Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = database.getReference();

        mDatabaseReference.child("shopusers").child("orders").child(shopmobile).child(date).removeValue();
        Intent intent = new Intent (context, Orders.class);
        context.startActivity(intent);
//        Toast.makeText(context,"Deleted!! Pull to refresh!")

    }

}
