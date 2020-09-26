package com.sajiloprint.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajiloprint.dashboard.models.CardCartProductModel;
import com.sajiloprint.dashboard.models.PlacedOrderModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Orderitems extends AppCompatActivity {

    private TextView userid,productid,productname,productprice,orderdate,deliverydate;
    private TextView username,useremail;
    private TextView usermobile,paymentmode,deliverycharge,quantity,totalamount,totalamount2;
    private RecyclerView mRecyclerview;
    private CardCartProductModel ordercollect;
    private String date;
    private ImageView userphoto,productimage;
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
        mRecyclerview = findViewById(R.id.userimage_recycler_view);
        if (mRecyclerview != null) {
            //to enable optimization of recyclerview
            mRecyclerview.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(mLayoutManager);

        Bundle bundle = getIntent().getExtras();
        ordercollect =  (CardCartProductModel) getIntent().getSerializableExtra("product");
        String parent = bundle.get("userphone").toString();
        String date = bundle.get("orderdate").toString();

        getuserdata(parent,date);
        productid.setText(ordercollect.getPrid());
        productname.setText(ordercollect.getPrname());
        productprice.setText(ordercollect.getPrprice());
        deliverycharge.setText("Delivery charge: " + ordercollect.getDeliveryprice());
        quantity.setText("Number of items(Quantity): " + ordercollect.getNo_of_items());
        Picasso.with(Orderitems.this).load(ordercollect.getPrimage()).into(productimage);




    }

    public void getuserdata(final String parent, final String date){
        mDatabaseReference.child("orders").child(parent).child(date).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot datasnapshot : snapshot.getChildren()) {
                    final String UID = datasnapshot.getKey();
                    if(!parent.equals("items")){
                        PlacedOrderModel ordermodel = snapshot.child(UID).getValue(PlacedOrderModel.class);

                        userid.setText(ordermodel.getOrderid());
                        username.setText(ordermodel.getDeleiveryname());
                        usermobile.setText(ordermodel.getDeliverymobile_no());
                        paymentmode.setText("Payment mode: "+ ordermodel.getPayment_mode());
                        useremail.setText("Email: "+ ordermodel.getDeliveryemail());
                        orderdate.setText("Order Date: "+ date);
                        deliverydate.setText("To be delivered on: "+ ordermodel.getDelivery_date());
                        totalamount.setText("Total Amount: "+ ordermodel.getTotal_amount());
                        totalamount2.setText("Total Amount: "+ ordermodel.getTotal_amount());


                        Picasso.with(Orderitems.this).load(ordermodel.getPhoto()).into(userphoto);

                    }

                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        }
}
