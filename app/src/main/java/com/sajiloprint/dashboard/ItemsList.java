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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajiloprint.dashboard.models.CardCartProductModel;
import com.sajiloprint.usersession.UserSession;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ItemsList extends AppCompatActivity {

    private RecyclerView mRecyclerview;

    private UserSession session;
    private String shopname;
    private String shopemail,shopmobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        Bundle bundle = getIntent().getExtras();
        String date = bundle.get("date").toString();

        getValues();

        mRecyclerview = findViewById(R.id.itemlist_recycler);

//        if (mRecyclerview != null) {
//            //to enable optimization of recyclerview
//            mRecyclerview.setHasFixedSize(true);
//        }

//        //using staggered grid pattern in recyclerview
//        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //using staggered grid pattern in recyclerview
        mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        populateRecyclerView(date);



    }

    private void populateRecyclerView(String date) {
        System.out.println("The shopmobile is " + shopmobile);

        //Getting reference to Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = database.getReference();

        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
        final FirebaseRecyclerAdapter<CardCartProductModel,MovieViewHolder> adapter = new FirebaseRecyclerAdapter<CardCartProductModel, MovieViewHolder>(
                CardCartProductModel.class,
                R.layout.itemsordered,
                MovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference.child("shopusers").child("orders").child(shopmobile).child(date).child("items").getRef()

        ) {

            @Override
            @Keep
            protected void populateViewHolder(final MovieViewHolder viewHolder, final CardCartProductModel model, final int position) {
                System.out.println("The cardname and count is " + model.getPrname() + model.getNo_of_items());
                viewHolder.cardname.setText(model.getPrname());
                viewHolder.cardcount.setText("Quantity : "+model.getNo_of_items());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ItemsList.this, Orderitems.class);
                        intent.putExtra("ordercollect",getItem(position));
//                        intent.putExtra("userphone",parent);
//                        intent.putExtra("orderdate",date);
                        startActivity(intent);
                    }
                });

//                cartcollect.add(model);

            }
        };
        mRecyclerview.setAdapter(adapter);


    }

    //viewHolder for our Firebase UI
    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView cardname;
        TextView cardcount;


        View mView;
        public MovieViewHolder(View v) {
            super(v);
            mView = v;
            cardname = v.findViewById(R.id.cart_prtitle);
            cardcount = v.findViewById(R.id.cart_prcount);

        }
    }


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
}
