package com.sajiloprint.dashboard;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajiloprint.dashboard.models.CardCartProductModel;
import com.sajiloprint.dashboard.models.ImageUploadModel;
import com.sajiloprint.dashboard.models.PlacedOrderModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Orderitems extends AppCompatActivity {

    private TextView userid,productid,productname,productprice,orderdate,deliverydate;
    private TextView username,useremail;
    private TextView usermobile,paymentmode,deliverycharge,quantity,totalamount,totalamount2;
    private TextView name_card,business_name,job_title,full_address,email,contact_num,business_web;
    private TextView finalcolor,finalshape;
    private RecyclerView mRecyclerview;
    private CardCartProductModel ordercollect;
    private String date;
    private ImageView userphoto,productimage;
    private ArrayList<ImageUploadModel> imageslist;
    private uploadAdapter adapter;
    private LinearLayout carddetailsll;
    private LinearLayout mugsdetailsll;
    private ImageView mugcolor, mugshape;
    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderitems);

        userid = findViewById(R.id.userid);
        username = findViewById(R.id.username);
        usermobile = findViewById(R.id.mobile_number);
        paymentmode = findViewById(R.id.paymentmode);
        useremail = findViewById(R.id.useremail);
        productid = findViewById(R.id.productid);
        productname = findViewById(R.id.prtitle);
        productprice = findViewById(R.id.prprice);
        deliverycharge = findViewById(R.id.delivercharge);
        quantity = findViewById(R.id.prcount);
        totalamount = findViewById(R.id.totalamount);
        orderdate = findViewById(R.id.orderdate);
        deliverydate = findViewById(R.id.deliverydate);
        userphoto = findViewById(R.id.userphoto);
        productimage = findViewById(R.id.image_cartlist);

        carddetailsll = findViewById(R.id.carddetailsll);
        name_card = findViewById(R.id.name_card);
        business_name = findViewById(R.id.business_name);
        job_title = findViewById(R.id.job_title);
        full_address = findViewById(R.id.full_address);
        email = findViewById(R.id.email);
        contact_num = findViewById(R.id.contact_num);
        business_web = findViewById(R.id.business_web);

        mugsdetailsll = findViewById(R.id.mugdetailsll);
        finalcolor = findViewById(R.id.finalcolor);
        finalshape = findViewById(R.id.finalshape);
        mugcolor = findViewById(R.id.imgfcolor);
        mugshape = findViewById(R.id.imgfshape);

        imageslist = new ArrayList<>();

        mRecyclerview = findViewById(R.id.userimage_recycler_view);
        if (mRecyclerview != null) {
            //to enable optimization of recyclerview
            mRecyclerview.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        Bundle bundle = getIntent().getExtras();
        ordercollect =  (CardCartProductModel) getIntent().getSerializableExtra("ordercollect");
        String shopmobile = bundle.get("shopmobile").toString();
//        String parent = bundle.get("userphone").toString();
        String date = bundle.get("orderdate").toString();

        getuserdata(shopmobile, date);
        productid.setText(Integer.toString(ordercollect.getPrid()));
        productname.setText(ordercollect.getPrname());
        productprice.setText(ordercollect.getPrprice());
        deliverycharge.setText("Delivery charge: " + ordercollect.getDeliveryprice());
        quantity.setText("Number of items(Quantity): " + ordercollect.getNo_of_items());
        Picasso.with(Orderitems.this).load(ordercollect.getPrimage()).into(productimage);

        populateRecyclerView(ordercollect.getimageid());

        if(ordercollect.getCard_name()==null && ordercollect.getCard_company_name()==null && ordercollect.getCard_formjob()==null && ordercollect.getCard_name()==null
        && ordercollect.getCard_company_address()==null && ordercollect.getCard_email()==null && ordercollect.getCard_number()==null && ordercollect.getCard_website()==null ){

            carddetailsll.setVisibility(View.GONE);
        }

        else{
            name_card.setText("Name in Card: " +ordercollect.getCard_name() );
            business_name.setText("Business Name:  " +ordercollect.getCard_company_name() );
            job_title.setText("Job Title:  " +ordercollect.getCard_formjob() );
            full_address.setText("Full Address:  " +ordercollect.getCard_company_address() );
            email.setText("Email:  " +ordercollect.getCard_email() );
            contact_num.setText("Contact number:  " +ordercollect.getCard_number() );
            business_web.setText("Business Website:  " +ordercollect.getCard_website() );
        }


        if(ordercollect.getFinalshape()==null && ordercollect.getfinalcolor()==null){
            mugsdetailsll.setVisibility(View.GONE);
        }
        else{
            finalcolor.setText(" Colour: ");
            finalshape.setText(" Shape: ");
            switch(ordercollect.getFinalshape()){
                case "shape1":
                    int imageResource = getResources().getIdentifier("@mipmap/mugshape1", null, getPackageName());
                    mugshape.setImageDrawable(getResources().getDrawable(imageResource));
                    break;

                case "shape2":
                     imageResource = getResources().getIdentifier("@mipmap/shape2mug", null, getPackageName());
                    mugshape.setImageDrawable(getResources().getDrawable(imageResource));
                    break;
                case "shape3":
                     imageResource = getResources().getIdentifier("@mipmap/shape3", null, getPackageName());
                    mugshape.setImageDrawable(getResources().getDrawable(imageResource));
                    break;
                case "shape4":
                     imageResource = getResources().getIdentifier("@mipmap/shape4", null, getPackageName());
                    mugshape.setImageDrawable(getResources().getDrawable(imageResource));
                    break;
                case "shape5":
                     imageResource = getResources().getIdentifier("@mipmap/shape", null, getPackageName());
                    mugshape.setImageDrawable(getResources().getDrawable(imageResource));
                    break;
            }
            switch(ordercollect.getfinalcolor()){
                case "color1":
                    int imageResource = getResources().getIdentifier("@mipmap/mugcolor1", null, getPackageName());
                    mugcolor.setImageDrawable(getResources().getDrawable(imageResource));
                    break;

                case "color2":
                    imageResource = getResources().getIdentifier("@mipmap/mugcolor2", null, getPackageName());
                    mugcolor.setImageDrawable(getResources().getDrawable(imageResource));
                    break;
                case "color3":
                    imageResource = getResources().getIdentifier("@mipmap/mugcolor3", null, getPackageName());
                    mugcolor.setImageDrawable(getResources().getDrawable(imageResource));
                    break;
                case "color4":
                    imageResource = getResources().getIdentifier("@mipmap/mugcolor4", null, getPackageName());
                    mugcolor.setImageDrawable(getResources().getDrawable(imageResource));
                    break;
                case "color5":
                    imageResource = getResources().getIdentifier("@mipmap/mugcolor5", null, getPackageName());
                    mugshape.setImageDrawable(getResources().getDrawable(imageResource));
                    break;
            }

        }




//        if()




    }

    public void getuserdata(final String shopmobile, final String date){
        mDatabaseReference.child("shopusers").child("orders").child(shopmobile).child(date).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot datasnapshot : snapshot.getChildren()) {
                    final String UID = datasnapshot.getKey();
                    if(!UID.equals("items")){
                        System.out.println("Here thess UID is" + UID);

                        PlacedOrderModel ordermodel = snapshot.child(UID).getValue(PlacedOrderModel.class);

                        userid.setText(ordermodel.getOrderid());
                        username.setText(ordermodel.getDeleiveryname());
                        usermobile.setText(ordermodel.getDeliverymobile_no());
                        paymentmode.setText("Payment mode: "+ ordermodel.getPayment_mode());
                        useremail.setText("Email: "+ ordermodel.getDeliveryemail());
                        orderdate.setText("Order Date: "+ date);
                        deliverydate.setText("To be delivered on: "+ ordermodel.getDelivery_date());
                        totalamount.setText("Total Amount: "+ ordermodel.getTotal_amount());
//                        totalamount2.setText("Total Amount: "+ ordermodel.getTotal_amount());


                        Picasso.with(Orderitems.this).load(ordermodel.getPhoto()).into(userphoto);

                    }

                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        }

    public void populateRecyclerView(final String imageid) {

        mDatabaseReference.child("Images").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    final String parent = dataSnapshot.getKey();
                    mDatabaseReference.child("Images").child(parent).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (final DataSnapshot datasnapshot : snapshot.getChildren()) {
                                String UID = datasnapshot.getKey();
                                String uploadimageid = datasnapshot.child("uploadimageid").getValue(String.class);
                                if(imageid.equals(uploadimageid)){
                                    ImageUploadModel uploadmodel = snapshot.child(UID).getValue(ImageUploadModel.class);
                                    imageslist.add(uploadmodel);
                                    System.out.println("fimagelocation" + uploadmodel.getFimageLocation());

                                }


                            }

                            adapter = new uploadAdapter(Orderitems.this,imageslist);
                            mRecyclerview.setAdapter(adapter);


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

}
