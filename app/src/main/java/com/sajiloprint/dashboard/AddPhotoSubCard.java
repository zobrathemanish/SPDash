package com.sajiloprint.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sajiloprint.dashboard.models.SubCardsmodel;

public class AddPhotoSubCard extends AppCompatActivity {

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
    private MaterialSpinner spinner;
    private TextInputEditText title, bulkdescription, size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.su_card_photogifts_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        cardName = findViewById(R.id.tiet_movie_name);
        cardimage = findViewById(R.id.tiet_movie_logo);
        carddesc = findViewById(R.id.description);
        cardprice = findViewById(R.id.price);
        productid = findViewById(R.id.productid);
        bSubmit = findViewById(R.id.b_submit);
        title = findViewById(R.id.title);
        bulkdescription = findViewById(R.id.bulkdescription);
        size = findViewById(R.id.size);

        Bundle bundle = getIntent().getExtras();
        card = bundle.getString("cardname");

        category = card;


//        spinner = findViewById(R.id.categoryspinner);
//
//        spinner.setItems("Cards", "CorporateGifts", "Stationary", "Calendar", "WallDecors", "Awards", "Wearables", "Photogifts");
//        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
//
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                category = item;
//            }
//        });

        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //bSubmit.setOnClickListener(this);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.b_submit:
                        if(!isEmpty(cardName) && !isEmpty(cardimage) && !isEmpty(carddesc) && !isEmpty(cardprice) && !isEmpty(productid) ){
                            myNewCard(cardName.getText().toString().trim(),cardimage.getText().toString(),carddesc.getText().toString(),Float.parseFloat(cardprice.getText().toString()),Integer.parseInt(productid.getText().toString()),title.getText().toString(),size.getText().toString(),bulkdescription.getText().toString());
                        }else{
                            if(isEmpty(cardName)){
                                Toast.makeText(getApplicationContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
                            }else if(isEmpty(cardimage)){
                                Toast.makeText(getApplicationContext(), "Please specify a url for the image", Toast.LENGTH_SHORT).show();
                            }else if(isEmpty(carddesc)){
                                Toast.makeText(getApplicationContext(), "Please enter description", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //to remove current fragment
//                        getActivity().onBackPressed();
                        break;
                }
            }
        });


    }
    private boolean isEmpty(TextInputEditText textInputEditText) {
        return textInputEditText.getText().toString().trim().length() <= 0;
    }
    private void myNewCard(String name, String image, String desc, float price, int pid, String title, String size, String bulkdescription) {
        //Creating a movie object with user defined variables
        SubCardsmodel movie = new SubCardsmodel(pid,name,image,desc,price,title,size,bulkdescription);
        //referring to movies node and setting the values from movie object to that location
        mDatabaseReference.child("Products").child(category).push().setValue(movie);
        Intent intent = new Intent(AddPhotoSubCard.this, AddCardview.class);
        startActivity(intent);



    }



}