package com.sajiloprint.dashboard.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

//import com.beingdev.magicprint.models.GenericProductModel;
//import com.beingdev.magicprint.prodcutscategory.Calendars;
//import com.bumptech.glide.Glide;

import com.sajiloprint.dashboard.AddCardview;
import com.sajiloprint.dashboard.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> showList;

   // ArrayList<GenericProductModel> ItemList;
    ArrayList<String> PrintList;


    static class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView cardName;



        public SearchViewHolder( View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.card_name);
        }


    }

    public SearchAdapter(Context context, ArrayList<String> showList) {
        this.context = context;
        this.showList = showList;
    }


    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_items, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {

            holder.cardName.setText(showList.get(position));
        System.out.println("is cardname showing?");
      //  holder.cardDesc.setText(cardDescList.get(position));

                holder.cardName.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

            //   GenericProductModel model = new GenericProductModel(cardId,cardImageList,);
//               if(ItemList.get(position).getCardid()==4378) {
//
//                   Intent intent = new Intent(context, Calendars.class);
//                   intent.putExtra("product",ItemList.get(position));
//                   intent.putExtra("cardname",ItemList.get(position).getCardname());
//                   context.startActivity(intent);
//
//
//
//               }else{
                   Intent intent = new Intent(context, AddCardview.class);
                   intent.putExtra("show", showList.get(position));
                   context.startActivity(intent);
//               }

           }

       });

    }



    @Override
    public int getItemCount() {
        return showList.size();

    }

}
