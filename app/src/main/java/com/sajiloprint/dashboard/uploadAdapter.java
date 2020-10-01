package com.sajiloprint.dashboard;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sajiloprint.dashboard.models.ImageUploadModel;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class uploadAdapter extends RecyclerView.Adapter<uploadAdapter.UploadViewHolder> {
    private static final String LOG_TAG = "SPDownload" ;
    private Context context;
    private ArrayList<ImageUploadModel> ItemList;
    OutputStream outputStream;





    static class UploadViewHolder extends RecyclerView.ViewHolder{
        ImageView cardImage;
        View mView;
        TextView imgqty;
        TextView imglocation;
        Button button;
        private KProgressHUD progressDialog;







        UploadViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            cardImage = itemView.findViewById(R.id.useruploadimage);
            imgqty = itemView.findViewById(R.id.imgqty);
            imglocation = itemView.findViewById(R.id.imglocation);
            button = itemView.findViewById(R.id.downloadimage);

        }


    }

    uploadAdapter(Context context, ArrayList<ImageUploadModel> ItemList) {
        this.context = context;

        this.ItemList = ItemList;
    }


    @Override
    public uploadAdapter.UploadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_uploadimageid, parent, false);
        return new UploadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UploadViewHolder holder, final int position) {


//
//        System.out.println("Itemlistfimage" + ItemList.get(position).getFimageLocation());
//        holder.imglocation.setText(ItemList.get(position).getFimageLocation());

//        System.out.println("The image location " + ItemList.get(position).getFimageLocation());
        Glide.with(context).load(ItemList.get(position).getFimageLocation()).asBitmap().into(holder.cardImage);

//        Glide.with(mContext).load(serviceProvider.getImage())
//                .thumbnail(0.5f)
//                .crossFade()
//                .placeholder(R.drawable.placeholder)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.iv_profile_image);


//       Picasso.with(context).load(ItemList.get(position).getFimageLocation()).config(Bitmap.Config.RGB_565).resize(800, 600).into(holder.cardImage);

        holder.imglocation.setVisibility(View.GONE);
        if(ItemList.get(position).getQuantity()==null)
            holder.imgqty.setVisibility(View.GONE);
        else
            holder.imgqty.setText("Image Quantity: " + ItemList.get(position).getQuantity());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("asdf" + ItemList.get(position).getFimageLocation());

                String filename = "filename.jpg";
                String downloadUrlOfImage = ItemList.get(position).getFimageLocation();
                File direct =
                        new File(Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                .getAbsolutePath() + "/" + "Sajilo Print" + "/");


                if (!direct.exists()) {
                    direct.mkdir();
                    Log.d(LOG_TAG, "dir created for first time");
                }

                DownloadManager dm = (DownloadManager) holder.cardImage.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(downloadUrlOfImage);
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(filename)
                        .setMimeType("image/jpeg")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                                File.separator + "Sajilo Print" + File.separator + filename);

                dm.enqueue(request);

            }
        });


    }









    @Override
    public int getItemCount() {
        return ItemList.size();

    }

}
