package com.sajiloprint.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sajiloprint.dashboard.models.SubCardsmodel;

public class AddSubCard extends AppCompatActivity {

    //created for firebaseui android tutorial by Vamsi Tallapudi

    private DatabaseReference mDatabaseReference;
    private TextInputEditText cardName;
    private TextInputEditText cardimage;
    private TextInputEditText carddesc;
    private TextInputEditText cardprice;
    private TextInputEditText productid;
    private String card;

    private Button bSubmit;
    private String category;
    private TextView upload;
    private boolean uploadflag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_card_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        cardName = findViewById(R.id.tiet_movie_name);
        cardimage = findViewById(R.id.tiet_movie_logo);
        carddesc = findViewById(R.id.description);
        cardprice = findViewById(R.id.price);
        productid = findViewById(R.id.productid);
        bSubmit = findViewById(R.id.b_submit);
        upload = findViewById(R.id.uploadimages);

        Bundle bundle = getIntent().getExtras();
        card = bundle.getString("cardname");

        category = card;

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadflag = true;
            }
        });



        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //bSubmit.setOnClickListener(this);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.b_submit) {
                    if (!isEmpty(cardName) && (!isEmpty(cardimage) || uploadflag) && !isEmpty(carddesc) && !isEmpty(cardprice) && !isEmpty(productid)) {
                        myNewCard(cardName.getText().toString().trim(), cardimage.getText().toString(), carddesc.getText().toString(), Float.parseFloat(cardprice.getText().toString()), Integer.parseInt(productid.getText().toString()));
                    } else {
                        if (isEmpty(cardName)) {
                            Toast.makeText(getApplicationContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
                        } else if (isEmpty(cardimage)) {
                            Toast.makeText(getApplicationContext(), "Please specify a url or upload image", Toast.LENGTH_SHORT).show();
                        } else if (isEmpty(carddesc)) {
                            Toast.makeText(getApplicationContext(), "Please enter description", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //to remove current fragment
//                        getActivity().onBackPressed();
                }
            }
        });


    }
    private boolean isEmpty(TextInputEditText textInputEditText) {
        return textInputEditText.getText().toString().trim().length() <= 0;
    }
    private void myNewCard(String name, String image, String desc, float price, int pid) {
        //Creating a movie object with user defined variables
        SubCardsmodel movie = new SubCardsmodel(pid,name,image,desc,price);
        //referring to movies node and setting the values from movie object to that location
        mDatabaseReference.child("Products").child(category).push().setValue(movie);
        Intent intent = new Intent(AddSubCard.this, AddCardview.class);
        startActivity(intent);



    }



}