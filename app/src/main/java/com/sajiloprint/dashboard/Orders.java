package com.sajiloprint.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sajiloprint.dashboard.models.CardCartProductModel;
import com.sajiloprint.dashboard.models.Orderbydateadapter;
import com.sajiloprint.usersession.UserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Orders extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;
    private String currentuser;
    private FirebaseAuth auth;
    ArrayList<CardCartProductModel> ordercollect;
    private KProgressHUD progressDialog;
    private HashMap<String,String> user;
    private UserSession session;
    private String shopname;
    private String shopemail,shopmobile;
    List<String> datelist;
    Orderbydateadapter orderbydateadapter;








    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        Toolbar toolbar = findViewById(R.id.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ordercollect = new ArrayList<>();
        datelist = new ArrayList<>();
        getValues();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser().getEmail();

        tvNoMovies = (TextView) findViewById(R.id.tv_no_cards);
        mRecyclerView = findViewById(R.id.my_recycler_view);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        myorders();



    }


    private void myorders() {

        System.out.println("my orders");
        progressDialog = KProgressHUD.create(Orders.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        mDatabaseReference.child("shopusers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               ordercollect.clear();
                if(dataSnapshot.hasChild("orders")){
                    mDatabaseReference.child("shopusers").child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                final String parent = dataSnapshot.getKey();
                                System.out.println("parent is" + parent + shopmobile);
                                if(parent.equals(shopmobile)){
                                    System.out.println("here");
                                mDatabaseReference.child("shopusers").child("orders").child(parent).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (final DataSnapshot datasnapshot : snapshot.getChildren()) {
                                            final String date = datasnapshot.getKey();
                                            System.out.println("Date is" + date);
                                            datelist.add(date);

                                            if(tvNoMovies.getVisibility()== View.VISIBLE){
                                                  tvNoMovies.setVisibility(View.GONE);
                                                  }

                                            progressDialog.dismiss();

                                        }
                                        orderbydateadapter = new Orderbydateadapter(Orders.this, datelist,shopmobile);
                                        mRecyclerView.setAdapter(orderbydateadapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                                else{
                                    progressDialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                    progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        }

//    public void populateRecyclerView(final String date) {
//
////        System.out.println("parent " + parent + date + itemsuid);
//
//        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
//        final FirebaseRecyclerAdapter<CardCartProductModel,MovieViewHolder> adapter = new FirebaseRecyclerAdapter<CardCartProductModel, MovieViewHolder>(
//                CardCartProductModel.class,
//                R.layout.cart_item_layout,
//                MovieViewHolder.class,
//                //referencing the node where we want the database to store the data from our Object
//                mDatabaseReference.child("ShopOrders").child(getPlaced_user_mobile_no).child(date).getRef()
//
//        ) {
//
//            @Override
//            @Keep
//            protected void populateViewHolder(final MovieViewHolder viewHolder, final CardCartProductModel model, final int position) {
//                if(tvNoMovies.getVisibility()== View.VISIBLE){
//                    tvNoMovies.setVisibility(View.GONE);
//                }
//                viewHolder.cardname.setText(model.getPrname());
//                System.out.println("product name is " + model.getPrname());
//                viewHolder.cardprice.setText("NRs."+ model.getNo_of_items()*Float.parseFloat(model.getPrprice()));
//                viewHolder.cardcount.setText("Quantity : "+model.getNo_of_items());
//
//                viewHolder.deliverycharge.setText("Delivery Charge:" + model.getDeliveryprice());
//                viewHolder.productid.setText(Integer.toString(model.getPrid()));
//
//
//
//                Picasso.with(Orders.this).load(model.getPrimage()).into(viewHolder.cardimage);
//
//                ordercollect.add(model);
//
//                viewHolder.item_product.setOnClickListener(new View.OnClickListener(){
//
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(Orders.this, Orderitems.class);
//                        intent.putExtra("ordercollect",ordercollect.get(position));
//                        intent.putExtra("userphone",parent);
//                        intent.putExtra("orderdate",date);
//                        startActivity(intent);
//                    }
//                });
//
//
//
//
////                viewHolder.carddelete.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Toast.makeText(Cart.this,getItem(position).getPrname(),Toast.LENGTH_SHORT).show();
////                        getRef(position).removeValue();
////                        session.decreaseCartValue();
////                        deletedatabaseimage(getItem(position).getimageid());
////                        startActivity(new Intent(Cart.this,Cart.class));
////                        finish();
////                    }
////                });
//            }
//        };
//        mRecyclerView.setAdapter(adapter);
//    }


    private void getValues() {

        //create new session object by passing application context
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        //get User details if logged in
        HashMap<String, String> user = session.getUserDetails();

        shopname = user.get(UserSession.KEY_NAME);
        shopemail = user.get(UserSession.KEY_EMAIL);
        shopmobile = user.get(UserSession.KEY_MOBiLE);
        System.out.println("nameemailmobile " + shopname + shopemail + shopmobile);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }





}
