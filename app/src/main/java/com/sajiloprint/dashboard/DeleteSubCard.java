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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sajiloprint.dashboard.models.DeleteCardsModel;

public class DeleteSubCard extends AppCompatActivity {

    //created for firebaseui android tutorial by Vamsi Tallapudi

    private DatabaseReference mDatabaseReference;
    private TextInputEditText cardName;
    private String textcardName;
    private TextInputEditText cardimage;
    private TextInputEditText carddesc;
    private TextInputEditText cardprice;
    private TextInputEditText productid;
    private String card;

    private Button bSubmit;
    private String category;
    private String tobedeleted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_card_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        cardName = findViewById(R.id.tiet_movie_name);
        bSubmit = findViewById(R.id.b_submit);

        Bundle bundle = getIntent().getExtras();
        card = bundle.getString("cardname");

        category = card;


        bSubmit = findViewById(R.id.b_submit);


        //tobedeleted = cardName.getText().toString();



        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //bSubmit.setOnClickListener(this);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.b_submit:
                        if(!isEmpty(cardName)){
                            myNewCard(cardName.getText().toString().trim());
                        }else{
                            if(isEmpty(cardName)){
                                Toast.makeText(getApplicationContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
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
    private void myNewCard(String name) {
        //Creating a movie object with user defined variables
        DeleteCardsModel movie = new DeleteCardsModel(name);
        //referring to movies node and setting the values from movie object to that location
        final String attribute = movie.getCardname();
        System.out.println("attribute is" + attribute);

        mDatabaseReference.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String parent = snapshot.getKey();

                    mDatabaseReference.child("Products").child(parent).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                                String UID = datasnapshot.getKey();
                                String card_name = datasnapshot.child("cardname").getValue(String.class);

                                if(card_name.toLowerCase().contains(attribute.toLowerCase())) {
                                    System.out.println("UID is " + UID);
                                    System.out.println("is this the cardname?" + card_name);
                                    System.out.println("The category is " + category);

                                    mDatabaseReference.child("Products").child(category).child(UID).removeValue();
                                    Intent intent = new Intent(DeleteSubCard.this, DeleteCardView.class);
                                    intent.putExtra("show",category);
                                    startActivity(intent);

                                }





                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }



}