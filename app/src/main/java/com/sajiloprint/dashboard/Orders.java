package com.sajiloprint.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajiloprint.dashboard.models.SubCardsmodel;
import com.squareup.picasso.Picasso;

public class Orders extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;
    private String currentuser;
    private FirebaseAuth auth;



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

        mDatabaseReference.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    final String parent = dataSnapshot.getKey();
                    System.out.println("parent is"+parent);
                    mDatabaseReference.child("orders").child(parent).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (final DataSnapshot datasnapshot : snapshot.getChildren()) {
                                final String date = datasnapshot.getKey();
                                System.out.println("Date is" + date);
                                mDatabaseReference.child("orders").child(parent).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (final DataSnapshot datasnapshot : snapshot.getChildren()) {
                                            String iditems=datasnapshot.getKey();
                                            System.out.println("Id items are "+ iditems);

                                            mDatabaseReference.child("orders").child(parent).child(date).child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (final DataSnapshot datasnapshot : snapshot.getChildren()) {
                                                        String itemsuid=datasnapshot.getKey();
                                                        mDatabaseReference.child("orders").child(parent).child(date).child("items").child(itemsuid).addListenerForSingleValueEvent(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                String shopemail = datasnapshot.child("shopemail").getValue(String.class);
                                                                System.out.println("shopemail found "+ shopemail);
                                                                if(shopemail.equals(currentuser)){
                                                                    System.out.println("You have orders");
                                                                }


                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });


                                                    }

//



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
