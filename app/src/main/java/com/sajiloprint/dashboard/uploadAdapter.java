package com.sajiloprint.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.sajiloprint.dashboard.models.ImageUploadModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class uploadAdapter extends RecyclerView.Adapter<uploadAdapter.UploadViewHolder> {
    Context context;
    ArrayList<String> cardNameList;
    ArrayList<String> cardImageList;
    ArrayList<ImageUploadModel> ItemList;





    static class UploadViewHolder extends RecyclerView.ViewHolder{
        ImageView cardImage;
        public View mView;
        public TextView imgqty;
        public TextView imglocation;





        public UploadViewHolder( View itemView) {
            super(itemView);
            mView = itemView;
            cardImage = itemView.findViewById(R.id.useruploadimage);
            imgqty = itemView.findViewById(R.id.imgqty);
            imglocation = itemView.findViewById(R.id.imglocation);
        }


    }

    public uploadAdapter(Context context, ArrayList<ImageUploadModel> ItemList) {
        this.context = context;

        this.ItemList = ItemList;
    }


    @Override
    public uploadAdapter.UploadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_uploadimageid, parent, false);
        return new UploadViewHolder(view);
    }

    @Override
    public void onBindViewHolder( UploadViewHolder holder, int position) {

        holder.imgqty.setText("Image Quantity: " + ItemList.get(position).getQuantity());
        holder.imglocation.setText(ItemList.get(position).getFimageLocation());

        Picasso.with(context).load(ItemList.get(position).getFimageLocation()).config(Bitmap.Config.RGB_565).resize(800, 600).into(holder.cardImage);

        holder.mView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {






           }

       });

    }



    @Override
    public int getItemCount() {
        return cardNameList.size();

    }

}
