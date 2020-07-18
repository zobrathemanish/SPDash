package com.sajiloprint.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.utils.JsonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sajiloprint.dashboard.models.CardsModel;
import com.sajiloprint.dashboard.models.DeleteCardsModel;


public class deleteCardFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference mDatabaseReference;
    private TextInputEditText cardName;
    private Button bSubmit;
    private String category;
    private MaterialSpinner spinner;
    private String UID="cards";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.delete_card_fragment,container,false);

        cardName = v.findViewById(R.id.tiet_movie_name);
        bSubmit = v.findViewById(R.id.b_submit);
        category = "Cards";

        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        bSubmit.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.b_submit:
                if(!isEmpty(cardName)){
                    myNewCard(cardName.getText().toString().trim());
                }else{
                    if(isEmpty(cardName)){
                        Toast.makeText(getContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
                    }
                }
                //to remove current fragment
                getActivity().onBackPressed();
                break;
        }
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
                                System.out.println(card_name);
                                if(card_name.toLowerCase().contains(attribute.toLowerCase())) {
                                    System.out.println("category is +"+ category);
                                    System.out.println("UID is" + UID);
                                    mDatabaseReference.child("Products").child(category).child(UID).removeValue();

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
    //check if edittext is empty
    private boolean isEmpty(TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }
}